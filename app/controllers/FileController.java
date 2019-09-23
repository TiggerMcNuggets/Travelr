package controllers;

import controllers.actions.Authorization;
import dto.file.FileDTO;
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

    @Inject
    public FileController(FileService fileService) {
        this.fileService = fileService;
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


}
