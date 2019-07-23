package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.User;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.AlbumRepository;
import repository.MediaRepository;
import repository.PersonalPhotoRepository;
import utils.FileHelper;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MediaController extends Controller {

    private final MediaRepository mediaRepository;

    private final AlbumRepository albumRepository;

    private final FileHelper fh = new FileHelper();

    private String MEDIA_FILEPATH;

    @Inject
    public MediaController(Config config, PersonalPhotoRepository personalPhotoRepository, MediaRepository mediaRepository, AlbumRepository albumRepository) {
        String rootPath = System.getProperty("user.home");
        MEDIA_FILEPATH = rootPath + config.getString("personalPhotosFilePath");
        this.mediaRepository = mediaRepository;
        this.albumRepository = albumRepository;
    }

    /**
     * retrieves all media items in an album
     *
     * @param request  the http request
     * @param user_id  the id of the user who wants to see the album content
     * @param album_id id of the album
     * @return json representation of accessible content in the album
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getAlbumContent(Http.Request request, Long user_id, Long album_id) {

        User user = request.attrs().get(Attrs.USER);
        Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        return albumRepository.list(album_id, user.id == user_id || isAdmin).thenApplyAsync(media -> {
            PathProperties pathProperties = PathProperties.parse("id, uriString, is_public, mediaType");
            return ok(Ebean.json().toJson(media, pathProperties));
        });
    }

    /**
     * Uploads a media item to the server file system.
     *
     * @param request  The request containing the image data to upload
     * @param user_id  The id of the traveller/user uploading the image
     * @param album_id The id of the album to add the media to.
     * @return A result whether the image upload was successful or not.
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> uploadMedia(Http.Request request, Long user_id, Long album_id) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        if (picture != null) {
            if (!fh.isValidFile(picture.getFilename())) {
                return CompletableFuture.completedFuture(badRequest("Incorrect File Type"));
            }
            String fileName = fh.getHashedImage(picture.getFilename());
            Files.TemporaryFile file = picture.getRef();
            fh.makeDirectory(this.MEDIA_FILEPATH);
            file.copyTo(Paths.get(this.MEDIA_FILEPATH + fileName), true);
            return mediaRepository.add(user_id, album_id, fileName).thenApplyAsync(media_id -> {
                if (media_id != null) {
                    return ok("File uploaded with Media ID " + media_id);

                } else if (media_id == null) {
                    return badRequest("Duplicate Media.");
                } else {
                    return badRequest("Error adding reference to the database.");
                }
            });
        } else {
            return CompletableFuture.completedFuture(badRequest(APIResponses.MISSING_FILE));
        }
    }

    /**
     * creates a new album
     *
     * @param request http request
     * @param userId  the is of the user creating the album
     * @return id of the new album created
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> createAlbumGivenUser(Http.Request request, Long userId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        return albumRepository.create().thenApplyAsync(id -> {

            // If destination already exists
            if (id == null) {
                return badRequest();
            }

            controllers.dto.Media.CreateAlbumRes response = new controllers.dto.Media.CreateAlbumRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return created(jsonResponse);
        });
    }
}

