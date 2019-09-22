package service;

import exceptions.NotFoundException;
import models.File;
import play.libs.Files;
import play.mvc.Http;
import repository.DatabaseExecutionContext;
import utils.FileHelper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;


public class FileService {

    private FileHelper fh;
    private DatabaseExecutionContext context;

    @Inject
    public FileService(FileHelper fh, DatabaseExecutionContext context) {
        this.fh = fh;
        this.context = context;
    }

    /**
     * Gets a list of files for a node
     * @param tripId
     * @return a list of files for the given node
     */
    public CompletableFuture<List<File>> getFilesForTripNode(Long tripId) {
        return supplyAsync(() -> File.find.getFilesForTripNode(tripId), context);
    }

    /**
     * Deletes a file by Id
     * @param fileId
     * @return
     */
    public CompletableFuture<Long> deleteFileById(Long fileId) {

        if(true) {
            throw new NotFoundException("asdsada");
        }

        return supplyAsync(() -> {
            File file = File.find.byId(fileId);

            if(file == null) {
                throw new NotFoundException("File not found");
            }

            file.delete();

            return null;
        }, context);
    }

    /**
     * Creates files for the given node
     * @param files
     * @return
     */
    public CompletableFuture<List<File>> createNewFiles(List<Http.MultipartFormData.FilePart<Files.TemporaryFile>> files) {

        List<File> newFileList = new ArrayList<>();

        for(Http.MultipartFormData.FilePart<Files.TemporaryFile> file : files) {

            newFileList.add(createNewFile(file));

        }

        return supplyAsync(() -> {
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
