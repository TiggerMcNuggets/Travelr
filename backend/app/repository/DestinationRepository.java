package repository;

import controllers.DestinationController;
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

    public CompletableFuture<List<Destination>> getUserDestinations(Long userId) {
        return supplyAsync(() -> Destination.find.getUserDestinations(userId), context);
    }

    public CompletableFuture<Destination> getById(Long destinationId) {
        return supplyAsync(() -> Destination.find.byId(destinationId));
    }

    public CompletableFuture<Long> add(DestinationController.DestinationRequest request, Long userId) {
        return supplyAsync(() -> {
            Destination destination = new Destination(request, User.find.byId(userId));
            destination.insert();
            return destination.id;
        });
    }

    public CompletableFuture<Long> update(DestinationController.DestinationRequest request, Long destinationId) {
        return supplyAsync(() -> {
            Destination destination = Destination.find.byId(destinationId);
            destination.name = request.name;
            destination.latitude = request.latitude;
            destination.longitude = request.longitude;
            destination.country = request.country;
            destination.district = request.district;
            destination.save();

            return destination.id;
        });
    }

    public CompletableFuture<Long> delete(Long id) {
        return supplyAsync(() -> {
            Destination.find.byId(id).delete();
            return id;
        });
    }
}
