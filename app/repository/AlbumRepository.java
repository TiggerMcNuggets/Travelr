package repository;

import controllers.dto.Media.CreateAlbumReq;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import com.fasterxml.jackson.databind.JsonNode;
import models.Album;
import models.Media;
import models.User;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.ArrayList;

public class AlbumRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public AlbumRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    /**
     * Returns all photos assoociated with a particular album.
     * 
     * @param albumId      user id The user id
     * @param privateMedia if true will not return the public photos that are
     *                     available to the user
     * @return CompletionStage<List<Media>> The list of personal photos associated
     *         with the user.
     */
    public CompletableFuture<List<Media>> list(Long albumId, Boolean privateMedia) {
        return supplyAsync(() -> {
            ExpressionList<Media> query = Media.find.query().where().eq("albums.id", albumId)
                    .or(Expr.eq("is_public", true), Expr.eq("is_public", !privateMedia));

            List<Media> mediaList = query.findList();
            return mediaList;
        }, executionContext);
    }

    /**
     * Creates a new empty album
     * 
     * @return the new album id which was created.
     */
    public CompletableFuture<Long> create(String name, Long userId) {
        return supplyAsync(() -> {
            User user = User.find.findById(userId);

            boolean duplicateName = false;
            for (Album al : Album.find.findAllByUserId(userId)) {
                if (al.getName().toLowerCase().equals(name.toLowerCase())) {
                    duplicateName = true;
                }
            }

            if (user == null || duplicateName)
                return null;

            Album album = new Album(user, name, false);
            album.save();
            return album.id;
        }, executionContext);
    }

    /**
     * Deletes an album
     *
     * @return the album id which was deleted.
     */
    public CompletableFuture<Long> remove(Long id) {
        return supplyAsync(() -> {
            Album album = Album.find.findAlbumById(id);
            if (!album.isPermanent && album != null) {
                album.delete();
                return album.id;
            }
            return null;
        }, executionContext);
    }

    /**
     * Returns all albums associated with a user.
     *
     * @param userId user id The user id
     * @return CompletionStage<List<Album>> The list of albums associated with the
     *         user.
     */
    public CompletionStage<List<Album>> listUserAlbums(Long userId) {
        return supplyAsync(() -> {
            User user = User.find.findById(userId);
            if (user == null)
                return null;
            return user.getAlbums();
        }, executionContext);
    }

    /**
     * Updates an album
     * 
     * @param userId  The user's id
     * @param albumId The album's id
     * @param isAdmin Whether the user is an admin
     * @param req     The request object
     * @return
     */
    public CompletableFuture<Album> updateAlbum(Long albumId, Long userId, boolean isAdmin, CreateAlbumReq req) {
        return supplyAsync(() -> {
            Album album = Album.find.findAlbumById(albumId);

            // Not found check
            if (album == null)
                return null;

            // Forbidden check
            boolean forbidden = !isAdmin && album.getUser().getId() != userId;

            // Bad Request check
            boolean badRequest = false;
            for (Album al : Album.find.findAllByUserId(userId)) {
                if (al.getName().toLowerCase().equals(req.getName().toLowerCase()) && !al.getId().equals(albumId)) {
                    badRequest = true;
                }
            }

            if (!forbidden && !badRequest) {
                album.setName(req.getName());
                album.update();
            }

            return album;
        }, executionContext);
    }
}
