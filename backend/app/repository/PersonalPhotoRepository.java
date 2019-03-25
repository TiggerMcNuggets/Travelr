package repository;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.dto.Photo.UpdatePhotoReq;
import finders.UserFinder;
import finders.PhotoFinder;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.ExpressionList;
import io.ebean.Transaction;
import models.Nationality;
import models.PersonalPhoto;
import models.User;
import play.db.ebean.EbeanConfig;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PersonalPhotoRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private PhotoFinder photoFinder = new PhotoFinder();
    private UserFinder userFinder = new UserFinder();

    @Inject
    public PersonalPhotoRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }


    /**
     * Adds a new personal photo for a user.
     * @param id user id
     */
    public CompletionStage<Long> add(Long id, String imageFileName) {
        return supplyAsync(() -> {
            User traveller = User.find.findById(id);
            System.out.println(traveller);
            if (traveller == null) return null; // bad user
            PersonalPhoto photo = new PersonalPhoto(traveller, imageFileName);
            photo.save();
            return photo.id;
        }, executionContext);
    }

    /**
     * Returns all photos assoociated with a user.
     * @param id user id The user id
     * @return CompletionStage<List<PersonalPhoto>> The list of personal photos associated with the user.
     */
    public CompletionStage<List<PersonalPhoto>> list(Long id) {
        return supplyAsync(() -> {
            ExpressionList<PersonalPhoto> query = ebeanServer.find(PersonalPhoto.class).where().eq("traveller_id", id);
            return query.findList();
        }, executionContext);
    }


    /**
     * Updates a photo that belongs to a user
     * @param request the request DTO
     * @param photoId the photo id
     * @return completable future of the new destination
     */
    public CompletableFuture<Long> update(UpdatePhotoReq request, Long photoId) {
        return supplyAsync(() -> {
            PersonalPhoto photo = photoFinder.findByPhotoId(photoId);
            photo.is_public = request.is_public;
            photo.save();

            return photo.id;
        });
    }

    /**
     * Gets one photo that belongs to a user
     * @param id the photo id
     * @return completable future of the photo
     */
    public CompletableFuture<PersonalPhoto> getOne(Long id) {
        return supplyAsync(() -> photoFinder.findByPhotoId(id), executionContext);
    }

    /**
     * Finds the User given their id and sets their profile pic filename
     * @param id the user ig
     * @param fileName the name of the photo
     * @return the name of the photo
     */
    public CompletableFuture<Object> setUserProfilePic(Long id, String fileName) {
        return supplyAsync(() -> {
            User user = userFinder.findById(id);
            user.userProfilePhoto = fileName;
            user.save();
            return user.userProfilePhoto;
        });
    }
}


