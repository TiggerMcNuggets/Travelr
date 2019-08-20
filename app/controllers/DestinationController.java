package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Destination.CreateDestReq;
import controllers.dto.Destination.CreateDestRes;
import controllers.dto.Destination.GetDestinationsRes;
import io.ebean.Ebean;
import models.Destination;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.DestinationRepository;

import javax.inject.Inject;
import java.util.ArrayList;
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

    /**
     * Gets a list of all destinations that belong to the specified user
     * @param request the http request
     * @param userId the id of the specified user
     * @return 200 with list of destinations if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserDestinationsGivenUser(Http.Request request, Long userId) {

        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        return destinationRepository
                .getAvailableDestinations(userId)
                .thenApplyAsync(destinations -> {
                    ArrayList<GetDestinationsRes> list = new ArrayList<>();

                    for(Destination destination : destinations) {
                        list.add(new GetDestinationsRes(destination));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonResponse = mapper.valueToTree(list);

                    for (int i = 0; i < jsonResponse.size(); i++) {
                        ((ObjectNode)jsonResponse.get(i)).remove("public");
                    }

                    return ok(jsonResponse);
                });
    }

    /**
     * Gets specific destination that belongs to the specified user
     * @param request the http request
     * @param userId the id of the specified user
     * @return 200 with list of destinations if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserDestinationGivenUser(Http.Request request, Long userId, Long destId) {

        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        Destination destination = Destination.find.findByIdIncludeDeleted(destId);
        if (destination == null) return  CompletableFuture.completedFuture(notFound(APIResponses.DESTINATION_NOT_FOUND));

        if (!destination.isPublic || (destination.isPublic && destinationRepository.isDestinationUsed(destination))) {
            CompletionStage<Result> authorisedToView = Authorization.isUserAuthorisedToViewTrip(request, userId, destination.user.id);
            if (authorisedToView != null) return authorisedToView;
        }

        Object response;
        response = new GetDestinationsRes(destination);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonResponse = mapper.valueToTree(response);
        ((ObjectNode)jsonResponse).remove("public");
        return CompletableFuture.completedFuture(ok(jsonResponse));
    }

    /**
     * Creates destination for a user
     * @param request the http request
     * @return 201 with json object of new id if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> createDestination(Http.Request request) {
        Form<CreateDestReq> createDestinationForm = formFactory.form(CreateDestReq.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if (createDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateDestReq req = createDestinationForm.get();

        return destinationRepository.add(req,user.id).thenApplyAsync(id -> {
            CreateDestRes response = new CreateDestRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return created(jsonResponse);
        });
    }
// NEW, ADDED FOR ADMIN REFACTORING
    /**
     * Creates destination for a user
     * @param request the http request
     * @param userId the user id to create a trip for
     * @return 201 with json object of new id if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> createDestinationGivenUser(Http.Request request, Long userId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        Form<CreateDestReq> createDestinationForm = formFactory.form(CreateDestReq.class).bindFromRequest(request);


        if (createDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateDestReq req = createDestinationForm.get();

        return destinationRepository.add(req,userId).thenApplyAsync(id -> {

            // If destination already exists
            if (id == null) {
                return badRequest();
            }

            CreateDestRes response = new CreateDestRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return created(jsonResponse);
        });
    }

    /**
     * Gets a single destination that belongs to a user and matches the given id
     * @param request the http request
     * @param id the destination id
     * @return 200 with destination if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserDestination(Http.Request request, Long id) {
        User user = request.attrs().get(Attrs.USER);
        return destinationRepository.getOneDestination(id).thenApplyAsync(destination -> {
            // Not Found Check
            if (destination == null) {
                return notFound(APIResponses.DESTINATION_NOT_FOUND);
            }
            // Forbidden Check
            if (destination.user.id != user.id) {
                return forbidden("Forbidden: Access Denied");
            }
            return ok(Ebean.json().toJson(destination));
        });
    }

    /**
     * Updates a destination that belongs to a user
     * @param request the http request
     * @param userId the id of the destination
     * @param destId the id of the destination
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserDestinationGivenUser(Http.Request request, Long userId, Long destId) {
        Form<CreateDestReq> updateDestinationForm = formFactory.form(CreateDestReq.class).bindFromRequest(request);

        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        if(updateDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateDestReq req = updateDestinationForm.get();

        return destinationRepository.getOneDestination(destId).thenComposeAsync(destination -> {
            // Not Found Check
            if (destination == null) {
                return CompletableFuture.completedFuture(notFound(APIResponses.DESTINATION_NOT_FOUND));
            }

            Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);
            Boolean isDestinationOwner = (destination.getUser().getId() == userId);
            if(!destination.isPublic && !isAdmin && !isDestinationOwner) {
                return CompletableFuture.completedFuture(Results.forbidden(APIResponses.FORBIDDEN_DESTINATION_EDIT));
            }

            return destinationRepository.update(req, destId, userId).thenApplyAsync(destinationId -> {

                //If destination is the same as another destination
                if (destinationId == null) {
                    return badRequest();
                }

                return ok("Destination updated");
            });
        });
    }

    /**
     * Updates a destination that belongs to a user
     * @param request the http request
     * @param id the id of the destination
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserDestination(Http.Request request, Long id) {
        Form<CreateDestReq> updateDestinationForm = formFactory.form(CreateDestReq.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.USER);

        if(updateDestinationForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }
        Destination destination = Destination.find.findById(id);
        if (destination == null) return  CompletableFuture.completedFuture(notFound(APIResponses.DESTINATION_NOT_FOUND));

        CompletionStage<Result> authorisedToView = Authorization.isUserAuthorisedToViewTrip(request, user.getId(), destination.user.id);
        if (authorisedToView != null) return authorisedToView;

        CreateDestReq req = updateDestinationForm.get();

        return destinationRepository.update(req, id, user.getId()).thenApplyAsync(destId -> ok("Destination updated"));

    }

    /**
     * Toggles a destination's deletion status
     * @param req the http request
     * @param userId the id of the user
     * @param destId the id of the destination
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> toggleDestinationDeleted(Http.Request req, Long userId, Long destId) {

        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(req, userId);

        if (middlewareRes != null) return middlewareRes;
        Destination destination = Destination.find.findByIdIncludeDeleted(destId);

        if (destination == null) return  CompletableFuture.completedFuture(notFound(APIResponses.DESTINATION_NOT_FOUND));

        CompletionStage<Result> authorisedToView = Authorization.isUserAuthorisedToViewTrip(req, userId, destination.user.id);
        if (authorisedToView != null) return authorisedToView;

        return destinationRepository.toggleDestinationDeleted(destId).thenApplyAsync(deleted -> {

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();

            response.put("id", destId);
            response.put("deleted", deleted);

            return ok(response.toString());
        });

    }

    /**
     * Makes a destination public
     * @param id The id of the destination that is going to be made public
     * @return 201 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> makeDestinationPublic(Long id) {

        return destinationRepository.makeDestinationPublic(id).thenApplyAsync(destinationId -> {
            if (destinationId == null) {
                return notFound();
            }
            return created("Destination is now public");
        });
    }
    /**
     * Makes a destination private
     * @param id The id of the destination that is going to be made private
     * @return 201 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> makeDestinationPrivate(Http.Request request, Long id) {

        User user = request.attrs().get(Attrs.USER);

        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, user.id);
        if (middlewareRes != null) return middlewareRes;

        return destinationRepository.getOneDestination(id).thenApplyAsync(destination -> {

            // Check that destination hasn't been used by anyone else (null)
            if (destination != null && destination.user == null) {
                return forbidden("Forbidden: There are other users using the destination");
            }

            destination.setPublic(false);
            destination.save();
            return created("Destination is now private");
        });
    }
}
