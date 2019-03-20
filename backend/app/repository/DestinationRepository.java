package repository;

import models.Destination;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class DestinationRepository {

    private DatabaseExecutionContext context;

    @Inject
    public DestinationRepository(DatabaseExecutionContext context) {

        this.context = context;
    }


//    public CompletableFuture<List<Destination>> getPublicDestinations() {
//        return supplyAsync(() -> Destination.find.getPublicDestinations(), context);
//    }

    public CompletableFuture<List<Destination>> getUserDestinations(Long userId) {
        return supplyAsync(() -> Destination.find.getUserDestinations(userId), context);
    }

    public CompletableFuture<Destination> getById(Long destinationId) {
        return supplyAsync(() -> Destination.find.byId(destinationId));
    }

    //public CompletableFuture<Long> add()
}
