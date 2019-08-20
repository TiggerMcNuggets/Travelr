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

    /**
     * returns a list of destinations with the same parent trip
     * @param id the id of the parent trip
     * @return the destinations that have trip as their parent
     */
    public Optional<DestinationNode> findByParentId(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOneOrEmpty();
    }


}
