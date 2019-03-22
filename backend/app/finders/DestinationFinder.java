package finders;

import io.ebean.Expr;
import io.ebean.Finder;
import models.Destination;
import models.User;

import java.util.List;
import java.util.Optional;

public class DestinationFinder extends Finder<Long, Destination> {


    public DestinationFinder() {
        super(Destination.class);
    }

    public List<Destination> getUserDestinations(Long userId) {
        return query().where().eq("user.id", userId).findList();
    }

    public Destination findById(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    public List<Destination> getAvaliableDestinations(Long userId) {
        return query().where().or(Expr.eq("isPublic", true), Expr.eq("user.id", userId)).findList();
    }

}