package controllers;

import com.typesafe.config.Config;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import dto.file.FileDTO;
import exceptions.BadRequestException;
import models.File;
import models.TripNode;
import models.User;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;
import service.FileService;
import service.TripService;
import utils.AsyncHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FileController extends Controller {

    private FileService fileService;
    private TripService tripService;
    private UserRepository userRepository;
    private String filesPath;

    @Inject
    public FileController(Config config, FileService fileService, TripService tripService, UserRepository userRepository) {
        this.fileService = fileService;
        String rootPath = System.getProperty("user.home");
        filesPath = rootPath + config.getString("filesPath");
    }

    /**
     * Endpoint for uploading of files to a trip. Allows multiple files to be uploaded at once
     * @param request request object
     * @param nodeId id of the node that a file is being added to
     * @param userId user who is uploading the file
     * @return
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> uploadFiles(Http.Request request, Long nodeId, Long userId) {

        Http.MultipartFormData<Files.TemporaryFile> mfd = request.body().asMultipartFormData();

        List<Http.MultipartFormData.FilePart<Files.TemporaryFile>> fileList;

        fileList = mfd.getFiles();
        for (Http.MultipartFormData.FilePart<Files.TemporaryFile> file: fileList) {
            if (file.getFileSize() > 12000000) {
                return CompletableFuture.completedFuture(badRequest(APIResponses.FILE_TOO_LARGE));
            }
        }
        return fileService.createNewFiles(fileList, nodeId, userId).thenApplyAsync(files -> {
            return ok();
        }).handle(AsyncHandler::handleResult);
    }

    /**
     * Endpoint which returns a list of FileDTO objects which displays data about the files
     * @param request request object
     * @param nodeId id of the trip
     * @param userId id of the user sending the request
     * @return List of file DTO
     */
    public CompletionStage<Result> getFilesForTrip(Http.Request request, Long nodeId, Long userId) {
        return fileService
            .getFilesForTripNode(nodeId).thenApplyAsync(files -> {
                List<FileDTO> fileDTOS = new ArrayList<>();

                for(File file : files) {
                    fileDTOS.add(new FileDTO(file));
                }

                return ok(Json.toJson(fileDTOS));
            }).handle(AsyncHandler::handleResult);
    }

    /**
     * Deletes a file by its Id
     * @param request request object
     * @param nodeId id of the trip
     * @param fileId id of the file
     * @param userId id of the user sending the request
     * @return ok response
     */
    public CompletionStage<Result> deleteFileById(Http.Request request, Long nodeId, Long fileId, Long userId) {
        CompletionStage<TripNode> tripStage = tripService.getTripByIdHandler(nodeId);
        CompletionStage<User> userStage = userRepository.getUserHandler(userId);
        CompletionStage<Void> permissionStage = tripStage.thenCombineAsync(userStage, (tripNode, user) -> tripService.checkWritePermissionHandler(tripNode, user).join());

        return permissionStage.thenComposeAsync(permission -> fileService.deleteFileById(fileId, nodeId, userId))
            .thenApplyAsync(output -> ok()).handle(AsyncHandler::handleResult);
    }

    /**
     * Returns the file object and renames it to the old name from the hash
     * @param request request object
     * @param fileId id of the file
     * @param userId id of the user sending the request
     * @return the file that has been requested
     */
    public CompletionStage<Result> getFile(Http.Request request, Long fileId, Long userId) {
        return fileService.getFileById(fileId).thenApplyAsync(file -> {
            try {

                java.io.File returnFile = new java.io.File(this.filesPath + file.getFilepath());
                return ok(returnFile).withHeaders("Content-Disposition", String.format("attachment; filename=%s", file.getName()));

            } catch (Exception e) {
                System.out.println(e);
                throw new BadRequestException("Error fetching file");
            }

        }).handle(AsyncHandler::handleResult);
    }
}
