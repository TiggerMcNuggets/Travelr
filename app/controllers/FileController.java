package controllers;

import com.typesafe.config.Config;
import controllers.actions.Authorization;
import dto.file.FileDTO;
import exceptions.BadRequestException;
import models.File;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.FileService;
import utils.AsyncHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class FileController extends Controller {

    private FileService fileService;
    private String filesPath;

    @Inject
    public FileController(Config config, FileService fileService) {

        this.fileService = fileService;
        String rootPath = System.getProperty("user.home");
        filesPath = rootPath + config.getString("filesPath");
    }

    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> uploadFiles(Http.Request request, Long nodeId, Long userId) {

        Http.MultipartFormData<Files.TemporaryFile> mfd = request.body().asMultipartFormData();

        List<Http.MultipartFormData.FilePart<Files.TemporaryFile>> fileList;

        fileList = mfd.getFiles();

        return fileService.createNewFiles(fileList, nodeId, userId).thenApplyAsync(files -> {
            return ok();
        });
    }

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

    public CompletionStage<Result> deleteFileById(Http.Request request, Long nodeId, Long fileId, Long userId) {
        return fileService.deleteFileById(fileId)
                .thenApplyAsync(output -> {
                    return ok();
                }).handle(AsyncHandler::handleResult);
    }


    public CompletionStage<Result> getFile(Http.Request request, Long fileId, Long userId) {
        return fileService.getFileById(fileId).thenApplyAsync(file -> {
            try {

                java.io.File returnFile = new java.io.File(this.filesPath + file.getFilepath());
                System.out.println(file.getName());
                return ok(returnFile).withHeaders("Content-Disposition", String.format("attachment; filename=%s", file.getName()));

            } catch (Exception e) {
                System.out.println(e);
                throw new BadRequestException("Error fetching file");
            }

        }).handle(AsyncHandler::handleResult);
    }


}
