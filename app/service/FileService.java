package service;

import models.File;
import play.libs.Files;
import play.mvc.Http;
import utils.FileHelper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class FileService {

    private FileHelper fh;

    @Inject
    public FileService(FileHelper fh) {
        this.fh = fh;
    }


    public CompletableFuture<List<File>> createNewFiles(List<Http.MultipartFormData.FilePart<Files.TemporaryFile>> files) {

        List<File> newFileList = new ArrayList<>();

        for(Http.MultipartFormData.FilePart<Files.TemporaryFile> file : files) {

            newFileList.add(createNewFile(file));

        }

        return CompletableFuture.supplyAsync(() -> {
            List<File> fileList = new ArrayList<>();

            return fileList;
        });

    }


    private File createNewFile(Http.MultipartFormData.FilePart<Files.TemporaryFile> file) {

        Files.TemporaryFile tempFile = file.getRef();

        System.out.println(file.getFilename());

        return new File();

    }
}
