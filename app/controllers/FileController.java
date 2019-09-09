package controllers;

import play.api.mvc.MultipartFormData;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FileController extends Controller {

    public FileController() {

    }


    public CompletionStage<Result> uploadFiles(Http.Request request) {

        Http.MultipartFormData mfd = request.body().asMultipartFormData();

        List<MultipartFormData.FilePart> fileList = mfd.getFiles();



    }


}
