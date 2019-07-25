package repository;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.Album;
import models.Media;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AlbumRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public AlbumRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    /**
     * Returns all photos assoociated with a user.
     * 
     * @param id           user id The user id
     * @param privateMedia if true will not return the public photos that are
     *                     available to the user
     * @return CompletionStage<List<PersonalPhoto>> The list of personal photos
     *         associated with the user.
     */
    public CompletionStage<List<Media>> list(Long id, Boolean privateMedia) {
        return supplyAsync(() -> {
            ExpressionList<Media> query = Media.find.query().where().eq("user_id", id).or(Expr.eq("is_public", true),
                    Expr.eq("is_public", !privateMedia));
            return query.findList();
        }, executionContext);
    }

    /**
     * Creates a new empty album
     * 
     * @return the new album id which was created.
     */
    public CompletableFuture<Long> create() {
        return supplyAsync(() -> {
            Album album = new Album();
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
            album.delete();
            return album.id;
        }, executionContext);
    }


}
