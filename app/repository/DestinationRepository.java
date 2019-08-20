package repository;

import controllers.dto.Destination.CreateDestReq;
import controllers.dto.TravellerType.CreateTravellerTypeReq;
import models.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class DestinationRepository {

    private DatabaseExecutionContext context;

    @Inject
    public DestinationRepository(DatabaseExecutionContext context) {
        this.context = context;
    }
    

    /**
     * Gets list of destinations that is avaliable for a user
     * @param userId the user id
     * @return completable future of list of destinations
     */
    public CompletableFuture<List<Destination>> getAvailableDestinations(Long userId) {
        return supplyAsync(() -> Destination.find.getAvaliableDestinations(userId), context);
    }

    /**
     * Gets one destination that belongs to a user
     * @param id the destination id
     * @return completable future of the destination
     */
    public CompletableFuture<Destination> getOneDestination(Long id) {
        return supplyAsync(() -> Destination.find.findById(id), context);
    }

    /**
     * Adds a destination for a user
     * @param request the request DTO
     * @param userId the user id
     * @return completable future of the new id
     */
    public CompletableFuture<Long> add(CreateDestReq request, Long userId) {
        return supplyAsync(() -> {

            User user = User.find.byId(userId);

            Destination destination = new Destination(request, user);

            ArrayList<TravellerType> travellerTypes = new ArrayList<TravellerType>();
            if (request.travellerTypes != null) {

                for (CreateTravellerTypeReq travellerTypeReq : request.travellerTypes) {
                    travellerTypes.add(TravellerType.find.byId(travellerTypeReq.id));
                }
            }

            destination.getTravellerTypes().clear();
            destination.getTravellerTypes().addAll(travellerTypes);

            List<Destination> sameDestinations = Destination.find.getSameDestinationsAvailable(destination, userId);



            if (sameDestinations.size() > 0) {
                return null;
            }
            
            destination.insert();



            return destination.id;
        }, context);
    }

    /**
     * Updates a destination that belongs to a user
     * @param request the request DTO
     * @param destinationId the destination id
     * @return completable future of the new destination
     */
    public CompletableFuture<Long> update(CreateDestReq request, Long destinationId, Long userId) {
        return supplyAsync(() -> {
            Destination destination = Destination.find.byId(destinationId);
            destination.setName(request.name);
            destination.setLatitude(request.latitude);
            destination.setLongitude(request.longitude);
            destination.setType(request.type);
            destination.setCountry(request.country);
            destination.setDistrict(request.district);
            destination.travellerTypes.clear();

            destination.travellerTypes = new ArrayList<TravellerType>();
            if(request.travellerTypes != null) {
                for (CreateTravellerTypeReq createTravellerTypeReq : request.travellerTypes) {
                    destination.travellerTypes.add(TravellerType.find.byId(createTravellerTypeReq.id));
                }
            }
            List<Destination> sameDestinations = Destination.find.getSameDestinationsAvailable(destination, userId);

            if (sameDestinations.size() > 0) {
                for (Destination dest : sameDestinations) {
                    if (dest.getId() != destinationId) {
                        return null;
                    }
                }
            }

            destination.save();

            return destination.id;
        }, context);
    }

    /**
     * Makes a destination public and merges previous destinations if same
     * @param destinationId the new destination
     * @return completable future of the destination id
     */
    public CompletableFuture<Long> makeDestinationPublic(Long destinationId) {
        return supplyAsync(() -> {

            Destination destination = Destination.find.byId(destinationId);

            if (destination == null) {
                return null;
            }

            List<Destination> sameDestinations = Destination.find.getSameDestinations(destinationId);

            // If same destinations: merge
            if(sameDestinations.size() > 0) {
                mergeDestinations(destination, sameDestinations);
            }

            destination.setPublic(true);
            destination.update();

            return destinationId;
        }, context);
    }

    /**
     * Merges destinations by converting all same destinations in trips to the new destination
     * Merges images from all destinations in sameDestinations into destination
     * @param sameDestinations The list of same destinations
     */
    private void mergeDestinations(Destination destination, List<Destination> sameDestinations) {
        List<DestinationNode> tripDestinations = new ArrayList<DestinationNode>();
        List<DestinationPhoto> destinationPhotos = new ArrayList<DestinationPhoto>();

        for (Destination sameDestination : sameDestinations) {
            destinationPhotos.addAll(DestinationPhoto.find.getAllPhotosForDestination(sameDestination.id));
            tripDestinations.addAll(DestinationNode.find.query().where().eq("destination", sameDestination).findList());
        }

        // Transfer destination in trips
        boolean destinationTaken = false;
        for (DestinationNode tripDestination : tripDestinations) {
            destinationTaken = true;
            tripDestination.setDestination(destination);
            tripDestination.save();
        }

        // Transfer destination photos
        for (DestinationPhoto destinationPhoto : destinationPhotos) {
            destinationPhoto.setDestination(destination);
            destinationPhoto.save();
        }


        if (destinationTaken) {
            Destination.find.transferToAdmin(destination.getId());
        }

        // Delete destination
        for (Destination sameDestination : sameDestinations) {
            if (sameDestination.getId() != destination.getId()) {
                sameDestination.delete();
            }
        }

    }

    /**
     *
     * The method deletes destination
     * @param destId the id of the destination to delete
     */
    public void deleteDestination(Long destId) {
        Destination dest = Destination.find.findById(destId);
        dest.delete();
        dest.update();
    }

    /**
     * Changes the user's destination field to the oppositive of its current value
     * @param id the users id
     * @return the users new deleted value
     */
    public CompletableFuture<Boolean> toggleDestinationDeleted(Long id) {
        return supplyAsync(() -> {
            Destination dest = Destination.find.findByIdIncludeDeleted(id);
            dest.setDeleted(!dest.deleted);

            dest.update();
            return dest.isDeleted();
        }, context);
    }

    /**
     * Checks if the same destination has been used in another trip
     * @param destination The destination
     * @return true if the destination has been used
     */
    public Boolean isDestinationUsed(Destination destination) {

        List<DestinationNode> tripDestinations = DestinationNode.find.query().where().eq("destination", destination).findList();
        return !tripDestinations.isEmpty();
    }


}
