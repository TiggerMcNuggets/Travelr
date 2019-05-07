package controllers;

import com.typesafe.config.Config;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.photo.UpdatePhotoReq;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.DestinationPhotoRepository;
import utils.FileHelper;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DestinationPhotoController extends Controller {

    private final DestinationPhotoRepository destinationPhotoRepository;

    private final FileHelper fh = new FileHelper();

    private String destinationPhotoFilepath;

    @Inject
    FormFactory formFactory;

    @Inject
    public DestinationPhotoController( Config config,
            DestinationPhotoRepository destinationPhotoRepository
    ) {
        String rootPath = System.getProperty("user.home");
        destinationPhotoFilepath = rootPath + config.getString("destinationPhotosFilePath");
        this.destinationPhotoRepository = destinationPhotoRepository;
    }

    /**
     * Allows the user to fetch rows from the destination photo repository, given a destination id
     * @param request the http request
     * @param id destination id
     * @return 200 response and nationalities JSON body if successful
     *         400 response if user is invalid
     *         401 response if access is denied
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> list(Http.Request request, Long id, Long dest_id) {

        User user = request.attrs().get(Attrs.USER);
        Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);
        //what we do here with the id (compares for user id)
        return destinationPhotoRepository.list(id, user.id == id || isAdmin, destId).thenApplyAsync(photos -> {
            //is this line ok? "public"
            PathProperties pathProperties = PathProperties.parse("id,photo_filename,is_public");
            return ok(Ebean.json().toJson(photos, pathProperties));
        });
    }

    /**
     * Gets a raw image from the file system and sends this as response data.
     * @param filename The file name of the image to get.
     * @return The raw image file which corresponds to the filename given.
     */
    public Result getImageFromDatabase(String filename) {
        File file = new File(this.destinationPhotoFilepath + filename);
        try {
            return ok(file);
        } catch (Exception e) {
            return badRequest(APIResponses.MISSING_FILE);
        }
    }

    /**
     * Uploads a destination photo to the server file system.
     * @param request The request containing the image data to upload.
     * @param id The id of the traveller/user uploading the image.
     * @param destId The id of the destination uploading the image of.
     * @return A result whether the image upload was successful or not.
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> uploadDestinationPhoto(Http.Request request, Long id, Long destId) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture != null) {
            String fileName = fh.getHashedImage(picture.getFilename());
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();
            Files.TemporaryFile file = picture.getRef();
            FileHelper fh = new FileHelper();
            fh.makeDirectory(this.destinationPhotoFilepath);
            file.copyTo(Paths.get(this.destinationPhotoFilepath + fileName), true);
            return destinationPhotoRepository.add(id, dest_id, fileName).thenApplyAsync((photo_id) -> {
                if (photo_id != null) {
                    return ok("File uploaded with Photo ID " + photo_id);
                } else {
                    return badRequest("Error adding reference to the database.");
                }
            });
        } else {
            return  CompletableFuture.completedFuture(badRequest(APIResponses.MISSING_FILE));
        }
    }

    /**
     * Updates a photo that belongs to a user's destination
     * @param request the http request
     * @param id the id of the photo
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateDestinationPhoto(Http.Request request, Long id) {
        Form<UpdatePhotoReq> updatePhotoForm = formFactory.form(UpdatePhotoReq.class).bindFromRequest(request);

        if (updatePhotoForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        UpdatePhotoReq req = updatePhotoForm.get();

        return destinationPhotoRepository.getOne(id).thenComposeAsync(photo -> {
            // Not Found Check
            if (photo == null) {
                return CompletableFuture.completedFuture(notFound("Photo not found"));
            }
            return destinationPhotoRepository.update(req, id).thenApplyAsync(destId -> ok("Photo updated"));

        });
    }

}
