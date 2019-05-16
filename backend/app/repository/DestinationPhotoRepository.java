package repository;

import controllers.dto.Photo.UpdatePhotoReq;
import io.ebean.*;
import models.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class DestinationPhotoRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public DestinationPhotoRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    /**
     * Returns all photos assoociated with a destination.
     * @param id destination id The destination id
     * @return CompletionStage<List<DestinationPhoto>> The list of personal photos associated with the user.
     */
    public CompletionStage<List<DestinationPhoto>>  list(Long id, Boolean privatePhotos, Long dest_id) {
        return supplyAsync(() -> {
            ExpressionList<DestinationPhoto> query = DestinationPhoto.find.query().where().and().or().eq("user_id", id).eq("is_public", true).endOr().eq("destination_id", dest_id).endAnd();
            return query.findList();
        }, executionContext);
    }

    /**
     * Adds a new destination photo for a user.
     * @param id user id
     */
    public CompletionStage<Long> add(Long id, Long dest_id, String imageFileName) {
        return supplyAsync(() -> {
            User traveller = User.find.findById(id);
            Destination destination = Destination.find.findById(dest_id);
            if (traveller == null) return null; // bad user
            ExpressionList<DestinationPhoto> query = DestinationPhoto.find.query().where().eq("destination_id", dest_id).and().eq("user_id", id);
            List<DestinationPhoto> photoList = query.findList();

            for (DestinationPhoto destinationPhoto: photoList) {
                if (destinationPhoto.getPhoto_filename().equals(imageFileName)) {
                    System.out.println("Duplicate Photo");
                    return null;
                }
            }

            DestinationPhoto photo = new DestinationPhoto(traveller, imageFileName, destination);
            photo.save();
            return photo.id;
        }, executionContext);
    }

    /**
     * Gets one photo that belongs to a user
     * @param id the photo id
     * @return completable future of the photo
     */
    public CompletableFuture<DestinationPhoto> getOne(Long id) {
        return supplyAsync(() -> DestinationPhoto.find.findByPhotoId(id), executionContext);
    }

    /**
     * Updates a destination photo
     * @param request the request DTO
     * @param photoId the photo id
     * @return completable future of the new photo
     */
    public CompletableFuture<Long> update(UpdatePhotoReq request, Long photoId) {
        return supplyAsync(() -> {
            DestinationPhoto photo = DestinationPhoto.find.findByPhotoId(photoId);
            photo.is_public = request.is_public;
            photo.save();

            return photo.id;
        });
    }
}
