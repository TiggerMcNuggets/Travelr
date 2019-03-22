package repository;

import com.google.inject.Inject;
import finders.TripFinder;
import models.Trip;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TripRepository {

    private DatabaseExecutionContext context;
    private TripFinder tripFinder = new TripFinder();

    @Inject
    public TripRepository(DatabaseExecutionContext context) {
        this.context = context;
    }

    public CompletableFuture<List<Trip>> getTrips(Long id) {
        return supplyAsync(() -> tripFinder.findAll(id), context);
    }

}
