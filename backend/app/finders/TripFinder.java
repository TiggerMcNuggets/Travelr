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

    public List<Trip> findAll(Long id) {
        return query().select("id, name").where().eq("user.id", id).findList();
    }


}
