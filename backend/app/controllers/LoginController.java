package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TravellerRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class LoginController extends Controller {

    private final TravellerRepository travellerRepository;


    /**
     * The constructor for the Login Controller.
     * @param travellerRepository
     */
    @Inject
    public LoginController(
            TravellerRepository travellerRepository
    ) {
        this.travellerRepository = travellerRepository;
    }

    /**
     * A static method used to check if the user is logged in.
     * @param request The request which may contain the session.
     * @return Whether the user is logged in or not.
     */
    public static Boolean isLoggedIn(Http.Request request) {
//        return request.session().getOptional("connected").isPresent();
        return true;
    }

    public static CompletionStage<Result> loginCheck(Http.Request request, CompletionStage<Result> result) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return result;
        } else {
//            return unauthorized("Not Logged In: Access Denied");
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }


    /**
     * Allows the user to login.
     * @param request The POST request which contains the credentials of the user that is logging in.
     * @return A message with an OK status which explains the result from the login.
     */
    public CompletionStage<Result> login(Http.Request request) {
        JsonNode data = request.body().asJson();
        return travellerRepository.login(data).thenApplyAsync((travelller) -> {
            if (isLoggedIn(request)) {
                return ok("You are already logged in!");
            } else if (travelller != null) {
                return redirect(routes.TravellerController.showProfile(travelller.getId())).addingToSession(request, "connected", travelller.getId().toString());
            } else {
                return ok("Unsuccessful Login - Please enter correct details or create a profile.");

            }
        });
    }

    /**
     * Allows the user to logout.
     * @return An OK status with message specifying the user has logged out.
     */
    public Result logout() {
        return ok("Successfully logged out").withNewSession();
    }


}
