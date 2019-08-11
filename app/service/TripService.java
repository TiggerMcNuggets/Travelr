package service;

import dto.trip.CreateTripDTO;
import dto.trip.GetTripDTO;
import dto.trip.NodeDTO;
import exceptions.CustomException;
import models.*;
import play.mvc.Http;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    // TODO Adam: uncouple function from DTO??? unsure if possible or best practise at this point

    public CompletableFuture<TripNode> updateTrip(Long tripId, GetTripDTO tripDTO, User user) {
        return supplyAsync(() -> {

            CompletionStage<Optional<TripNode>> tripStage = getTripById(tripId);

            CompletionStage<List<Node>> childrenStage = getChildrenByTripId(tripId);

            return tripStage.thenCombineAsync(childrenStage, (tripNodeOptional, children) -> {

                /**
                 * Check Trip Exists
                 */
                if(!tripNodeOptional.isPresent()) {
                    throw new CustomException(Http.Status.NOT_FOUND, "Trip not found");
                }

                TripNode trip = tripNodeOptional.get();

                /**
                 * Check User can edit
                 */
                if(trip.getUser() != user) {
                    throw new CustomException(Http.Status.UNAUTHORIZED, "You do not have permission to update this trip");
                }

                trip.setName(tripDTO.name);

                /**
                 * Get Updated Ids
                 */
                ArrayList<Long> newNodeIds = new ArrayList<>();

                if(tripDTO.getNodes() == null) {
                    tripDTO.setNodes(new ArrayList<>());
                }

                for(NodeDTO node : tripDTO.getNodes()) {
                    if(node.id == null) {
                        if(node.type.equals("trip")) {
                            TripNode newNode = new TripNode(node.name, user);
                            newNode.setParent(trip);
                            newNode.save();
                            node.id = newNode.getId();

                        } else {

                            Optional<Destination> destination = Optional.ofNullable(Destination.find.byId(node.destination.id));
                            if(!destination.isPresent()) {
                                throw new CustomException(Http.Status.NOT_FOUND, "Destination not found");
                            }

                            DestinationNode newNode = new DestinationNode(node.name, user, destination.get());
                            newNode.setParent(trip);
                            newNode.save();

                            node.id = newNode.getId();
                        }

                    }
                    newNodeIds.add(node.id);
                }

                for(Node oldNode : children) {
                    if (!newNodeIds.contains(oldNode.getId())) {
                        oldNode.delete();
                    }
                }

                for (NodeDTO node : tripDTO.getNodes()) {
                    if (node.type.toLowerCase().equals("trip")) {
                        Optional<TripNode> tNodeOptional = Optional.ofNullable(TripNode.find.byId(node.id));
                        if (!tNodeOptional.isPresent()) {
                            throw new CustomException(Http.Status.NOT_FOUND, "Trip node not found");
                        }
                        TripNode tNode = tNodeOptional.get();
                        tNode.setName(node.name);
                        tNode.setOrdinal(node.ordinal);
                        tNode.update();
                    } else {
                        Optional<DestinationNode> dNodeOptional = Optional.ofNullable(DestinationNode.find.byId(node.id));
                        if (!dNodeOptional.isPresent()) {
                            throw new CustomException(Http.Status.NOT_FOUND, "Destination node not found");
                        }
                        DestinationNode dNode = dNodeOptional.get();
                        dNode.setName(node.name);
                        dNode.setOrdinal(node.ordinal);
                        dNode.setArrivalDate(node.arrivalDate);
                        dNode.setDepartureDate(node.departureDate);

                        Optional<Destination> destinationOptional = Optional.ofNullable(Destination.find.byId(dNode.getDestination().getId()));
                        if (!destinationOptional.isPresent()) {
                            throw new CustomException(Http.Status.NOT_FOUND, "Destination for destination node not found");
                        }
                        dNode.setDestination(destinationOptional.get());

                        dNode.update();
                    }
                }

                return trip;

            }).toCompletableFuture().join();
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
