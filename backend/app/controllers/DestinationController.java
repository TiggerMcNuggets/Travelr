package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import io.ebean.Ebean;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.DestinationRepository;

import javax.inject.Inject;
import javax.validation.Constraint;
import java.util.concurrent.CompletableFuture;
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

    @Authorization.RequireAuth
    public CompletionStage<Result> createDestination(Http.Request request) {
        Form<DestinationRequest> createDestinationForm = formFactory.form(DestinationRequest.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if(createDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest());
        }

        DestinationRequest req = createDestinationForm.get();

        return destinationRepository.add(req,user.id).thenApplyAsync(id -> {
            ObjectNode userResponse = Json.newObject();
            userResponse.put("id", id);
            return ok(userResponse);
        });
    }

    @Authorization.RequireAuth
    public CompletionStage<Result> updateDestination(Http.Request request, Long id) {
        Form<DestinationRequest> updateDestinationForm = formFactory.form(DestinationRequest.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if(updateDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest());
        }

        DestinationRequest req = updateDestinationForm.get();

        return destinationRepository.update(req,user.id).thenApplyAsync(destId -> ok());
    }

    @Authorization.RequireAuth
    public CompletionStage<Result> deleteDestination(Http.Request request, Long id) {

        return destinationRepository.delete(id).thenApplyAsync((destId) -> ok());
    }

    // Forms

    public static class DestinationRequest {
        @Constraints.Required
        public String name;
        @Constraints.Required
        public Double latitude;
        @Constraints.Required
        public Double longitude;
        @Constraints.Required
        public String type;
        @Constraints.Required
        public String district;
        @Constraints.Required
        public String country;

    }


}
