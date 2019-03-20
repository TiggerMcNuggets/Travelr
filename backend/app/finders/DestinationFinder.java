package finders;

import io.ebean.Expr;
import io.ebean.Finder;
import models.Destination;
import models.User;

import java.util.List;
import java.util.Optional;

public class DestinationFinder extends Finder<Long, Destination> {

    /**
     * Construct using the default EbeanServer.
     */
    public DestinationFinder() {
        super(Destination.class);
    }


//    public List<Destination> getPublicDestinations() {
//        return query().where().eq("isPublic", true).findList();
//    }

    public List<Destination> getUserDestinations(long userId) {
        return query().where().eq("user.id", userId).findList();
    }

    public List<Destination> getAvaliableDestinations(long userId) {
        return query().where().or(Expr.eq("isPublic", true), Expr.eq("user.id", userId)).findList();
    }

}