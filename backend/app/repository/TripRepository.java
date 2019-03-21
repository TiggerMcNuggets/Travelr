package repository;

import com.fasterxml.jackson.databind.JsonNode;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.PagedList;
import io.ebean.Transaction;

import models.Destination;
import models.TripDestination;
import models.Trip;
import models.Traveller;

import play.db.ebean.EbeanConfig;
import play.mvc.Http;
import play.mvc.Results;
import play.db.ebean.Transactional;

import javax.inject.Inject;
import javax.xml.transform.Result;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class TripRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final TripDestinationsRepository tripDestinationsRepository;

    @Inject
    public TripRepository(EbeanConfig ebeanConfig,
                          DatabaseExecutionContext executionContext,
                          TripDestinationsRepository tripDestinationsRepository) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.tripDestinationsRepository = tripDestinationsRepository;
    }

    /**
     * Converts a Trip in json data format to a Trip object
     * @param data json file containing trip parameters in json format
     * @return a Trip object
     */
    public Trip tripParser(JsonNode data) {
        System.out.println(data.asText());
        String name = data.at("/name").asText();
        long travellerID = data.at("/travellerID").asLong();
        Traveller traveller = ebeanServer.find(Traveller.class).where().eq("id", travellerID).findOne();
        Trip trip = new Trip(name, traveller);
        return trip;
    }

    /**
     * Converts a Trip in json data format to a Trip object
     * @param data json file containing trip parameters in json format
     * @return a Trip object
     */
    public Trip updateTrip(JsonNode data, Trip trip) {
        String name = data.at("/name").asText();
        trip.setName(name);
        return trip;
    }


    /**
     * inserts a Trip object into the database
     * @param data the data from the http request
     * @return the Trip object that was inserted
     */
    public CompletionStage<Trip> add(JsonNode data) {
        return supplyAsync(() -> {
            Trip trip = tripParser(data);
            trip.save();
            return trip;
        }, executionContext);
    }


    /**
     * queries the database for a list of all Trip objects in the database
     * @return a list of Trip objects
     */
    public CompletionStage<List<Trip>> list() {
        return supplyAsync(() -> ebeanServer.find(Trip.class).findList(), executionContext);
    }

    /**
     *A function to update a trip's name from the database
     * @param data the data from the http request
     * @param id the trip id
     * @return a true or false boolean signifying update success/failure
     */
    public CompletionStage<Boolean> update(JsonNode data, Long id) {
        return supplyAsync(() -> {
            try (Transaction transaction = ebeanServer.beginTransaction()) {
                Trip trip = ebeanServer.find(Trip.class).where().eq("id", id).findOne();
                trip = updateTrip(data, trip);
                if (trip == null) {
                    transaction.rollback();
                    return false;
                } else {
                    trip.save();
                }
                transaction.commit();
                return true;
            }
        });
    }

    /**
     *A function to delete a trip from the database
     * @param id the trip id
     * @return a true or false boolean signifying deletion success/failure
     */
    public CompletionStage<Boolean> delete(Long id) {
        return supplyAsync(() -> {
            try (Transaction transaction = ebeanServer.beginTransaction()) {
                Trip trip = ebeanServer.find(Trip.class).where().eq("id", id).findOne();
                if (trip == null) {
                    transaction.rollback();
                    return false;
                }
                trip.delete();
                transaction.commit();
                return true;
            }
        });
    }
}