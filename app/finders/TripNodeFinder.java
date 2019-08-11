package finders;

import io.ebean.Finder;
import models.TripNode;

import java.util.Optional;

public class TripNodeFinder extends Finder<Long, TripNode> {

    public TripNodeFinder() {
        super(TripNode.class);
    }

    public Optional<TripNode> findByIdIncludeDeleted(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOneOrEmpty();
    }


}
