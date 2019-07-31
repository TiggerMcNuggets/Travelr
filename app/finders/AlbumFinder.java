package finders;

import io.ebean.Finder;
import models.Album;

import java.util.List;

public class AlbumFinder extends Finder<Long, Album> {

    public AlbumFinder() {
        super(Album.class);
    }

    /**
     * returns personal photo from database if given id matches that given, returns null otherwise
     * @param id
     * @return
     */
    public Album findAlbumById(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    /**
     * Returns list of all albums for a given user
     * @param userId The user's id
     * @return List of albums
     */
    public List<Album> findAllByUserId(Long userId) {
        return query().where().eq("user.id", userId).findList();
    }
}