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

    /**
     * Inserts the destinations for a trip from a list of destinations DTO
     * @param trip the trip object
     * @param destinations the list of destinations
     */
    private void insertDestinations(Trip trip, List<TripDestinationReq> destinations) {
        for (TripDestinationReq destinationReq : destinations) {
            Destination destination = destinationFinder.findById(destinationReq.id);

            // TRANSFER DESTINATION TO ADMIN IF PUBLIC

            if(destination.isPublic && destination.user.id != 1) {
                Destination.find.transferToAdmin(destination.id);
            }

            TripDestination tripDestination = new TripDestination(destinationReq.arrivalDate, destinationReq.departureDate, destinationReq.ordinal, trip, destination);
            tripDestination.insert();
        }
    }

    /**
     * Gets all trips for a given user
     * @param userId the user id
     * @return completable future of list of trips
     */

    public CompletableFuture<List<Trip>> getTrips(Long userId) {
        return supplyAsync(() -> tripFinder.findAll(userId), context);
    }


    /**
     * Creates a trip for a user from user DTO
     * @param request the request DTO
     * @param user the user object
     * @return completable future of the new trip id
     */
    public CompletableFuture<Long> createTrip(CreateTripReq request, User user) {
        return supplyAsync(() -> {
            Trip trip = new Trip(request.name, user);
            trip.insert();

            this.insertDestinations(trip, request.destinations);
            trip.save();

            return trip.id;
        }, context);
    }

    /**
     * Gets a trip for a user by trip id
     * @param id the trip id
     * @return completable future of trip
     */
    public CompletableFuture<Trip> getTrip(Long id) {
        return supplyAsync(() -> tripFinder.findOne(id), context);
    }

    /**
     * Updates trip details for a given user
     * @param request the request DTO
     * @param trip the trip object
     * @return completable future of the trip id
     */

    public CompletableFuture<Long> updateTrip(CreateTripReq request, Trip trip) {
        return supplyAsync(() -> {
            this.insertDestinations(trip, request.destinations);
            trip.save();

            return trip.id;
        }, context);
    }



}
