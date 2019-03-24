package finders;

import io.ebean.Expr;
import io.ebean.Finder;
import models.Destination;
import models.PersonalPhoto;

import java.util.List;

public class PhotoFinder extends Finder<Long, PersonalPhoto> {


    public PhotoFinder() {
        super(PersonalPhoto.class);
    }

    public PersonalPhoto findByPhotoId(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

}