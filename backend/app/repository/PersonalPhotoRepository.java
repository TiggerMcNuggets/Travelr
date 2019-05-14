package repository;

import controllers.dto.Photo.UpdatePhotoReq;
import io.ebean.*;
import models.PersonalPhoto;
import models.User;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PersonalPhotoRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public PersonalPhotoRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    /**
     * Adds a new personal photo for a user.
     * @param id user id
     */
    public CompletionStage<Long> add(Long id, String imageFileName) {
        return supplyAsync(() -> {
            User traveller = User.find.findById(id);
            if (traveller == null) return null; // bad user
            ExpressionList<PersonalPhoto> query = PersonalPhoto.find.query().where().eq("user_id", id);
            List<PersonalPhoto> photoList = query.findList();

            for (PersonalPhoto personalPhoto: photoList) {
                if (personalPhoto.getPhoto_filename().equals(imageFileName)) {
                    System.err.println("Duplicate Photo");
                    return null;
                }
            }
            PersonalPhoto photo = new PersonalPhoto(traveller, imageFileName);
            photo.save();
            return photo.id;
        }, executionContext);
    }

    /**
     * Returns all photos assoociated with a user.
     * @param id user id The user id
     * @param privatePhotos  if true will not return the public photos that are available to the user
     * @return CompletionStage<List<PersonalPhoto>> The list of personal photos associated with the user.
     */
    public CompletionStage<List<PersonalPhoto>> list(Long id, Boolean privatePhotos) {
        return supplyAsync(() -> {
            ExpressionList<PersonalPhoto> query = PersonalPhoto.find.query().where().eq("user_id", id).or(Expr.eq("is_public", true), Expr.eq("is_public", !privatePhotos));
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
            PersonalPhoto photo = PersonalPhoto.find.findByPhotoId(photoId);
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
        return supplyAsync(() -> PersonalPhoto.find.findByPhotoId(id), executionContext);
    }

    /**
     * Finds the User given their id and sets their profile pic filename
     * @param id the user ig
     * @param fileName the name of the photo
     * @return the name of the photo
     */
    public CompletableFuture<Object> setUserProfilePic(Long id, String fileName) {
        return supplyAsync(() -> {
            try {
                User user = User.find.findById(id);
                user.setUserProfilePhoto(fileName);
                user.save();
                return user.userProfilePhoto;
            } catch (Error e) {
                return null;
            }
        });
    }

    /**
     * retrieves profile pic for user
     * @param id user id
     * @return file name of profile pic, otherwise null
     */
    public CompletionStage<String> getUserProfilePic(long id) {
        return supplyAsync(() -> {
            try {
                User user = User.find.findById(id);
                String filename = user.userProfilePhoto;
                return filename;
            } catch (Error e) {
                return null;
            }
        });
    }
}


