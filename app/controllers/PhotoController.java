package controllers;

import com.typesafe.config.Config;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Photo.ChooseProfilePicReq;
import controllers.dto.Photo.UpdatePhotoReq;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.PersonalPhotoRepository;
import utils.FileHelper;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PhotoController extends Controller {

    private final PersonalPhotoRepository personalPhotoRepository;

    private final FileHelper fh = new FileHelper();

    private String personalPhotosFilepath;

    private String profilePhotosFilepath;

    private String mediaFilepath;

    @Inject
    FormFactory formFactory;

    @Inject
    public PhotoController(Config config, PersonalPhotoRepository personalPhotoRepository) {
        String rootPath = System.getProperty("user.home");
        personalPhotosFilepath = rootPath + config.getString("personalPhotosFilePath");
        profilePhotosFilepath = rootPath + config.getString("profilePhotosFilePath");
        mediaFilepath = rootPath + config.getString("mediaFilePath");
        this.personalPhotoRepository = personalPhotoRepository;
    }


    /**
     * Allows the user to fetch rows from the personal photo repository, given a user id
     * @param request the http request
     * @param id user id
     * @return 200 response and nationalities JSON body if successful
     *         400 response if user is invalid
     *         401 response if access is denied
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> list(Http.Request request, Long id) {
 
        User user = request.attrs().get(Attrs.USER);
        Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        return personalPhotoRepository.list(id, user.id == id || isAdmin).thenApplyAsync(photos -> {
            PathProperties pathProperties = PathProperties.parse("id, photo_filename, is_public");
            return ok(Ebean.json().toJson(photos, pathProperties));
        });
    }

    /**
     * Uploads a personal photo to the server file system.
     * @param request The request containing the image data to upload.
     * @param id The id of the traveller/user uploading the image.
     * @return A result whether the image upload was successful or not.
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> uploadPersonalPhoto(Http.Request request, Long id) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture != null) {
            if (!fh.isValidFile(picture.getFilename())) {
                return CompletableFuture.completedFuture(badRequest("Incorrect File Type"));
            }
            String fileName = fh.getHashedImage(picture.getFilename());
            Files.TemporaryFile file = picture.getRef();
            fh.makeDirectory(this.personalPhotosFilepath);
            file.copyTo(Paths.get(this.personalPhotosFilepath + fileName), true);
            return personalPhotoRepository.add(id, fileName).thenApplyAsync(photo_id -> {
                if (photo_id != null) {
                    return ok("File uploaded with Photo ID " + photo_id);
                } else if (photo_id == null) {
                    return badRequest("Duplicate Photo.");
                } else {
                    return badRequest("Error adding reference to the database.");
                }
            });
        } else {
            return  CompletableFuture.completedFuture(badRequest(APIResponses.MISSING_FILE));
        }
    }

        /**
          * Gets a raw image from the file system and sends this as response data.
          * @param filename The file name of the image to get.
         * @return The raw image file which corresponds to the filename given.
         */

    public Result getImageFromDatabase(String filename) {

        File file = new File(this.personalPhotosFilepath + filename);
        try {
            return ok(file);
        } catch (Exception e) {
            return badRequest(APIResponses.MISSING_FILE);
        }
    }


    /**
     * Updates a photo that belongs to a user
     * @param request the http request
     * @param id the id of the photo
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserPhoto(Http.Request request, Long id) {
        Form<UpdatePhotoReq> updatePhotoForm = formFactory.form(UpdatePhotoReq.class).bindFromRequest(request);

        if (updatePhotoForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Error updating photo"));
        }

        UpdatePhotoReq req = updatePhotoForm.get();

        return personalPhotoRepository.getOne(id).thenComposeAsync(photo -> {
            // Not Found Check
            if (photo == null) {
                return CompletableFuture.completedFuture(notFound("Photo not found"));
            }
            return personalPhotoRepository.update(req, id).thenApplyAsync(destId -> ok("Photo updated"));

        });
    }

    /**
     * uploads new photo as user profile pic
     * @param request http request containing an image
     * @param id id of user to change profile pic
     * @return 200 http response if successful, else 400 for bad request
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> uploadProfilePhoto(Http.Request request, Long id) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture != null) {
            if (!fh.isValidFile(picture.getFilename())) {
                return CompletableFuture.completedFuture(badRequest("Incorrect File Type"));
            }
            String fileName = fh.getHashedImage(picture.getFilename());
            Files.TemporaryFile file = picture.getRef();
            fh.makeDirectory(this.mediaFilepath);
            file.copyTo(Paths.get(this.mediaFilepath + fileName), true);

            return personalPhotoRepository.setUserProfilePic(id, fileName).thenApplyAsync(photoName -> {
                if (photoName != null) {
                    return ok("Your profile image was successfully set to " + photoName);
                } else {
                    return badRequest("Error setting profile image");
                }
            });
        } else {
            return  CompletableFuture.completedFuture(badRequest(APIResponses.MISSING_FILE));
        }
    }


    /**
     * sets existing photo to user profile pic
     * @param request http request containing the filename of the photo
     * @param id id of user to change profile pic
     * @return 200 http response if successful, else 400 for bad request
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> chooseProfilePhoto(Http.Request request, Long id) {
        Form<ChooseProfilePicReq> chooseProfilePicForm = formFactory.form(ChooseProfilePicReq.class).bindFromRequest(request);
        ChooseProfilePicReq req = chooseProfilePicForm.get();
        String fileName = req.photo_filename;

        try {
        if (fileName != null) {
            fh.makeDirectory(this.mediaFilepath);
            Path sourceDirectory = Paths.get(this.mediaFilepath + fileName);
            Path targetDirectory = Paths.get((this.mediaFilepath + fileName));
            java.nio.file.Files.copy(sourceDirectory, targetDirectory);
        }
        } catch (IOException e) {
            System.err.println("Profile image already exists in directory");
        }

        return personalPhotoRepository.setUserProfilePic(id, fileName).thenApplyAsync(photoName -> {
            if (photoName != null) {
                return ok("Your profile image was successfully set to " + photoName);
            } else {
                return badRequest("Error setting profile image");
            }
        });
    }


    /**
     * retrieves user profile pic using the user's id
     * @param id user id
     * @return 200 http response code if successful, else 404
     */
    public CompletionStage<Result> getProfilePic(long id) {
        return personalPhotoRepository.getUserProfilePic(id).thenApplyAsync(fileName -> {
            try {
                File file = new File(this.mediaFilepath + fileName);
                return ok(file);
            } catch (Exception e) {
                System.err.println(e);
                return notFound("File not found");
            }
        });
    }
}
