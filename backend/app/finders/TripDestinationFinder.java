package finders;

import io.ebean.Expr;
import io.ebean.Finder;
import models.Destination;
import models.TripDestination;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripDestinationFinder extends Finder<Long, TripDestination> {


    /**
     * Constructor
     */
    public TripDestinationFinder() {
        super(TripDestination.class);
    }

    /**
     * Gets all trip destinations where the destination matches the given destination id
     * @param destinationId
     * @return
     */
    public List<TripDestination> getAllByDestinationId(Long destinationId) {
        return query().where().eq("destination.id", destinationId).findList();
    }

}