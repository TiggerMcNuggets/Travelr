package finders;

import io.ebean.Finder;
import models.Trip;


import java.util.List;


public class TripFinder extends Finder<Long, Trip> {

    public TripFinder(){
        super(Trip.class);
    }

    /**
     * Retrieves a trip from database based on id and includes the soft deleted trips.
     * @param id the id of the trip
     * @return the found trip, otherwise null
     */
    public Trip findByIdIncludeDeleted(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOneOrEmpty().orElse(null);
    }
}
