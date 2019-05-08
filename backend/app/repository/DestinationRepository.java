package repository;

import controllers.dto.destination.CreateDestReq;
import models.Destination;
import models.TripDestination;
import models.User;

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



            Destination destination = new Destination(request, User.find.byId(userId));
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

            destination.isPublic = true;
            destination.update();

            return destinationId;
        }, context);
    }

    /**
     * Merges destinations by converting all same destinations in trips to the new destination
     * @param sameDestinations The list of same destinations
     */
    private void mergeDestinations(Destination destination, List<Destination> sameDestinations) {
        List<TripDestination> tripDestinations = new ArrayList<TripDestination>();

        for (Destination sameDestination : sameDestinations) {
            tripDestinations.addAll(TripDestination.find.getAllByDestinationId(sameDestination.getId()));
        }

        for (TripDestination tripDestination : tripDestinations) {
            tripDestination.setDestination(destination);
            tripDestination.save();
        }
    }

    /**
     *
     * The method sets the variable deleted to true for the destination with given id
     * @param destId the id of the destination to shallow delete
     */
    public void shallowDeleteDestination(Long destId) {
        Destination dest = Destination.find.findById(destId);
        dest.setDeleted(true);
        dest.update();
    }
}
