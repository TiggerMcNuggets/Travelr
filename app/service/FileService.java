package service;

import com.typesafe.config.Config;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import models.File;
import models.TripNode;
import models.User;
import org.apache.commons.io.FilenameUtils;
import play.libs.Files;
import play.mvc.Http;
import repository.DatabaseExecutionContext;
import tyrex.services.UUID;
import utils.FileHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class FileService {

    private FileHelper fh;
    private DatabaseExecutionContext context;
    private String filesPath;
    private TripService tripService;

    @Inject
    public FileService(Config config, FileHelper fh, DatabaseExecutionContext context, TripService tripService) {
        this.fh = fh;
        this.context = context;
        this.tripService = tripService;
        String rootPath = System.getProperty("user.home");
        filesPath = rootPath + config.getString("filesPath");
    }

    /**
     * Gets a list of files for a node
     * @param tripId of the trip we are getting nodes for
     * @return a list of files for the given node
     */
    public CompletableFuture<List<File>> getFilesForTripNode(Long tripId) {
        return supplyAsync(() -> File.find.getFilesForTripNode(tripId), context);
    }

    /**
     * Deletes a file by Id
     * @param fileId id of the file being deleted
     * @return
     */
    public CompletableFuture<Long> deleteFileById(Long fileId, Long tripId, Long userId) {

        return supplyAsync(() -> {
            File file = File.find.byId(fileId);
            TripNode tripNode = TripNode.find.byId(tripId);
            User user = User.find.findById(userId);
            if (tripNode == null) {
                throw new NotFoundException("Trip not found");
            }
            if (user == null) {
                throw new NotFoundException("Trip not found");
            }
            if(file == null) {
                throw new NotFoundException("File not found");
            }
            tripService.isPermittedToWrite(tripNode, user);
            file.delete();
            return null;
        }, context);
    }

    /**
     * Creates files for the given node
     * @param files multipart form data of the files being uploaded
     * @return a list of file objects
     */
    public CompletableFuture<List<File>> createNewFiles(List<Http.MultipartFormData.FilePart<Files.TemporaryFile>> files, Long nodeId, Long userId) {
        return supplyAsync(() -> {

            User user = User.find.findById(userId);

            if(user == null) {
                throw new NotFoundException("User not found");
            }

            TripNode node = TripNode.find.byId(nodeId);

            if(node == null) {
                throw new NotFoundException("Trip not found");
            }

            this.tripService.checkReadPermissionHandler(node, user);

            List<File> newFileList = new ArrayList<>();

            for(Http.MultipartFormData.FilePart<Files.TemporaryFile> file : files) {
                File newFile = createNewFile(file);

                newFile.setUser(user);
                newFile.setTrip(node);

                newFile.insert();

                newFileList.add(newFile);
            }

            return newFileList;
        }, context);
    }

    /**
     * Fetchs a file object by its Id
     * @param fileId the id of the file we are looking for
     * @return the file found by the Id
     */
    public CompletableFuture<File> getFileById(Long fileId) {
        return supplyAsync(() -> {
            File file = File.find.byId(fileId);

            if(file == null) {
                throw new NotFoundException("File not found");
            }

           return file;

        }, context);
    }

    /**
     * Takes a temporary file and saves it to the server with a hashed name
     * @param file a multipartform data file
     * @return a file object ready to insert
     */
    private File createNewFile(Http.MultipartFormData.FilePart<Files.TemporaryFile> file) {

        Files.TemporaryFile newFile = file.getRef();
        fh.makeDirectory(this.filesPath);
        String newFileName = UUID.create();
        newFile.copyTo(Paths.get(this.filesPath + newFileName), true);

        File newFileObject = new File();

        newFileObject.setName(file.getFilename());
        newFileObject.setExtension(getFileExtension(file.getFilename()));
        newFileObject.setFilepath(newFileName);

        return newFileObject;
    }

    /**
     * Get the extension from a file name
     * @param name
     * @return
     */
    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

}
