package service;

import dto.trip.CreateTripDTO;
import dto.trip.TripDTO;
import dto.trip.TripDestinationDTO;
import models.Trip;
import models.TripDestination;
import models.User;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A service which holds the business logic relating to Trips
 */
public class TripService {

    private DatabaseExecutionContext context;

    @Inject
    public TripService(DatabaseExecutionContext context) { this.context = context; }

    public CompletableFuture<Trip> updateTrip(TripDTO tripDTO, User user) {
        return supplyAsync(() -> {
            Trip trip = new Trip(tripDTO.name, tripDTO.description, user);

            List<TripDestination> tripDestinationList = new ArrayList<>();

            trip.setDestinations(tripDestinationList);

            return trip;
        }, context);
    }

    /**
     * Creates a trip from a CreateTripDTO and sets the owner of the trip to user
     * @param tripDTO
     * @param user
     * @return
     */
    public CompletableFuture<Long> createTrip(CreateTripDTO tripDTO, User user) {
        return supplyAsync(() -> {

            Trip trip = new Trip(tripDTO.name, tripDTO.description, user);

            trip.save();

            return trip.id;
        }, context);
    }

    /**
     * Get a trip by Id
     * @param tripId
     * @return
     */
    public CompletableFuture<Trip> getTripById(Long tripId) {
        return supplyAsync(() -> {

            return Trip.find.byId(tripId);

        }, context);
    }

    /**
     * Get all trips for a user
     * @param userId
     * @return
     */
    public CompletableFuture<List<Trip>> getTripsForUser(Long userId) {
        return supplyAsync(() -> {
            return Trip.find.query().where().eq("user.id", userId).findList();
        }, context);
    }
}
