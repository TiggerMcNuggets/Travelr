package repository;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.dto.Photo.UpdatePhotoReq;
import finders.UserFinder;
import finders.PhotoFinder;
import io.ebean.*;
import models.DestinationPhoto;
import models.Nationality;
import models.PersonalPhoto;
import models.User;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    public CompletionStage<List<DestinationPhoto>> list(Long id, Boolean privatePhotos, Long dest_id) {
        return supplyAsync(() -> {
            ExpressionList<DestinationPhoto> query = DestinationPhoto.find.query().where().and().or().eq("traveller_id", id).eq("is_public", true).endOr().eq("destination_id", dest_id).endAnd();
            return query.findList();
        }, executionContext);
    }

    /**
     * Adds a new destination photo for a user.
     * @param id user id
     */
    public CompletionStage<Long> add(Long id, String imageFileName) {
        return supplyAsync(() -> {
            User traveller = User.find.findById(id);
            if (traveller == null) return null; // bad user
            DestinationPhoto photo = new DestinationPhoto(traveller, imageFileName);
            photo.save();
            return photo.id;
        }, executionContext);
    }
}
