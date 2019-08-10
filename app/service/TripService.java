package service;

import dto.trip.CreateTripDTO;
import dto.trip.GetTripDTO;
import dto.trip.NodeDTO;
import javassist.NotFoundException;
import models.*;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A service which holds the business logic relating to Trips
 */
public class TripService {

    private DatabaseExecutionContext context;

    @Inject
    public TripService(DatabaseExecutionContext context) { this.context = context; }

    /**
     * Creates a TripNode from a CreateTripDTO and sets the owner of the trip to user
     * @param tripDTO
     * @param user
     * @return
     */
    public CompletableFuture<Long> createTrip(CreateTripDTO tripDTO, User user) {
        return supplyAsync(() -> {

            TripNode trip = new TripNode(tripDTO.name, user);

            trip.insert();

            return trip.getId();
        }, context);
    }

    /**
     * Get a trip by Id
     * @param tripId
     * @return
     */
    public CompletableFuture<Optional<TripNode>> getTripById(Long tripId) {
        return supplyAsync(() -> {

            Optional<TripNode> trip = Optional.ofNullable(TripNode.find.byId(tripId));

            return trip;

        }, context);
    }

    /**
     * Gets the children for a given trip Id
     * Ordered by ordinal to remove as much logic as possible from frontend
     * @param tripId
     * @return
     */
    public CompletableFuture<List<Node>> getChildrenByTripId(Long tripId) {
        return supplyAsync(() -> {

            List<Node> tNodes = Node.find.query().where().eq("parent.id", tripId).orderBy().asc("ordinal").findList();

            return tNodes;

        }, context);

    }

    /**
     * Creates a list that can be used for breadcrumbs on the frontend
     * @param tripId of the currently viewed trip
     * @return a list of TripNodes in order from top of the tree to the bottom
     */
    public CompletableFuture<List<TripNode>> getNavigationForTrip(Long tripId) {
        return supplyAsync(() -> {

            TripNode trip = TripNode.find.byId(tripId);

            if(trip == null) {
                return null;
            }

            ArrayList<TripNode> navigation = new ArrayList<>();

            navigation.add(trip);

            TripNode parentNode = trip.getParent();

            while(parentNode != null) {
                navigation.add(0, parentNode);
                parentNode = parentNode.getParent();
            }

            return navigation;
        }, context);
    }

    /**
     * Get all trips for a user
     * @param userId
     * @return
     */
    public CompletableFuture<List<TripNode>> getTripsForUser(Long userId) {
        return supplyAsync(() -> {
            return TripNode.find.query().where().and().eq("user.id", userId).eq("parent", null).findList();
        }, context);
    }

    public CompletableFuture<TripNode> updateTrip(Long tripId, GetTripDTO tripDTO, User user) {
        return supplyAsync(() -> {
            CompletionStage<Optional<TripNode>> tripStage = getTripById(tripId);

            CompletionStage<List<Node>> childrenStage = getChildrenByTripId(tripId);

            CompletionStage<TripNode> newTrip = tripStage.thenCombineAsync(childrenStage, (tripNodeOptional, children) -> {
                if (!tripNodeOptional.isPresent()) {
                    throw new exceptions.NotFoundException("Trip not found");
                }

                TripNode trip = tripNodeOptional.get();

                TripNode.db().beginTransaction();

                trip.setName(tripDTO.name);

                ArrayList<Long> newNodeIds = new ArrayList<>();

                for (NodeDTO node : tripDTO.getNodes()) {
                    if (node.id == null) {
                        if (node.type.equals("trip")) {
                            TripNode newNode = new TripNode(node.name, user);
                            newNode.insert();
                            node.id = newNode.getId();

                        } else {

                            Optional<Destination> destination = Optional.ofNullable(Destination.find.byId(node.destination.id));

                            if (!destination.isPresent()) {
                                throw new exceptions.NotFoundException("Destination not found");
                            }

                            DestinationNode newNode = new DestinationNode(node.name, user, destination.get());
                            newNode.insert();
                            node.id = newNode.getId();
                        }

                    }
                    newNodeIds.add(node.id);
                }

//                for(Node oldNode : children)
            return trip;
            });

            return newTrip.thenA;
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
            Optional<TripNode> tripNodeOptional = TripNode.find.findByIdIncludeDeleted(id);

            if(tripNodeOptional.isPresent()) {
                TripNode tripNode = tripNodeOptional.get();
                tripNode.setDeleted(!tripNode.isDeleted());
                tripNode.update();
                return tripNode.isDeleted();
            }

            return false;

        }, context);
    }
}
