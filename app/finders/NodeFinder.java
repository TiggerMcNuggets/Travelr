package finders;

import io.ebean.Finder;
import models.Node;
import models.PersonalPhoto;
import models.Trip;

import java.util.Optional;

public class NodeFinder extends Finder<Long, Node> {

    public NodeFinder() {
        super(Node.class);
    }

    public Optional<Node> findByIdIncludeDeleted(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOneOrEmpty();
    }


}
