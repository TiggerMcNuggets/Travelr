package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;

import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TripDestinationsRepository;
import repository.TripRepository;

import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class TripController extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final TripRepository tripRepository;
    private final TripDestinationsRepository tripDestinationsRepository;

    @Inject
    public TripController(FormFactory formFactory,
                          TripRepository tripRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi,
                          TripDestinationsRepository tripDestinationsRepository) {
        this.tripRepository = tripRepository;
        this.formFactory = formFactory;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.tripDestinationsRepository = tripDestinationsRepository;
    }

    /**
     * Allows a user to display all of the trips
     *
     * @param request the http request
     * @return a 200 http response and trips object if successful, 401 in case user is not authorised
     */
    public CompletionStage<Result> list(Http.Request request) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return tripRepository.list().thenApplyAsync((trips) -> {
                return ok(Ebean.json().toJson(trips));
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }

    /**
     * takes an http request to insert a trip into the database
     * @return a 200 http response if successful, 400 otherwise for bad request
     */
    public CompletionStage<Result> add(Http.Request request) {
        JsonNode data = request.body().asJson();
        if (controllers.LoginController.isLoggedIn(request)) {
            return tripRepository.add(data).thenApplyAsync((trip) -> {
                if (trip == null) {
                    return badRequest("Bad Request - Failed to add the trip");
                } else {
                    for (JsonNode destination : data.at("/destinations")) {
                        tripDestinationsRepository.addGivenTripId(destination, trip.id);
                    }
                }
                return ok("Trip: " + trip + " added");
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }

    /**
     *takes an http request to update a trip from the database
     * @param request http request
     * @param id trip id
     * @return a 200 http response if successful, 400 otherwise for bad request
     */
    public CompletionStage<Result> update(Http.Request request, Long id) {
        JsonNode data = request.body().asJson();
        return tripRepository.update(data, id).thenApplyAsync((updateSuccess) -> {
            if (updateSuccess) {
                return ok("Trip successfully updated");
            } else {
                return badRequest("Trip update unsuccessful");
            }
        });
    }

    /**
     *Takes an http request to delete a trip from the database
     * @param id trip id
     * @return a 200 http response if successful, 400 otherwise for bad request
     */
    public CompletionStage<Result> delete(Long id) {
        return tripRepository.delete(id).thenApplyAsync((deleteSuccess) -> {
            if (deleteSuccess) {
                return ok("Trip successfully deleted");
            } else {
                return notFound("Trip not found");
            }
        });
    }
}
