package repository;

import models.TravellerType;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TravellerTypeRepository {

    private DatabaseExecutionContext context;

    @Inject
    public TravellerTypeRepository(DatabaseExecutionContext context) {
        this.context = context;
    }

    /**
     * Gets all traveller types
     * @return completable future of list of traveller types
     */
    public CompletableFuture<List<TravellerType>> getTravellerTypes() {
        return supplyAsync(() -> TravellerType.find.all(), context);
    }
}
