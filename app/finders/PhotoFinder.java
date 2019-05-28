package finders;

import io.ebean.Finder;
import models.PersonalPhoto;

public class PhotoFinder extends Finder<Long, PersonalPhoto> {


    public PhotoFinder() {
        super(PersonalPhoto.class);
    }

    /**
     * returns personal photo from database if given id matches that given, returns null otherwise
     * @param id
     * @return
     */
    public PersonalPhoto findByPhotoId(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

}