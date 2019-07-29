package finders;

import io.ebean.Finder;
import models.Media;
import models.PersonalPhoto;

public class MediaFinder extends Finder<Long, Media> {


    public MediaFinder() {
        super(Media.class);
    }

    /**
     * returns personal photo from database if given id matches that given, returns null otherwise
     * @param id
     * @return
     */
    public Media findMediaById(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

}