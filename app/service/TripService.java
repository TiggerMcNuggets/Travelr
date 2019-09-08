package service;

import dto.trip.CreateTripDTO;
import dto.trip.GetTripDTO;
import dto.trip.NodeDTO;
import dto.trip.TripStatusDTO;
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
    public TripService(DatabaseExecutionContext context) {
        this.context = context;
    }

    /**
     * Creates a TripNode from a CreateTripDTO and sets the owner of the trip to user
     * @param tripDTO
     * @param user
     * @return
     */
    public CompletableFuture<Long> createTrip(CreateTripDTO tripDTO, User user) {
        return supplyAsync(() -> {

            TripNode trip = new TripNode(tripDTO.getName(), user);

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

    /**
     * updates a trips based on information chamnged by the user
     * @param tripId trip to be updated
     * @param tripDTO
     * @param user user who owns the trip
     * @return the updated trip
     */
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
                if(trip.getUser().getId() != user.getId()) {
                    throw new CustomException(Http.Status.UNAUTHORIZED, "You do not have permission to update this trip");
                }

                trip.setName(tripDTO.name);


                /**
                 * Get list of old deleted children
                 */
                List<Long> oldDeletedIds = Node
                        .find
                        .query()
                        .setIncludeSoftDeletes()
                        .where()
                        .eq("parent", trip)
                        .eq("deleted", true)
                        .findIds();

                /**
                 * Get Updated Ids
                 */
                List<Long> newNodeIds = new ArrayList<>();

                if(tripDTO.getNodes() == null) {
                    tripDTO.setNodes(new ArrayList<>());
                }

                for(NodeDTO node : tripDTO.getNodes()) {
                    if(node.getId() == null) {
                        if (node.getType().equals("trip")) {
                            TripNode newNode = new TripNode(node.getName(), user);
                            newNode.setParent(trip);
                            newNode.save();
                            node.setId(newNode.getId());

                        } else {

                            Optional<Destination> destination = Optional.ofNullable(Destination.find.byId(node.destination.id));
                            if (!destination.isPresent()) {
                                throw new CustomException(Http.Status.NOT_FOUND, "Destination not found");
                            }

                            DestinationNode newNode = new DestinationNode(node.getName(), user, destination.get());
                            newNode.setParent(trip);
                            newNode.save();

                            node.setId(newNode.getId());
                        }

                    }

                    else {
                        if (oldDeletedIds.contains(node.getId())) {
                            Optional<Node> optionalNode = Node.find.findByIdIncludeDeleted(node.getId());
                            if (!optionalNode.isPresent()) {
                                throw new CustomException(Http.Status.NOT_FOUND, "trip not found");
                            }
                            optionalNode.get().setDeleted(false);
                            optionalNode.get().save();
                        }

                    }

                    newNodeIds.add(node.getId());
                }

                for(Node oldNode : children) {
                    if (!newNodeIds.contains(oldNode.getId())) {
                        oldNode.delete();
                    }
                }



                for (NodeDTO node : tripDTO.getNodes()) {
                    if (node.type.toLowerCase().equals("trip")) {
                        Optional<TripNode> tNodeOptional = Optional.ofNullable(TripNode.find.byId(node.getId()));
                        if (!tNodeOptional.isPresent()) {
                            throw new CustomException(Http.Status.NOT_FOUND, "Trip node not found");
                        }
                        TripNode tNode = tNodeOptional.get();
                        tNode.setName(node.getName());
                        tNode.setOrdinal(node.getOrdinal());
                        tNode.update();
                    } else {
                        Optional<DestinationNode> dNodeOptional = Optional.ofNullable(DestinationNode.find.byId(node.getId()));
                        if (!dNodeOptional.isPresent()) {
                            throw new CustomException(Http.Status.NOT_FOUND, "Destination node not found");
                        }
                        DestinationNode dNode = dNodeOptional.get();
                        dNode.setName(node.getName());
                        dNode.setOrdinal(node.getOrdinal());
                        dNode.setArrivalDate(node.getArrivalDate());
                        dNode.setDepartureDate(node.getDepartureDate());

                        Optional<Destination> destinationOptional = Optional.ofNullable(Destination.find.byId(node.getDestination().getId()));
                        if (!destinationOptional.isPresent()) {
                            throw new CustomException(Http.Status.NOT_FOUND, "Destination for destination node not found");
                        }
                        dNode.setDestination(destinationOptional.get());

                        dNode.update();
                    }
                }

                trip.save();

                return trip;

            }).toCompletableFuture().join();
        }, context);

    }

    /**
     * Deletes a trip given an Id
     *
     * @param tripId
     * @return true if successful deletion, false if not
     */
    public CompletableFuture<Boolean> deleteTrip(Long tripId) {
        return supplyAsync(() -> {
            TripNode trip = TripNode.find.byId(tripId);
            if(trip != null) {
                trip.delete();

                return true;
            }

            return false;
        }, context);

    }

    /**
     * Toggles the deletion of a trip given an Id
     *
     * @param id
     * @return true if the trip is deleted
     */
    public CompletableFuture<Boolean> toggleTripDeleted(Long id) {
        return supplyAsync(() -> {
            Optional<TripNode> tripNodeOptional = TripNode.find.findByIdIncludeDeleted(id);

            if (tripNodeOptional.isPresent()) {
                TripNode tripNode = tripNodeOptional.get();
                tripNode.setDeleted(!tripNode.isDeleted());
                tripNode.update();
                return tripNode.isDeleted();
            }

            return false;
        });

    }

    /**
     * Updates a Group value to the group property of a Trip object
     * 
     * @param trip the Trip object
     * @param user the User object
     * @return the Trip id of the user that has been updated
     */
    public CompletableFuture<Long> changeUserTripStatus(TripStatusDTO tripStatusDTO, User user, Node trip) {
        return supplyAsync(() -> {

            // Gets the current user status and the status from the request
            NodeUserStatus userStatus = NodeUserStatus.find.query().where().eq("user", user).eq("trip", trip).findOne();
            TripStatus tripStatus = tripStatusDTO.getStatus();

            // Updates the user status if it exists otherwise creates a new user status entry.
            if (userStatus == null) {
                userStatus = new NodeUserStatus(user, trip, tripStatus);
                userStatus.insert();
            } else {
                userStatus.setTripStatus(tripStatus);
                userStatus.update();
            }

            // Finds all the children destinations of the trip which is being updated
            List<Node> childrenDestinations = Node.find.query().where().eq("parent", trip).eq("dtype", "destination")
                    .findList();

            // Updates the status of each of the child destinations to the same status as the parent trip.
            for (Node destinationNode : childrenDestinations) {
                NodeUserStatus userDestStatus = NodeUserStatus.find.query().where().eq("user", user)
                        .eq("node", destinationNode).findOne();
                userDestStatus.setTripStatus(tripStatus);
                userDestStatus.update();
            }

            return trip.id;

        }, context);
    }

    /**
     * 
     * @param trip  the Trip object*
     * @param group the Group object*@return the Trip id of the user that has been
     *              updated
     */

    public CompletableFuture<Long> toggleGroupInTrip(Node trip, Grouping group) {
        return supplyAsync(() -> {
            if (trip.getUserGroup() == null) {
                trip.setUserGroup(group);
            } else {
                trip.setUserGroup(null);
            }

            trip.update();
            return trip.getId();

        }, context);
    }

}
