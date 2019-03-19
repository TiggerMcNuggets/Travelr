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

    public CompletableFuture<List<TravellerType>> getTravellerTypes() {
        return supplyAsync(() -> {
            return TravellerType.find.all();
        }, context);
    }
}
