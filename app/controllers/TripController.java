package controllers;


import com.google.inject.Inject;

import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import dto.trip.NodeDTO;
import dto.shared.CreatedDTO;
import dto.trip.*;

import exceptions.CustomException;
import models.*;
import net.fortuna.ical4j.model.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import service.MailgunService;
import service.TripService;
import utils.iCalCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TripController extends Controller {

    @Inject
    FormFactory formFactory;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TripService tripService;
    private final MailgunService mailgunService;

    @Inject

    public TripController(TripService tripService, MailgunService mailgunService) {
        this.mailgunService = mailgunService;
        this.tripService = tripService;
    }

    /**
     * Creates a .ics file to return to the user with the trip
     * @param req the http request
     * @param userId the id of the user
     * @param tripId the id of the trip
     * @return The .ics file generated for the trip
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserTripAsICalFile(Http.Request req, Long userId, Long tripId){

        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(req, userId);

        if (middlewareRes != null) return middlewareRes;

        TripNode trip = TripNode.find.byId(tripId);

        if (trip == null) return  CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));

        iCalCreator creator = new iCalCreator();
        Calendar iCalString = creator.createCalendarFromTrip(trip);
        try {
            File tempFile = File.createTempFile(trip.getName(), ".ics");
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write(iCalString.toString());
            bw.close();
            return CompletableFuture.completedFuture(ok(tempFile));
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(internalServerError());
        }
    }

    /**
     * Create a trip endpoint
     *
     * @param request
     * @param userId
     * @return
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> createTrip(Http.Request request, Long userId) {

        Form<CreateTripDTO> createTripForm = formFactory.form(CreateTripDTO.class).bindFromRequest(request);

        if (createTripForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest());
        }

        User user = request.attrs().get(Attrs.ACCESS_USER);

        CreateTripDTO dto = createTripForm.get();

        return tripService.createTrip(dto, user).thenApplyAsync((tripId) -> created(Json.toJson(new CreatedDTO(tripId))));
    }


    /**
     * Get all trips for a given user
     *
     * @param request
     * @param userId
     * @return
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> getUserTrips(Http.Request request, Long userId) {
        return tripService.getTripsForUser(userId).thenApplyAsync(trips -> {
            ArrayList<TripSimpleDTO> tripDTOS = new ArrayList<>();

            for (TripNode trip : trips) {
                tripDTOS.add(new TripSimpleDTO(trip));
            }

            return ok(Json.toJson(tripDTOS));
        });
    }

    /**
     * Fetches a trip by Id
     * The three parts of the response are root, trip, and navigation.
     * Root: Is the first level root trip which it belongs to
     * Trip: Is the trip that was fetched by the Id
     * Navigation: Is the list of nodes which make up the breadcrumbs
     * @param request the request object
     * @param tripId the Id of the trip being fetched
     * @param userId the User who the request is being sent for (used in middleware)
     * @returna GetTripResponse object
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> fetchTrip(Http.Request request, Long tripId, Long userId) {

        CompletionStage<Optional<TripNode>> tripStage = tripService.getTripById(tripId);

        CompletionStage<List<Node>> childrenStage = tripService.getChildrenByTripId(tripId);

        CompletionStage<List<TripNode>> navigationStage = tripService.getNavigationForTrip(tripId);


        /**
         * Wait for trip and children to be fetched.
         * Then create TripDTO
         */
        CompletionStage<GetTripDTO> tripDtoStage = tripStage.thenCombineAsync(childrenStage, (tripNodeOptional, children) -> {

            if (!tripNodeOptional.isPresent()) {
                throw new CustomException(404, "Trip not found");
            }

            Node trip = tripNodeOptional.get();

            GetTripDTO dto = new GetTripDTO();

            // TODO: Move this logic to DTO not sure why it is in the controller??

            // Trip Details
            dto.setName(trip.getName());
            dto.setId(trip.getId());

            // Format trip's children
            List<NodeDTO> childrenDTO = new ArrayList<>();

            for (Node node : children) {
                childrenDTO.add(new NodeDTO(node));
            }

            dto.setNodes(childrenDTO);


            // Format trip's usergroup
            List<NodeUserDTO> usergroupDTO = new ArrayList<>();
            Grouping grouping = tripService.getRootTripGrouping(tripId);


            if (grouping != null) {
                for (UserGroup user : grouping.getUserGroups()) {
                    usergroupDTO.add(new NodeUserDTO(user, trip));
                }
            }

            dto.setUsergroup(usergroupDTO);

            return dto;
        });


        /**
         * Wait for TripDTO and navigation to be ready
         * Then create and return GetTripResponse
         */
        return tripDtoStage.thenCombineAsync(navigationStage, (tripDto, navigation) -> {

            List<NavigationDTO> navigationDTOS = new ArrayList<>();

            for(TripNode node : navigation) {
                navigationDTOS.add(new NavigationDTO(node));
            }

            RootNodeDTO rootNodeDTO = new RootNodeDTO(navigation.get(0));

            GetTripResponse response = new GetTripResponse();

            response.setTrip(tripDto);
            response.setNavigation(navigationDTOS);
            response.setRoot(rootNodeDTO);

            return ok(Json.toJson(response));
        }).handle((result, ex)-> {
            return handleTrips(result, ex);
        });
    }


    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> updateTrip(Http.Request request, Long tripId, Long userId) {

        Form<GetTripDTO> updateTripForm = formFactory.form(GetTripDTO.class).bindFromRequest(request);

        User user = request.attrs().get(Attrs.ACCESS_USER);

        if (updateTripForm.hasErrors()) {
            logger.error("Trip object is not valid");
            return CompletableFuture.completedFuture(badRequest());
        }

        GetTripDTO tripDTO = updateTripForm.get();

        return tripService.updateTrip(tripId, tripDTO, user).thenApplyAsync(trip -> {

            List<Node> children = tripService.getChildrenByTripId(tripId).join();


            GetTripDTO dto = new GetTripDTO();

            // Trip Details
            dto.setName(trip.getName());
            dto.setId(trip.getId());

            // Format trip's children
            List<NodeDTO> childrenDTO = new ArrayList<>();

            for (Node node : children) {
                childrenDTO.add(new NodeDTO(node));
            }

            dto.setNodes(childrenDTO);


            return ok(Json.toJson(dto));


        }).handle((result, ex)-> {
            return handleTrips(result, ex);
        });
    }

    /**
     * Function for handling the async code within trips
     * @param result the result of the async code passed in
     * @param ex any errors thrown by the code
     * @return the result unless there is an error
     */
    private Result handleTrips(Result result, Throwable ex) {
        if(ex != null) {
            if(ex.getMessage().equals(CustomException.class.getCanonicalName())) {
                CustomException exception = (CustomException)ex.getCause();
                return status(exception.getResult(), exception.getResultMessage());
            }

            logger.error(ex.getMessage());
            ex.printStackTrace();
            return status(500);
        }

        return result;
    }


    /**
     * Soft Deletes a trip
     * @param request the http request
     * @param userId the id of the user
     * @param tripId the id of the destination
     *
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> softDeleteTrip(Http.Request request, Long tripId, Long userId) {

        return tripService.toggleTripDeleted(tripId).thenApplyAsync(deleted -> {
            return ok(Json.toJson(deleted));
        });
    }

    /**
     * Changing the user trip attendance status.
     *
     * @param request the http request
     * @param tripId  the id of the trip object
     * @param userId  the id of the user
     * @return 200 for updating successfully, 404 for not found user/trip, 401 for
     *         unauthenticated, 403 for unauthorised
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> changeUserTripStatus(Http.Request request, Long tripId, Long userId) {
        Optional<Node> node = Optional.ofNullable(Node.find.byId(tripId));
        Optional<User> user = Optional.ofNullable(User.find.byId(userId));

        Form<TripStatusDTO> updateTripForm = formFactory.form(TripStatusDTO.class).bindFromRequest(request);

        if (updateTripForm.hasErrors()) {
            logger.error("Trip object is not valid");
            return CompletableFuture.completedFuture(badRequest());
        }

        TripStatusDTO tripStatusDTO = updateTripForm.get();

        return tripService.changeUserTripStatus(tripStatusDTO, user.get(), node.get())
                .thenApplyAsync(id -> ok(APIResponses.TRIP_STATUS_UPDATED));
    }


    /**
     * Toggles a relation between a trip and a group
     *
     * @param request the http request
     * @param tripId  the id of the trip object
     * @param userId  the id of the user
     * @param groupId the id of the group
     * @return 200 for updating successfully, 404 for not found user/group/trip, 401
     *         for unauthenticated, 403 for unauthorised
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> addGroupToTrip(Http.Request request, Long tripId, Long userId, Long groupId) {
        Optional<Node> node = Optional.ofNullable(Node.find.byId(tripId));
        Optional<User> user = Optional.ofNullable(User.find.byId(userId));
        Optional<Grouping> group = Optional.ofNullable(Grouping.find.byId(groupId));

        if (!node.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        }
        if (!user.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRAVELLER_NOT_FOUND));
        }
        if (!group.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.GROUP_NOT_FOUND));
        }

        return tripService.toggleGroupInTrip(node.get(), group.get())
                .thenApplyAsync(id -> ok(APIResponses.TRIP_GROUP_UPDATED));
    }

    /**
     * Deletes all trip user statuses for trip and children of the trip
     * @param group the group which need to have statuses deleted
     * @param tNode the trip node to delete status for.
     */
    public void deleteTripUserStatus(Grouping group, TripNode tNode) {
        List<Node> tripNodes = tripService.getChildrenByTripId(tNode.getId()).join();

        for (Node node: tripNodes) {
            tripService.deleteTripUserStatus(group, tNode);
            if(node.getClass() == TripNode.class) {
                deleteTripUserStatus(group, (TripNode) node);
            }
        }
    }

    /**
     * Toggles a relation between a trip and a group
     *
     * @param request the http request
     * @param tripId  the id of the trip object
     * @param userId  the id of the user
     * @param groupId the id of the group
     * @return 200 for updating successfully, 404 for not found user/group/trip, 401
     *         for unauthenticated, 403 for unauthorised
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> deleteGroupFromTrip(Http.Request request, Long tripId, Long userId, Long groupId) {
        Optional<Node> node = Optional.ofNullable(Node.find.byId(tripId));
        Optional<User> user = Optional.ofNullable(User.find.byId(userId));
        Optional<Grouping> group = Optional.ofNullable(Grouping.find.byId(groupId));

        if (!node.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        }

        if (!group.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.GROUP_NOT_FOUND));
        }

        // Getting a list of owners for the group.
        List<Long> owners = new ArrayList<>();
        for (UserGroup userGroup : group.get().getUserGroups()) {
            if (userGroup.isOwner()) {
                owners.add(userGroup.getUser().getId());
            }
        }

        // First check if the user exists
        if (!user.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRAVELLER_NOT_FOUND));

        // Check if the user is indeed a grouping owner.
        } else if (!owners.contains(user.get().getId())) {
            return CompletableFuture.completedFuture(notFound(APIResponses.FORBIDDEN));
        }

        // Deletes the relating user status relating to the group to delete.
        this.deleteTripUserStatus(group.get(), (TripNode) node.get());

        // Now delete the group from the trip.
        return tripService.deleteGroupFromTrip(node.get())
                .thenApplyAsync(id -> ok(APIResponses.TRIP_GROUP_UPDATED));
    }

}
