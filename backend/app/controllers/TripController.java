package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import models.Trip;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import static play.libs.Scala.asScala;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.TripRepository;

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

public class TripController extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final TripRepository tripRepository;

    @Inject
    public TripController(FormFactory formFactory,
                          TripRepository tripRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        this.tripRepository = tripRepository;
        this.formFactory = formFactory;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
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
