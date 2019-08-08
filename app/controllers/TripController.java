package controllers;


import com.google.inject.Inject;

import controllers.actions.Authorization;
import dto.trip.*;

import models.*;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import service.TripService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;


public class TripController extends Controller {

    @Inject
    FormFactory formFactory;
    private final TripService tripService;

    @Inject

    public TripController(TripService tripService) {

        this.tripService = tripService;
    }
//
//    /**
//     * Create a trip endpoint
//     *
//     * @param request
//     * @return
//     */
//    @Authorization.RequireAuthOrAdmin
//    public CompletionStage<Result> createTrip(Http.Request request, Long userId) {
//
//        Form<CreateTripDTO> createTripForm = formFactory.form(CreateTripDTO.class).bindFromRequest(request);
//
//        if (createTripForm.hasErrors()) {
//            return CompletableFuture.completedFuture(badRequest());
//        }
//
//        User user = request.attrs().get(Attrs.ACCESS_USER);
//
//        CreateTripDTO dto = createTripForm.get();
//
//        return tripService.createTrip(dto, user).thenApplyAsync((tripId) -> created(Json.toJson(new CreatedDTO(tripId))));
//    }
//
//    /**
//     * update trip endpoint
//     *
//     * @param request
//     * @return
//     */
//    @Authorization.RequireAuthOrAdmin
//    public CompletionStage<Result> updateTrip(Http.Request request, Long tripId, Long userId) {
//
//        Form<TripDTO> createTripForm = formFactory.form(TripDTO.class).bindFromRequest(request);
//        TripDTO dto = createTripForm.get();
//
//        if (createTripForm.hasErrors()) {
//            System.out.println(createTripForm.errors());
//            return CompletableFuture.completedFuture(badRequest());
//        }
//
//        if (dto.getDestinations().size() < 2) {
//            return CompletableFuture.completedFuture(badRequest());
//        }
//
//        User user = request.attrs().get(Attrs.USER);
//
//
//        return tripService.updateTrip(tripId, dto, user).thenApplyAsync((trip) -> {
//            if (trip == null) {
//                return notFound();
//            }
//
//            return ok(Json.toJson(new TripDTO(trip)));
//        });
//    }
//
//    /**
//     * Get all trips for a given user
//     *
//     * @param request
//     * @param userId
//     * @return
//     */
//    @Authorization.RequireAuthOrAdmin
//    public CompletionStage<Result> getUserTrips(Http.Request request, Long userId) {
//        // TODO ADD AUTH CHECK
//        return tripService.getTripsForUser(userId).thenApplyAsync(trips -> {
//            ArrayList<TripDTO> tripDTOS = new ArrayList<>();
//
//            for (Trip trip : trips) {
//                tripDTOS.add(new TripDTO(trip));
//            }
//
//            return ok(Json.toJson(tripDTOS));
//        });
//
//    }
//
//    /**
//     * Get a trip by tripId
//     *
//     * @param request
//     * @param tripId
//     * @return
//     */
//    @Authorization.RequireAuthOrAdmin
//    public CompletionStage<Result> getTripById(Http.Request request, Long tripId, Long userId) {
//        // TODO ADD AUTH CHECK
//        return tripService.getTripById(tripId).thenApplyAsync(trip -> {
//            if (trip == null) {
//                return notFound();
//            }
//            return ok(Json.toJson(new TripDTO(trip)));
//        });
//    }
//
//
//    @Authorization.RequireAuthOrAdmin
//    public CompletionStage<Result> deleteTrip(Http.Request request, Long tripId, Long userId) {
//        return tripService.deleteTrip(tripId).thenApplyAsync(success -> {
//            if (success) {
//                return ok();
//            } else {
//                return badRequest();
//            }
//        });
//    }
//
//    /**
//     * //     * Soft Deletes a trip
//     * //     * @param req the http request
//     * //     * @param userId the id of the user
//     * //     * @param tripId the id of the destination
//     * //
//     */
//    @Authorization.RequireAuthOrAdmin
//    public CompletionStage<Result> softDeleteTrip(Http.Request request, Long tripId, Long userId) {
//        Trip trip = Trip.find.findByIdIncludeDeleted(tripId);
//
//        if (trip == null) return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
//        return tripService.toggleTripDeleted(tripId).thenApplyAsync(deleted -> {
//
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode response = mapper.createObjectNode();
//
//            response.put("id", tripId);
//            response.put("deleted", deleted);
//
//            return ok(response.toString());
//        });
//    }
//
//
//    /**
//     * Creates a .ics file to return to the user with the trip
//     * @param req the http request
//     * @param userId the id of the user
//     * @param tripId the id of the trip
//     * @return The .ics file generated for the trip
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> getUserTripAsICalFile(Http.Request req, Long userId, Long tripId){
//
//        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(req, userId);
//
//        if (middlewareRes != null) return middlewareRes;
//
//        Trip trip = Trip.find.byId(tripId);
//
//        if (trip == null) return  CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
//
//        iCalCreator creator = new iCalCreator();
//        Calendar iCalString = creator.createCalendarFromTrip(trip);
//        try {
//            File tempFile = File.createTempFile(trip.name, ".ics");
//            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
//            bw.write(iCalString.toString());
//            bw.close();
//            return CompletableFuture.completedFuture(ok(tempFile));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return CompletableFuture.completedFuture(internalServerError());
//        }
//    }

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

    public CompletionStage<Result> fetchTrip(Http.Request request, Long tripId, Long userId) {

        CompletionStage<TripNode> tripStage = tripService.getTripById(tripId);

        CompletionStage<List<Node>> childrenStage = tripService.getChildByTripId(tripId);

        CompletionStage<List<TripNode>> navigationStage = tripService.getNavigationForTrip(tripId);

        CompletionStage<GetTripDTO> tripDtoStage = tripStage.thenCombineAsync(childrenStage, (trip, children) -> {

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
        });
    }



    public Result test() throws Exception {

        User user = User.find.findById(1L);

        Destination dest1 = new Destination("Destination 1", 1.0, 1.0, "type", "district", "country", user);
        dest1.insert();

        Destination dest2 = new Destination("Destination 2", 2.0, 2.0, "type2", "district2", "country2", user);
        dest2.insert();

        DestinationNode tdf1 = new DestinationNode("Custom Destination 1 Name", user, dest1);
        DestinationNode tdf2 = new DestinationNode("Custom Destination 2 Name", user, dest2);

        TripNode tc = new TripNode("Level 1 Inner Trip", user);
        tc.add(tdf1);
        tc.add(tdf2);


        Destination dest3 = new Destination("Destination 3", 3.0, 3.0, "type3", "district3", "country3", user);
        dest3.insert();

        DestinationNode tdf3 = new DestinationNode("Custom Destination 3 Name", user, dest3);

        TripNode tc2 = new TripNode("Root Trip", user);
        tc2.add(tc);
        tc2.add(tdf3);
        tc2.save();
        tc.save();

        List<Node> tNodes = Node.find.query().where().eq("parent", tc2).findList();

        List<NodeDTO> nodeDTOS = new ArrayList<>();

        for (Node node : tNodes) {
            System.out.println(node.getClass());
            nodeDTOS.add(new NodeDTO(node));
        }

        return ok(Json.toJson(nodeDTOS));
    }
}
