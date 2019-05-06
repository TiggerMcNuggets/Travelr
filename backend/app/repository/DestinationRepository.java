package repository;

import controllers.DestinationController;
import controllers.dto.Destination.CreateDestReq;
import finders.DestinationFinder;
import models.Destination;
import models.User;

import javax.inject.Inject;
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
     * Gets list of destinations that belongs to a user
     * @param userId the user id
     * @return completable future of list of destinations
     */
    public CompletableFuture<List<Destination>> getUserDestinations(Long userId) {
        return supplyAsync(() -> Destination.find.getUserDestinations(userId), context);
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
    public CompletableFuture<Long> update(CreateDestReq request, Long destinationId) {
        return supplyAsync(() -> {
            Destination destination = Destination.find.byId(destinationId);
            destination.setName(request.name);
            destination.setLatitude(request.latitude);
            destination.setLongitude(request.longitude);
            destination.setType(request.type);
            destination.setCountry(request.country);
            destination.setDistrict(request.district);
            destination.save();

            return destination.id;
        }, context);
    }

    public CompletableFuture<Long> makeDestinationPublic(Long destinationId) {
        return supplyAsync(() -> {

            Destination destination = Destination.find.byId(destinationId);

            User adminUser = User.find.findById(1L);


            List<Destination> sameDestinations = Destination.find.getSameDestinations(destinationId);

            // No similar destinations, no merge has happened
            if(sameDestinations.size() == 0) {
                destination.isPublic = true;

                destination.update();

            } else {
                // TODO MERGE OTHER DESTINATIONS HERE
            }

            return destinationId;
        }, context);
    }



    
}
