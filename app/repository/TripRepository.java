//package repository;
//
//import com.google.inject.Inject;
//import controllers.dto.trip.CreateTripReq;
//import controllers.dto.trip.TripDestinationReq;
//import finders.DestinationFinder;
//import finders.TripFinder;
//import models.Destination;
//import models.Trip;
//import models.TripDestination;
//import models.User;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static java.util.concurrent.CompletableFuture.supplyAsync;
//
//public class TripRepository {
//
//    private DatabaseExecutionContext context;
//    private TripFinder tripFinder = new TripFinder();
//    private DestinationFinder destinationFinder = new DestinationFinder();
//
//    @Inject
//    public TripRepository(DatabaseExecutionContext context) {
//        this.context = context;
//    }
//
//    /**
//     * Inserts the destinations for a trip from a list of destinations DTO
//     * @param trip the trip object
//     * @param destinations the list of destinations
//     */
//    private void insertDestinations(Trip trip, List<TripDestinationReq> destinations) {
//        for (TripDestinationReq destinationReq : destinations) {
//            Destination destination = destinationFinder.findById(destinationReq.id);
//
//            // TRANSFER DESTINATION TO ADMIN IF PUBLIC
//
//            if(destination.isPublic && destination.user != null) {
//                Destination.find.transferToAdmin(destination.id);
//            }
//
//            TripDestination tripDestination = new TripDestination(destinationReq.arrivalDate, destinationReq.departureDate, destinationReq.ordinal, trip, destination);
//            tripDestination.insert();
//        }
//    }
//
//    /**
//     * Gets all trips for a given user
//     * @param userId the user id
//     * @return completable future of list of trips
//     */
//
//    public CompletableFuture<List<Trip>> getTrips(Long userId) {
//        return supplyAsync(() -> tripFinder.findAll(userId), context);
//    }
//
//
//    /**
//     * Creates a trip for a user from user DTO
//     * @param request the request DTO
//     * @param user the user object
//     * @return completable future of the new trip id
//     */
//    public CompletableFuture<Long> createTrip(CreateTripReq request, User user) {
//        return supplyAsync(() -> {
//            Trip trip = new Trip(request.name, user);
//            trip.insert();
//
//            this.insertDestinations(trip, request.destinations);
//            trip.save();
//
//            return trip.id;
//        }, context);
//    }
//
//    /**
//     * Gets a trip for a user by trip id
//     * @param id the trip id
//     * @return completable future of trip
//     */
//    public CompletableFuture<Trip> getTrip(Long id) {
//        return supplyAsync(() -> tripFinder.findOne(id), context);
//    }
//
//    /**
//     * Updates trip details for a given user
//     * @param request the request DTO
//     * @param trip the trip object
//     * @return completable future of the trip id
//     */
//
//    public CompletableFuture<Long> updateTrip(CreateTripReq request, Trip trip) {
//        return supplyAsync(() -> {
//            this.insertDestinations(trip, request.destinations);
//            trip.save();
//
//            return trip.id;
//        }, context);
//    }
//
//    /**
//     *
//     * Deletes a trip permanently from the database.
//     * @param tripId the id of the trip to delete
//     */
//    public void deleteTrip(Long tripId) {
//        Trip trip = tripFinder.findOne(tripId);
//        trip.delete();
//        trip.update();
//    }
//
//    /**
//     * Changes the user's trip deleted value to the opposite of its current value
//     * @param id the users id
//     * @return the users new deleted value
//     */
//    public CompletableFuture<Boolean> toggleTripDeleted(Long id) {
//        return supplyAsync(() -> {
//            Trip dest = Trip.find.findByIdIncludeDeleted(id);
//            dest.deleted = !dest.deleted;
//            dest.update();
//            return dest.deleted;
//        }, context);
//    }
//
//
//
//}
