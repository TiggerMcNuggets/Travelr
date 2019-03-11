package repository;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.Result;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.PagedList;
import io.ebean.Transaction;

import models.Destination;
import models.TripDestination;
import models.Trip;

import play.db.ebean.EbeanConfig;
import play.mvc.Http;
import play.mvc.Results;
import play.db.ebean.Transactional;

import utils.Moment;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class TripDestinationsRepository {
    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TripDestinationsRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }


    /**
     * Converts a TripDestination in json data format to a TripDestination object
     * @param data json file containing trip destination parameters in json format
     * @return a TripDestination object
     */
    private TripDestination tripDestinationParser(JsonNode data) {
        long tripID = data.at("/trip").asInt();
        long destinationID = data.at("/destination").asInt();
        Date arrivalDate = null;
        Date departureDate = null;
        Trip trip = ebeanServer.find(Trip.class).where().eq("id", tripID).findOne();
        if (trip == null) {
            return null;
        }
        Destination destination = ebeanServer.find(Destination.class).where().eq("id", destinationID).findOne();
        if (data.at("/arrivalDate") != null) {
            arrivalDate = (new Moment(data.at("/arrivalDate").asText())).toDOBFormat();
        }
        if (data.at("/arrivalDate") != null) {
            departureDate = (new Moment(data.at("/departureDate").asText())).toDOBFormat();
        }
        int orderNo;
        if (checkValidDestination(trip, findCurrentOrderNo(trip), destination)) {
            orderNo = findCurrentOrderNo(trip);
        } else {
            return null;
        }
        TripDestination tripDestination = new TripDestination(trip, destination, arrivalDate, departureDate, orderNo);
        return tripDestination;
    }

    /**
     * updates a given trip destination with the appropriate arrival and departure dates
     * @param data json file containing trip destination parameters in json format
     * @return a TripDestination object
     */
    private TripDestination updateTripDestination(JsonNode data, TripDestination tripDestination) {
        Date arrivalDate = null;
        Date departureDate = null;
        if (data.at("/arrivalDate") != null) {
            arrivalDate = (new Moment(data.at("/arrivalDate").asText())).toDOBFormat();
        }
        if (data.at("/arrivalDate") != null) {
            departureDate = (new Moment(data.at("/departureDate").asText())).toDOBFormat();
        }
        tripDestination.setArrivalDate(arrivalDate);
        tripDestination.setDepartureDate(departureDate);
        return tripDestination;
    }

    /**
     * inserts a TripDestination object into the database
     * @param req the http request
     * @return the TripDestination object that was inserted
     */
    public CompletionStage<TripDestination> add(Http.Request req) {
        return supplyAsync(() -> {
            JsonNode data = req.body().asJson();
            TripDestination tripDestination = tripDestinationParser(data);
            if (tripDestination == null) {
                return null;
            }
            tripDestination.save();
            return tripDestination;
        }, executionContext);
    }

    /**
     * queries the database for a list of all TripDestination objects in the database
     * @return a list of TripDestination objects
     */
    public CompletionStage<List<TripDestination>> list() {
        return supplyAsync(() -> ebeanServer.find(TripDestination.class).orderBy().asc("orderNo").findList(), executionContext);
    }

    /**
     * A function to find the next order number for tripdestinations using a trip id
     * @param trip the trip which the trip destination belongs to
     * @return the order number as an integer
     */
    public int findCurrentOrderNo(Trip trip) {
        int orderNo;
        List<TripDestination> tripDestinations = ebeanServer.find(TripDestination.class)
                .where()
                .eq("trip", trip)
                .orderBy()
                    .desc("orderNo")
                .findList();
        if (tripDestinations.isEmpty()) {
            orderNo = 0;
        } else {
            orderNo = tripDestinations.get(0).getOrderNo() + 1;
        }
        return orderNo;
    }

    /**
     * A function to check whether the destination is valid
     * @param trip the trip which the trip destination belongs to
     * @param orderNo the order number to be checked for validity
     * @param destination the destination of the trip destination
     * @return a true or false result, depending on whether the orderNo is valid for the trip destination
     */
    public boolean checkValidDestination (Trip trip, int orderNo, Destination destination) {
        TripDestination previousTripDestination = ebeanServer.find(TripDestination.class)
                .where()
                .eq("trip", trip)
                .eq("orderNo", (orderNo - 1))
                .findOne();
        TripDestination nextTripDestination = ebeanServer.find(TripDestination.class)
                .where()
                .eq("trip", trip)
                .eq("orderNo", (orderNo + 1))
                .findOne();
        if (previousTripDestination == null) {
            return true;
        }else if ((previousTripDestination.getDestination().getId() != destination.getId()) && (nextTripDestination == null)){
            return true;
        }else if ((previousTripDestination.getDestination().getId()) == destination.getId() && (nextTripDestination == null)) {
            return false;
        }else if (nextTripDestination.getDestination().getId() != (destination.getId())) {
            return true;
        }
        return false;
    }

    /**
     * Deletes a singular trip destination and changes the order number of trip destinations for that trip accordingly
     * @param id the trip id
     * @param orderNo the order number of the trip destination to be deleted
     * @return a true or false boolean depending on whether the deletion was successful
     */
    public Result deleteTripDestination (long id, int orderNo) {
        try (Transaction transaction = ebeanServer.beginTransaction()) {
            Trip trip = ebeanServer.find(Trip.class).where().eq("id", id).findOne();
            if (trip == null) {
                return notFound("trip not found");
            }
            int maxOrderNo = findCurrentOrderNo(trip) - 1;
            List<TripDestination> tripDestinations = ebeanServer.find(TripDestination.class)
                    .where()
                    .eq("trip", trip)
                    .orderBy()
                    .asc("orderNo")
                    .findList();
            if ((trip == null) || (tripDestinations == null)) {
                return ok("Trip successfully deleted");
            }
            for (int i = 0; i < (maxOrderNo - orderNo); i++) {
                TripDestination tripDestination = tripDestinations.get(maxOrderNo - i);
                tripDestination.orderNoSubtract();
                tripDestination.update();
            }
            TripDestination deletedTripDestination = tripDestinations.get(orderNo);
            tripDestinations.remove(orderNo);
            deletedTripDestination.delete();
            if (!checkValidDestination(trip, orderNo, tripDestinations.get(orderNo).getDestination())) {
                transaction.rollback();
                return badRequest("invalid destination deletion");
            }
            transaction.commit();
            return ok("Trip successfully deleted");
        }
    }

    /**
     * a function to swap the order number of two destinations
     * @param id the trip id
     * @param data the json file containing the first and second trip destination order number
     * @return a 404 not found error if trip or destinations not found, a 400 bad request if swap is invalid and a 200
     * success message if swapped successfully
     */
    public Result swapTripDestinations (Long id, JsonNode data) {
        try (Transaction transaction = ebeanServer.beginTransaction()) {
            Integer firstOrderNo = data.at("/first").asInt();
            Integer secondOrderNo = data.at("/second").asInt();
            Trip trip = ebeanServer
                    .find(Trip.class)
                    .where()
                    .eq("id", id)
                    .findOne();
            TripDestination firstDestination = ebeanServer
                    .find(TripDestination.class)
                    .where()
                    .eq("trip", trip)
                    .eq("orderNo", firstOrderNo)
                    .findOne();
            TripDestination secondDestination = ebeanServer
                    .find(TripDestination.class)
                    .where()
                    .eq("trip", trip)
                    .eq("orderNo", secondOrderNo)
                    .findOne();
            System.out.println("repository 1");
            if (trip == null || firstDestination == null || secondDestination == null) {
                return notFound("trip or trip destination not found");
            }
            firstDestination.setOrderNo(secondOrderNo);
            secondDestination.setOrderNo(firstOrderNo);
            boolean firstValidity = checkValidDestination(trip, secondOrderNo, firstDestination.getDestination());
            boolean secondValidity = checkValidDestination(trip, firstOrderNo, secondDestination.getDestination());
            if (!firstValidity || !secondValidity) {
                transaction.rollback();
                System.out.println("repository 2");
                return badRequest("Invalid trip destination order swap");
            }
            firstDestination.save();
            secondDestination.save();
            transaction.commit();
            return ok("order of trip destinations successfully swapped");
        }
    }

    /**
     *A function to update a trip destination from the database
     * @param data the data from the http request
     * @param id the trip id
     * @return a true or false boolean signifying update success/failure
     */
    public CompletionStage<Boolean> update(JsonNode data, Long id, int orderNo) {
        return supplyAsync(() -> {
            try (Transaction transaction = ebeanServer.beginTransaction()) {
                Trip trip = ebeanServer.find(Trip.class)
                        .where()
                        .eq("id", id)
                        .findOne();
                TripDestination tripDestination = ebeanServer.find(TripDestination.class)
                        .where()
                        .eq("trip", trip)
                        .eq("orderNo", orderNo)
                        .findOne();
                tripDestination = updateTripDestination(data, tripDestination);
                if (trip == null || tripDestination == null) {
                    transaction.rollback();
                    return false;
                } else {
                    tripDestination.save();
                }
                transaction.commit();
                return true;
            }
        });
    }
}