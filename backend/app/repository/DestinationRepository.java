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

    private DestinationFinder destinationFinder = new DestinationFinder();

    @Inject
    public DestinationRepository(DatabaseExecutionContext context) {
        this.context = context;
    }

    public CompletableFuture<List<Destination>> getUserDestinations(Long userId) {
        return supplyAsync(() -> destinationFinder.getUserDestinations(userId), context);
    }

    public CompletableFuture<Destination> getOneDestination(Long id) {
        return supplyAsync(() -> destinationFinder.findById(id), context);
    }

    public CompletableFuture<Long> add(CreateDestReq request, Long userId) {
        return supplyAsync(() -> {
            Destination destination = new Destination(request, User.find.byId(userId));
            destination.insert();
            return destination.id;
        });
    }

    public CompletableFuture<Long> update(CreateDestReq request, Long destinationId) {
        return supplyAsync(() -> {
            Destination destination = destinationFinder.byId(destinationId);
            destination.name = request.name;
            destination.latitude = request.latitude;
            destination.longitude = request.longitude;
            destination.country = request.country;
            destination.district = request.district;
            destination.save();

            return destination.id;
        });
    }
}
