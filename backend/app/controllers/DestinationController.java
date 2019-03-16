package controllers;

import io.ebean.Ebean;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.DestinationRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Scala.asScala;

public class DestinationController extends Controller {

    /**
     * Destination Repository needed for database interaction for dealing with databases.
     */
    private  final DestinationRepository destinationRepository;

    /**
     * The Destination Controller Constructor.
     * @param destinationRepository The instance of the destination repository for database methods.
     */

    @Inject
    public DestinationController(
            DestinationRepository destinationRepository
    ) {
        this.destinationRepository = destinationRepository;
    }

    /**
     * Allows user to see a display of all the destinations.
     * @return 200 response and list of destinations in JSON format when successful, 401 in case user is not authorised
     */

    public CompletionStage<Result> list(Http.Request request) {
        if (controllers.LoginController.isLoggedIn(request)) {
        return destinationRepository.list().thenApplyAsync((destinations) -> {
            return ok(Ebean.json().toJson(destinations));
        });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     * @param request the http request
     * @param id the destination id
     * @return 200 response and JSON format when successful, 401 in case user is not authorised
     */
    public CompletionStage<Result> getOne(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return destinationRepository.getOne(id).thenApplyAsync((destination) -> {
                return ok(Ebean.json().toJson(destination));
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     * Allows the user to add a destination to the database directly by POST request.
     * @param request The HTTP POST request which contains information about the destination to be added.
     * @return A response message which describes if the insert was successful.
     */
    public CompletionStage<Result> add(Http.Request request) {
        if (controllers.LoginController.isLoggedIn(request)) {

        return destinationRepository.add(request).thenApplyAsync((destination) -> {
            if (destination == null) {
                return badRequest("Bad Request - Failed to add the destination");
            }

            return ok("Destination: " + destination + " added");
        });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     * Allows the user to delete a destination instance based on an destination id value.
     * @param id The destination id of the destination to be deleted.
     * @return A response message describing if the deletion was successful.
     */
    public CompletionStage<Result> delete(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request)) {

        return destinationRepository.delete(id).thenApplyAsync((destination) ->
            ok("Destination: " + destination +  "deleted")
        );
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     * Allows the user to update a destination instance which was previously created.
     * @param request The request containing data to update the destination instance.
     * @param id The id of the destination to be updated.
     * @return A response message describing if the destination was updated successfully.
     */
    public CompletionStage<Result> update(Http.Request request, Long id) {
        System.out.println("Here");
        if (controllers.LoginController.isLoggedIn(request)) {
        return destinationRepository.update(request, id).thenApplyAsync((destination) ->
                ok("Destination: " + destination +  "updated")
        );
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }




}
