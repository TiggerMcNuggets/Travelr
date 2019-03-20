package controllers;

import controllers.actions.Attrs;
import controllers.actions.Authorization;
import io.ebean.Ebean;
import models.User;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.DestinationRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DestinationController extends Controller {


    @Inject
    FormFactory formFactory;


    private final DestinationRepository destinationRepository;

    @Inject
    public DestinationController(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }


//    public CompletionStage<Result> listPublicDestination() {
//        return destinationRepository
//                .getPublicDestinations()
//                .thenApplyAsync(destinations -> ok(Ebean.json().toJson(destinations)));
//    }

    @Authorization.RequireAuth
    public CompletionStage<Result> listUserDestinations(Http.Request request) {
        // Get authenticated user's id
        User user = request.attrs().get(Attrs.USER);
        System.out.print(user.id);
        return destinationRepository
                .getUserDestinations(user.id)
                .thenApplyAsync(destinations -> ok(Ebean.json().toJson(destinations)));

    }





    // Forms

    public static class CreateDestinatoinRequest {
        public String name;
        public Double latitude;
    }


}
