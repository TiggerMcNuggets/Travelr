package finders;

import io.ebean.Finder;
import models.DestinationPhoto;

public class DestinationPhotoFinder extends Finder<Long, DestinationPhoto> {


    public DestinationPhotoFinder() {
        super(DestinationPhoto.class);
    }

    public DestinationPhoto findByPhotoId(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }


}
