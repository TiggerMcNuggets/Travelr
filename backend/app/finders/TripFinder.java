package finders;

import io.ebean.Finder;
import models.Trip;


import java.util.List;


public class TripFinder extends Finder<Long, Trip> {

    public TripFinder(){
        super(Trip.class);
    }

    /**
     * returns the trip id and trip name of all trips created by a user
     * @param userId user id of the user who's trips we want to retrieve
     * @return
     */
    public List<Trip> findAll(Long userId) {
        return query().select("id, name").where().eq("user.id", userId).findList();
    }

    /**
     * retrieves a trip from the databse given the trip id matches the id given
     * @param id
     * @return
     */
    public Trip findOne(Long id) {
        return query().fetch("destinations.destination").where().eq("id", id).findOneOrEmpty().orElse(null);
    }


}
