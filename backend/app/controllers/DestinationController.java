package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.dto.Destination.CreateDestReq;
import controllers.dto.Destination.CreateDestRes;
import controllers.dto.User.GetUserRes;
import io.ebean.Ebean;
import models.User;
import org.omg.CosNaming.NamingContextPackage.NotFound;
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


    @Authorization.RequireAuth
    public CompletionStage<Result> getUserDestinations(Http.Request request) {
        User user = request.attrs().get(Attrs.USER);
        return destinationRepository
                .getUserDestinations(user.id)
                .thenApplyAsync(destinations -> ok(Ebean.json().toJson(destinations)));

    }

    @Authorization.RequireAuth
    public CompletionStage<Result> createDestination(Http.Request request) {
        Form<CreateDestReq> createDestinationForm = formFactory.form(CreateDestReq.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if (createDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        CreateDestReq req = createDestinationForm.get();

        return destinationRepository.add(req,user.id).thenApplyAsync(id -> {
            CreateDestRes response = new CreateDestRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return ok(jsonResponse);
        });
    }

    @Authorization.RequireAuth
    public CompletionStage<Result> getUserDestination(Http.Request request, Long id) {
        User user = request.attrs().get(Attrs.USER);
        return destinationRepository.getOneDestination(id).thenApplyAsync(destination -> {
            // Not Found Check
            if (destination == null) {
                return notFound("Destination not found");
            }
            // Forbidden Check
            if (destination.user.id != user.id) {
                return forbidden("Forbidden: Access Denied");
            }
            return ok(Ebean.json().toJson(destination));
        });

    }

    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserDestination(Http.Request request, Long id) {
        Form<CreateDestReq> updateDestinationForm = formFactory.form(CreateDestReq.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if(updateDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest());
        }

        CreateDestReq req = updateDestinationForm.get();

        return destinationRepository.getOneDestination(id).thenComposeAsync(destination -> {
            // Not Found Check
            if (destination == null) {
                return CompletableFuture.completedFuture(notFound("Destination not found"));
            }
            // Forbidden Check
            if (destination.user.id != user.id) {
                return CompletableFuture.completedFuture(forbidden("Forbidden: Access Denied"));
            }
            return destinationRepository.update(req, id).thenApplyAsync(destId -> ok("Destination updated"));

        });

    }

}
