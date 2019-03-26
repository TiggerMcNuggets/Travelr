package controllers;

//import controllers.actions.Attrs;
import controllers.actions.Authorization;
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

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PhotoController extends Controller {

    private final PersonalPhotoRepository personalPhotoRepository;

    @Inject
    FormFactory formFactory;

    @Inject
    public PhotoController(
            PersonalPhotoRepository personalPhotoRepository
    ) {
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
        FileHelper fh = new FileHelper();
        fh.make_directory("resources/images");

        return personalPhotoRepository.list(id).thenApplyAsync((photos) -> {
            PathProperties pathProperties = PathProperties.parse("id,photo_filename,is_public");
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
            String fileName = picture.getFilename();
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();
            Files.TemporaryFile file = picture.getRef();
            FileHelper fh = new FileHelper();
            fh.make_directory("resources/images");
            file.copyTo(Paths.get("resources/images/"+ fileName), true);
            return personalPhotoRepository.add(id, fileName).thenApplyAsync((photo_id) -> {
                if (photo_id != null) {
                    return ok("File uploaded with Photo ID " + photo_id);
                } else {
                    return badRequest("Error adding reference to the database.");
                }
            });
        } else {
            return  CompletableFuture.completedFuture(badRequest("Missing file"));
        }
    }

    /**
      * Gets a raw image from the file system and sends this as response data.
      * @param filename The file name of the image to get.
     * @return The raw image file which corresponds to the filename given.
     */
    @Authorization.RequireAuth
    public Result getImageFromDatabase(String filename) {
        File file = new File("resources/images/" + filename);
        try {
            return ok(file);
        } catch (Exception e) {
            System.out.println(e);
            return badRequest("Missing file");
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
//        User user = request.attrs().get(Attrs.USER);

        if (updatePhotoForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        UpdatePhotoReq req = updatePhotoForm.get();

        return personalPhotoRepository.getOne(id).thenComposeAsync(photo -> {
            // Not Found Check
            if (photo == null) {
                return CompletableFuture.completedFuture(notFound("Photo not found"));
            }
            // Forbidden Check
//            if (photo.traveller.id != user.id) {
//                return CompletableFuture.completedFuture(forbidden("Forbidden: Access Denied"));
//            }
            return personalPhotoRepository.update(req, id).thenApplyAsync(destId -> ok("Photo updated"));

        });
    }

    @Authorization.RequireAuth
    public CompletionStage<Result> uploadProfilePhoto(Http.Request request, Long id) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();
            Files.TemporaryFile file = picture.getRef();
            FileHelper fh = new FileHelper();
            fh.make_directory("resources/images");
            file.copyTo(Paths.get("resources/images/"+ fileName), true);
            return personalPhotoRepository.setUserProfilePic(id, fileName).thenApplyAsync((photoName) -> {
                if (photoName != null) {
                    return ok("Your profile image was successfully set to " + photoName);
                } else {
                    return badRequest("Error setting profile image");
                }
            });
        } else {
            return CompletableFuture.completedFuture(badRequest("Missing file"));
        }
    }


    @Authorization.RequireAuth
    public CompletionStage<Result> chooseProfilePhoto(Http.Request request, Long id) {
        Form<ChooseProfilePicReq> chooseProfilePicForm = formFactory.form(ChooseProfilePicReq.class).bindFromRequest(request);
        ChooseProfilePicReq req = chooseProfilePicForm.get();
        String fileName = req.fileName;
        return personalPhotoRepository.setUserProfilePic(id, fileName).thenApplyAsync((photoName) -> {
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
        return personalPhotoRepository.getUserProfilePic(id).thenApplyAsync((fileName) -> {
            try {
                File file = new File("resources/images/" + fileName);
                return ok(file);
            } catch (Exception e) {
                System.out.println(e);
                return notFound("File not found");
            }
        });
    }
}
