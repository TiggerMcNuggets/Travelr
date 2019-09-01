package repository;

import controllers.dto.Media.UpdateMediaReq;
import io.ebean.ExpressionList;
import models.Album;
import models.Media;
import models.User;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.commons.io.FileUtils;
import com.typesafe.config.Config;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class MediaRepository {

    private final DatabaseExecutionContext executionContext;
    private final String rootPath = System.getProperty("user.home");
    private String MEDIA_FILEPATH;

    @Inject
    public MediaRepository(Config config, DatabaseExecutionContext executionContext) {
        MEDIA_FILEPATH = rootPath + config.getString("mediaFilePath");
        this.executionContext = executionContext;
    }

    /**
     * Adds a new media for a user.
     * 
     * @param id user id
     */
    public CompletionStage<Long> add(Long id, Long album_id, String imageFileName) {
        return supplyAsync(() -> {
            User traveller = User.find.findById(id);
            if (traveller == null)
                return null; // bad user

            ExpressionList<Media> query = Media.find.query().where().eq("user_id", id);
            List<Media> photoList = query.findList();

            File file2 = new File(this.MEDIA_FILEPATH + imageFileName);
            Media mediaToAdd = mediaToAdd = new Media(traveller, imageFileName);
            Boolean found = false;
            for (Media media : photoList) {
                File file1 = new File(this.MEDIA_FILEPATH + media.getUriString());
                try {
                    if (FileUtils.contentEquals(file1, file2)) {
                        System.err.println("Duplicate Photo");
                        found = true;
                        mediaToAdd = media;
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Error");
                    return null;
                }
            }

            Album album = Album.find.findAlbumById(album_id);
            Album defaultAlbum = traveller.getDefaultAlbum();

            if (album == null)
                return null; // album does not exist

            if (found) {
                album.addMedia(mediaToAdd);
                album.update();
                mediaToAdd.addAlbum(album);
                mediaToAdd.update();
            } else {
                album.addMedia(mediaToAdd);
                mediaToAdd.addAlbum(album);
                if (!album.id.equals(defaultAlbum.id)) {
                    defaultAlbum.addMedia(mediaToAdd);
                    defaultAlbum.save();
                    mediaToAdd.addAlbum(defaultAlbum);
                    mediaToAdd.update();
                }
                album.save();
                mediaToAdd.save();
            }

            return mediaToAdd.id;
        }, executionContext);
    }

    /**
     * adds a media item to an album
     * 
     * @param album_id id of album to add media to
     * @param media_id id of media item
     * @return id of album media is added to
     */
    public CompletionStage<Long> addMediaToAlbum(Long album_id, Long media_id) {
        return supplyAsync(() -> {
            Album album = Album.find.findAlbumById(album_id);
            if (album == null)
                return null; // album does not exist
            Media media = Media.find.findMediaById(media_id);
            if (media == null)
                return null; // media does not exist
            album.addMedia(media);
            album.save();
            media.addAlbum(album);
            media.save();
            return album.id;
        }, executionContext);
    }

    /**
     * Updates a media that belongs to a user, changes it from private to public
     * 
     * @param request the request DTO
     * @param mediaId the media id
     * @return completable future of the new destination
     */
    public CompletableFuture<Long> update(UpdateMediaReq request, Long mediaId) {
        return supplyAsync(() -> {
            Media media = Media.find.findMediaById(mediaId);
            if (request.is_public != null)
                media.setIs_public(request.is_public);
            if (request.caption != null)
                media.setCaption(request.caption);
            media.save();

            return media.id;
        });
    }

    /**
     * Gets one media item that belongs to a user
     * 
     * @param id the media id
     * @return completable future of the photo
     */
    public CompletableFuture<Media> getOne(Long id) {
        return supplyAsync(() -> Media.find.findMediaById(id), executionContext);
    }

    /**
     * Deletes single media item from an album
     *
     * @return the media id which was deleted.
     */
    public CompletableFuture<Long> remove(Long album_id, Long media_id, Integer removeAll) {
        return supplyAsync(() -> {

            Album album = Album.find.findAlbumById(album_id);
            if (album == null)
                return null; // album does not exist

            Media media = Media.find.findMediaById(media_id);
            if (media == null)
                return null; // media does not exist

            if (removeAll == 1) {

                // Remove media from all albums
                for (Album al : media.getAlbums()) {
                    al.removeMedia(media);
                    al.update();
                }

                media.setAlbums(new ArrayList<>());
                media.update();

            } else {
                album.removeMedia(media);
                album.update();

                media.removeAlbum(album);
                media.update();
            }

            if (media.fileCanBeDeleted()) {
                File file = new File(MEDIA_FILEPATH + media.getUriString());
                if (file.delete()) {
                    System.out.println("File has been removed");
                }
                media.delete();
            }

            return media.getId();
        }, executionContext);
    }


}
