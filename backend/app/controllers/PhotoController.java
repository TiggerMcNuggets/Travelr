package controllers;

import com.sun.xml.bind.v2.schemagen.xmlschema.LocalAttribute;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.dto.Photo.UpdatePhotoReq;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.Files;
import play.libs.concurrent.HttpExecutionContext;
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
        User user = request.attrs().get(Attrs.USER);

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
}
