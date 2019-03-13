package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.Transaction;
import models.Destination;
import models.TripDestination;
import models.Trip;
import models.Destination;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.libs.Scala.asScala;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.TripRepository;
import repository.DestinationRepository;
import repository.TripDestinationsRepository;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

import javax.inject.Inject;

public class TripDestinationsController extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final TripDestinationsRepository tripDestinationsRepository;

    @Inject
    public TripDestinationsController(FormFactory formFactory,
                                     TripDestinationsRepository tripDestinationsRepository,
                                     HttpExecutionContext httpExecutionContext,
                                     MessagesApi messagesApi) {
        this.tripDestinationsRepository = tripDestinationsRepository;
        this.formFactory = formFactory;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * Displays all combinations of destinations associated with their trip
     *
     * @param request the http request
     * @return 200 response and trip destinations json object
     */
    public CompletionStage<Result> list(Http.Request request) {
        if (controllers.LoginController.isLoggedIn(request)) {
        return tripDestinationsRepository.list().thenApplyAsync((tripDestinations) -> {
            if (tripDestinations == null) {
                return notFound("Trip Destinations not found");
            }
            return ok(Ebean.json().toJson(tripDestinations));
        });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     * takes an http request to insert a trip destination into the database
     * @return a 200 http response if successful, 400 otherwise for bad request
     */
    public CompletionStage<Result> add(Http.Request request) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return tripDestinationsRepository.add(request).thenApplyAsync((tripDestination) -> {
                if (tripDestination == null) {
                    return badRequest("Bad Request - Failed to add the trip destination" + tripDestination);
                }
                return ok("Trip: " + tripDestination + " added");
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     *Takes an http request to delete a trip from the database
     * @param id trip id
     * @return a 200 http response if successful, 400 otherwise for bad request
     */
    public Result delete(long id, int orderNo) {
        return tripDestinationsRepository.deleteTripDestination(id, orderNo);
    }

    /**
     *Takes an http request to delete a trip from the database
     * @param id trip id
     * @return a 200 http response if successful, 400 otherwise for bad request and 404 for not found
     */
    public Result swap(Http.Request request, long id) {
        System.out.println("controller 1");
        JsonNode data = request.body().asJson();
        return tripDestinationsRepository.swapTripDestinations(id, data);
    }

    /**
     *takes an http request to update a trip from the database
     * @param request http request
     * @param id trip id
     * @return a 200 http response if successful, 400 otherwise for bad request
     */
    public CompletionStage<Result> update(Http.Request request, Long id, int orderNo) {
        JsonNode data = request.body().asJson();
        return tripDestinationsRepository.update(data, id, orderNo).thenApplyAsync((updateSuccess) -> {
            if (updateSuccess) {
                return ok("Trip successfully updated");
            } else {
                return badRequest("Trip update unsuccessful");
            }
        });
    }
}