package service;

import dto.trip.CreateTripDTO;
import dto.trip.TripDTO;
import dto.trip.TripDestinationDTO;
import models.Destination;
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

    public CompletableFuture<Trip> updateTrip(Long tripId, TripDTO tripDTO, User user) {
        return supplyAsync(() -> {
            Trip trip = Trip.find.byId(tripId);

            if(trip == null) {
                return null;
            }

            trip.setName(tripDTO.name);

            for(TripDestination td: trip.getDestinations()) {
                td.delete();
            }
            trip.setDestinations(new ArrayList<>());
            trip.save();


            ArrayList<TripDestination> newTripDestinations = new ArrayList<>();

            for(TripDestinationDTO destDTO : tripDTO.destinations) {
                Destination destination = Destination.find.byId(destDTO.destination.id);

                TripDestination tripDestination = new TripDestination(
                        destDTO.customName,
                        destDTO.ordinal,
                        destDTO.depth,
                        destDTO.arrivalDate,
                        destDTO.departureDate,
                        destination);

                newTripDestinations.add(tripDestination);
            }

            trip.setDestinations(newTripDestinations);

            trip.save();

            return trip;
        }, context);
    }

    /**
     * Creates a trip from a CreateTripDTO and sets the owner of the trip to user
     * @param tripDTO
     * @param user
     * @return
     */
    public CompletableFuture<Long> createTrip(TripDTO tripDTO, User user) {
        return supplyAsync(() -> {

            Trip trip = new Trip(tripDTO.name, tripDTO.description, user);

            ArrayList<TripDestination> newTripDestinations = new ArrayList<>();

            for(TripDestinationDTO destDTO : tripDTO.destinations) {
                Destination destination = Destination.find.byId(destDTO.destination.id);

                TripDestination tripDestination = new TripDestination(
                        destDTO.customName,
                        destDTO.ordinal,
                        destDTO.depth,
                        destDTO.arrivalDate,
                        destDTO.departureDate,
                        destination);

                newTripDestinations.add(tripDestination);
            }

            trip.setDestinations(newTripDestinations);

            trip.insert();

            return trip.getId();
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


    /**
     * Deletes a trip given an Id
     * @param tripId
     * @return true if successful deletion, false if not
     */
    public CompletableFuture<Boolean> deleteTrip(Long tripId) {
        return supplyAsync(() -> {
            Trip trip = Trip.find.byId(tripId);
            if(trip != null) {
                trip.delete();

                return true;
            }

            return false;
        }, context);

    }

    /**
     * Toggles the deletion of a trip given an Id
     * @param id
     * @return true if the trip is deleted
     */
    public CompletableFuture<Boolean> toggleTripDeleted(Long id) {
        return supplyAsync(() -> {
            Trip trip = Trip.find.findByIdIncludeDeleted(id);
            trip.setDeleted(!trip.isDeleted());
            trip.update();
            return trip.isDeleted();
        }, context);
    }
}
