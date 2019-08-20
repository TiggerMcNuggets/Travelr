package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Media.*;
import finders.DestinationFinder;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.Album;
import models.Destination;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.AlbumRepository;
import repository.MediaRepository;
import repository.PersonalPhotoRepository;
import utils.FileHelper;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MediaController extends Controller {

    private final MediaRepository mediaRepository;

    private final AlbumRepository albumRepository;

    private final FileHelper fh = new FileHelper();

    private String MEDIA_FILEPATH;

    @Inject
    FormFactory formFactory;

    @Inject
    public MediaController(Config config,
            MediaRepository mediaRepository, AlbumRepository albumRepository) {
        String rootPath = System.getProperty("user.home");
        MEDIA_FILEPATH = rootPath + config.getString("mediaFilePath");
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

        return albumRepository.list(album_id, ((user.id).equals(user_id)) || isAdmin).thenApplyAsync(media -> {
            PathProperties pathProperties = PathProperties.parse("id, uriString, is_public, mediaType, caption");
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
                }
                return notFound(APIResponses.ALBUM_OR_MEDIA_NOT_FOUND);
            });
        } else {
            return CompletableFuture.completedFuture(badRequest(APIResponses.MISSING_FILE));
        }
    }

    /**
     * Uploads a media item to the server file system.
     *
     * @param request  The request containing the image data to upload
     * @param id  The id of the traveller/user uploading the image
     * @param dest_id The id of the album to add the media to.
     * @return A result whether the image upload was successful or not.
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> uploadDestinationMedia(Http.Request request, Long id, Long dest_id) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("picture");
        Destination destination = Destination.find.findById(dest_id);
        Album destinationAlbum = destination.getDefaultAlbum();
        Long album_id = destinationAlbum.getId();
        if (picture != null) {
            if (!fh.isValidFile(picture.getFilename())) {
                return CompletableFuture.completedFuture(badRequest("Incorrect File Type"));
            }
            String fileName = fh.getHashedImage(picture.getFilename());
            Files.TemporaryFile file = picture.getRef();
            fh.makeDirectory(this.MEDIA_FILEPATH);
            file.copyTo(Paths.get(this.MEDIA_FILEPATH + fileName), true);
            return mediaRepository.add(id, album_id, fileName).thenApplyAsync(media_id -> {
                if (media_id != null) {
                    return ok("File uploaded with Media ID " + media_id);
                }
                return notFound(APIResponses.ALBUM_OR_MEDIA_NOT_FOUND);
            });
        } else {
            return CompletableFuture.completedFuture(badRequest(APIResponses.MISSING_FILE));
        }
    }

    /**
     * Retrieves all albums for a user
     *
     * @param request  the http request
     * @param user_id  the id of the user who wants to see the album content
     * @return json representation of accessible content in the album
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUsersAlbums(Http.Request request, Long user_id) {

        return albumRepository.listUserAlbums(user_id).thenApplyAsync(albums -> {

            GetAlbumsRes response = new GetAlbumsRes(request, albums);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response.getGetAlbumRes());
            return ok(jsonResponse);
        });
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
        if (middlewareRes != null)
            return middlewareRes;

        Form<CreateAlbumReq> createAlbumForm = formFactory.form(CreateAlbumReq.class).bindFromRequest(request);

        if (createAlbumForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Error updating media"));
        }

        CreateAlbumReq req = createAlbumForm.get();

        return albumRepository.create(req.getName(), userId).thenApplyAsync(id -> {

            // If album already exists
            if (id == null) {
                return badRequest();
            }

            controllers.dto.Media.CreateAlbumRes response = new controllers.dto.Media.CreateAlbumRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return created(jsonResponse);
        });
    }

    /**
     * Updates a media that belongs to a user
     * 
     * @param request  the http request
     * @param media_id the id of the media item
     * @return 200 with string if all ok
     *
     *         this function may need added authorisation checks!!
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserMedia(Http.Request request, Long user_id, Long media_id) {
        Form<UpdateMediaReq> updateMediaForm = formFactory.form(UpdateMediaReq.class).bindFromRequest(request);

        if (updateMediaForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Error updating media"));
        }

        UpdateMediaReq req = updateMediaForm.get();

        return mediaRepository.getOne(media_id).thenComposeAsync(media -> {
            // Not Found Check
            if (media == null) {
                return CompletableFuture.completedFuture(notFound("Media item not found"));
            }
            return mediaRepository.update(req, media_id).thenApplyAsync(destId -> ok("Media updated"));

        });
    }

    /**
     * adds an existing media item to an existing album
     * 
     * @param request  the http request
     * @param album_id the id of the existing album
     * @param media_id the id of the existing
     * @return
     *
     *         will need security measures added
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> addMediaToAlbum(Http.Request request, Long user_id, Long album_id, Long media_id) {
        return mediaRepository.addMediaToAlbum(album_id, media_id).thenApplyAsync(updated_album_id -> {
            // not found check, repository checks that both album and media exist
            if (updated_album_id == null) {
                return notFound(APIResponses.ALBUM_OR_MEDIA_NOT_FOUND);

            }
            return ok("album updated successfully");
        });
    }

    /**
     * Deletes an existing album
     * 
     * @param request  the http request
     * @param album_id the id of the existing album
     * @return 200 if deletion successful otherwise 404 not found.
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> deleteAlbum(Http.Request request, Long user_id, Long album_id) {
        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, user_id);
        if (middlewareRes != null)
            return middlewareRes;

        if (user_id != Album.find.findAlbumById(album_id).getUser().getId()) {
            return CompletableFuture.completedFuture(unauthorized(APIResponses.FORBIDDEN_ALBUM_DELETION));
        }

        return albumRepository.remove(album_id).thenApplyAsync(deleted_album_id -> {
            // not found check, repository checks that both album and media exist
            if (deleted_album_id == null) {
                return notFound(APIResponses.ALBUM_OR_MEDIA_NOT_FOUND);

            }
            return ok(APIResponses.SUCCESSFUL_ALBUM_DELETION);
        });
    }

    /**
     * Deletes an existing album
     * 
     * @param request  the http request
     * @param album_id the id of the existing album
     * @return 200 if deletion successful otherwise 404 not found.
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> deleteSingleMedia(Http.Request request, Long user_id, Long album_id, Long media_id, Integer removeAll) {
        return mediaRepository.remove(album_id, media_id, removeAll).thenApplyAsync(deleted_media_id -> {
            //not found check, repository checks that both album and media exist
            if(deleted_media_id == null) {
                return notFound(APIResponses.ALBUM_OR_MEDIA_NOT_FOUND);
            }
            return ok(APIResponses.SUCCESSFUL_MEDIA_DELETION);
        });
    }

    /**
     * Gets a raw image from the file system and sends this as response data.
     * 
     * @param filename The file name of the image to get.
     * @return The raw image file which corresponds to the filename given.
     */
    public Result getImageFromDatabase(Http.Request request, Long user_id, String filename) {

        File file = new File(this.MEDIA_FILEPATH + filename);
        try {
            return ok(file);
        } catch (Exception e) {
            return badRequest(APIResponses.MISSING_FILE);
        }
    }

    /**
     * Updates an album name
     * @param request The request body for the album which includes name
     * @param userId The user's id
     * @param albumId The album's id
     * @return 200 if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateAlbumGivenUser(Http.Request request, Long userId, Long albumId) {
        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        Form<CreateAlbumReq> createAlbumForm = formFactory.form(CreateAlbumReq.class).bindFromRequest(request);

        if (createAlbumForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Error updating album"));
        }

        CreateAlbumReq req = createAlbumForm.get();
        boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        return albumRepository.updateAlbum(albumId, userId, isAdmin, req).thenApplyAsync(album -> {
            if (album == null) {
                return notFound(APIResponses.ALBUM_OR_MEDIA_NOT_FOUND);
            }

            else if (!isAdmin && album.getUser().getId() != userId) {
                return forbidden(APIResponses.FORBIDDEN_ALBUM_UPDATE);
            }

            else if (req.getName() != album.getName()) {
                return badRequest(APIResponses.DUPLICATE_ALBUM_NAME);
            }

            return ok(APIResponses.SUCCESSFUL_ALBUM_UPDATE);
        });
    }
}
