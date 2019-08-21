package controllers;


import com.google.inject.Inject;

import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
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

            if(!tripNodeOptional.isPresent()) {
                throw new CustomException(404, "Trip not found");
            }

            TripNode trip = tripNodeOptional.get();

            GetTripDTO dto = new GetTripDTO();

            // Trip Details
            dto.setName(trip.getName());
            dto.setId(trip.getId());

            // Format trip's children
            List<NodeDTO> childrenDTO = new ArrayList<>();

            for (Node node : children) {
                System.out.println(node.getClass());
                childrenDTO.add(new NodeDTO(node));
            }

            dto.setNodes(childrenDTO);

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
     * //     * Soft Deletes a trip
     * //     * @param req the http request
     * //     * @param userId the id of the user
     * //     * @param tripId the id of the destination
     * //
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> softDeleteTrip(Http.Request request, Long tripId, Long userId) {

        return tripService.toggleTripDeleted(tripId).thenApplyAsync(deleted -> {
            return ok(Json.toJson(deleted));
        });
    }

    /**
     * TEST FUNCTION DELETE BEFORE MASTER MERGE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @return
     * @throws Exception
     */
    public Result test() throws Exception {

        User user = User.find.findById(3L);

        Destination dest1 = new Destination("Destination 1", 1.0, 1.0, "type", "district", "country", user);
        dest1.insert();

        Destination dest2 = new Destination("Destination 2", 2.0, 2.0, "type2", "district2", "country2", user);
        dest2.insert();

        DestinationNode tdf1 = new DestinationNode("Custom Destination 1 Name", user, dest1);
        tdf1.setOrdinal(1);
        DestinationNode tdf2 = new DestinationNode("Custom Destination 2 Name", user, dest2);
        tdf2.setOrdinal(0);

        TripNode tc = new TripNode("Level 1 Inner Trip", user);
        tc.setOrdinal(1);
        tc.save();


        tdf1.setParent(tc);
        tdf1.save();
        tdf2.setParent(tc);
        tdf2.save();


        Destination dest3 = new Destination("Destination 3", 3.0, 3.0, "type3", "district3", "country3", user);
        dest3.insert();


        DestinationNode tdf3 = new DestinationNode("Custom Destination 3 Name", user, dest3);
        tdf3.setOrdinal(0);

        TripNode tc2 = new TripNode("Root Trip", user);

        tc2.save();


        tc.setParent(tc2);
        tc.save();

        tdf3.setParent(tc2);
        tdf3.save();


        List<Node> tNodes = Node.find.query().where().eq("parent", tc2).findList();

        List<NodeDTO> nodeDTOS = new ArrayList<>();

        for (Node node : tNodes) {
            System.out.println(node.getClass());
            nodeDTOS.add(new NodeDTO(node));
        }

        return ok(Json.toJson(nodeDTOS));
    }
}
