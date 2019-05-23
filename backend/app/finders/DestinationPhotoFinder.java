package finders;

import io.ebean.Finder;
import models.DestinationPhoto;

import java.util.List;

public class DestinationPhotoFinder extends Finder<Long, DestinationPhoto> {


    public DestinationPhotoFinder() {
        super(DestinationPhoto.class);
    }

    /**
     * returns destination photo from database if given id matches that given, returns null otherwise
     * @param id
     * @return
     */
    public DestinationPhoto findByPhotoId(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }


    /**
     * Returns a list of destination photos for the destination id provided
     * @param destID
     * @return List of destinations photos
     */
    public List<DestinationPhoto> getAllPhotosForDestination(Long destID) {
        return query().where().eq("destination.id", destID).findList();
    }

}
