package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Destination.CreateDestinationEditReq;
import controllers.dto.Destination.DestinationEditRequestResponse;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.DestinationEditRequestRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DestinationEditRequestController extends Controller {

    @Inject
    FormFactory formFactory;

    private final DestinationEditRequestRepository destinationEditRequestRepository;

    @Inject
    public DestinationEditRequestController(DestinationEditRequestRepository repository) {
        destinationEditRequestRepository = repository;
    }


    @Authorization.RequireAuth
    public CompletionStage<Result> createRequest(Http.Request request) {

        Form<CreateDestinationEditReq> destinationEditForm = formFactory.form(CreateDestinationEditReq.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if (destinationEditForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateDestinationEditReq destRequest = destinationEditForm.get();

        return destinationEditRequestRepository
                .createRequest(destRequest, user)
                .thenApplyAsync(id -> {

                    if(id == 0L) {
                        return badRequest();
                    }

                    return created(Json.toJson(id));
                });
    }


    @Authorization.RequireAdminAuth
    public CompletionStage<Result> getAllRequests(Http.Request request) {

        return destinationEditRequestRepository
                .getAllEditRequests()
                .thenApplyAsync(requests -> {
                    DestinationEditRequestResponse res = new DestinationEditRequestResponse(requests);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonResponse = mapper.valueToTree(res);
                    return ok(jsonResponse);
                });
    }

    @Authorization.RequireAdminAuth
    public CompletionStage<Result> acceptRequest(Http.Request request, Long id) {

        return destinationEditRequestRepository
                .acceptRequest(id)
                .thenApplyAsync(output -> {
                    if(output) {
                        return ok();
                    }

                    return notFound();

                });
    }

    @Authorization.RequireAdminAuth
    public CompletionStage<Result> denyRequest(Http.Request request, Long id) {
        return destinationEditRequestRepository
                .deleteRequest(id)
                .thenApplyAsync(output -> {
                    if(output) {
                        return ok();
                    }

                    return notFound();

                });
    }

}