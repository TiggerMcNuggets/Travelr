package finders;

import io.ebean.Finder;
import models.DestinationNode;

import java.util.Optional;

public class DestinationNodeFinder extends Finder<Long, DestinationNode> {

    public DestinationNodeFinder() {
        super(DestinationNode.class);
    }

    public Optional<DestinationNode> findByIdIncludeDeleted(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOneOrEmpty();
    }


}
