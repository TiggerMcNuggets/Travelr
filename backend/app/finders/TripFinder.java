package finders;

import io.ebean.Finder;
import models.Trip;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class TripFinder extends Finder<Long, Trip> {

    public TripFinder(){
        super(Trip.class);
    }


}
