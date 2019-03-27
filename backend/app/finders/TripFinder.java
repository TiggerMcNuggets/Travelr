package finders;

import io.ebean.Finder;
import models.Trip;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class TripFinder extends Finder<Long, Trip> {

    public TripFinder(){
        super(Trip.class);
    }

    public List<Trip> findAll(Long userId) {
        return query().select("id, name").where().eq("user.id", userId).findList();
    }

    public Trip findOne(Long id) {
        return query().fetch("destinations.destination").where().eq("id", id).findOneOrEmpty().orElse(null);
    }


}
