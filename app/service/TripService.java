package service;

import dto.trip.TripDTO;
import dto.trip.TripDestinationDTO;
import models.Trip;
import models.User;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * A service which holds the business logic relating to Trips
 */
public class TripService {

    private DatabaseExecutionContext context;

    @Inject
    public TripService(DatabaseExecutionContext context) { this.context = context; }

    public CompletableFuture<Trip> createTrip(TripDTO tripDTO, User user) {

        Trip trip = new Trip(tripDTO.name, tripDTO.description, user);

        for(TripDestinationDTO )


    }


}
