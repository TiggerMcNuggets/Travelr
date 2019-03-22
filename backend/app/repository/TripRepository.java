package repository;

import com.google.inject.Inject;
import controllers.dto.Trip.CreateTripReq;
import controllers.dto.Trip.CreateTripRes;
import controllers.dto.Trip.TripDestinationReq;
import finders.DestinationFinder;
import finders.TripFinder;
import models.Destination;
import models.Trip;
import models.TripDestination;
import models.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TripRepository {

    private DatabaseExecutionContext context;
    private TripFinder tripFinder = new TripFinder();
    private DestinationFinder destinationFinder = new DestinationFinder();

    @Inject
    public TripRepository(DatabaseExecutionContext context) {
        this.context = context;
    }

    public CompletableFuture<List<Trip>> getTrips(Long userId) {
        return supplyAsync(() -> tripFinder.findAll(userId), context);
    }

    public CompletableFuture<Long> createTrip(CreateTripReq request, User user) {
        return supplyAsync(() -> {
            Trip trip = new Trip(request.name, user);
            trip.insert();

            // Insert destinations
            for(TripDestinationReq destinationReq: request.destinations) {
                Destination destination = destinationFinder.findById(destinationReq.id);
                TripDestination tripDestination = new TripDestination(destinationReq.arrivalDate, destinationReq.departureDate, destinationReq.ordinal, trip, destination);
                tripDestination.insert();
            }
            trip.save();

            return trip.id;
        }, context);
    }

    public CompletableFuture<Trip> getTrip(Long id) {
        return supplyAsync(() -> tripFinder.findOne(id), context);
    }



}
