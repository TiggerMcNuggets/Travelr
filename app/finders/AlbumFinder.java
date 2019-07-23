package finders;

import io.ebean.Finder;
import models.Album;

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

}