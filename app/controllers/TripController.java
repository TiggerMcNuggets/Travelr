package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import dto.trip.TripDTO;
import dto.shared.CreatedDTO;
import dto.trip.NodeDTO;

import models.*;
import net.fortuna.ical4j.model .Calendar;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import service.TripService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import utils.iCalCreator;


public class TripController extends Controller {

    @Inject
    FormFactory formFactory;
    private final TripService tripService;

    @Inject

    public TripController(TripService tripService) {

        this.tripService = tripService;
    }

    /**
     * Create a trip endpoint
     *
     * @param request
     * @return
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> createTrip(Http.Request request, Long userId) {

        Form<TripDTO> createTripForm = formFactory.form(TripDTO.class).bindFromRequest(request);

        if (createTripForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest());
        }

        User user = request.attrs().get(Attrs.ACCESS_USER);

        TripDTO dto = createTripForm.get();

        return tripService.createTrip(dto, user).thenApplyAsync((tripId) -> created(Json.toJson(new CreatedDTO(tripId))));
    }

    /**
     * update trip endpoint
     *
     * @param request
     * @return
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> updateTrip(Http.Request request, Long tripId, Long userId) {

        Form<TripDTO> createTripForm = formFactory.form(TripDTO.class).bindFromRequest(request);
        TripDTO dto = createTripForm.get();

        if (createTripForm.hasErrors()) {
            System.out.println(createTripForm.errors());
            return CompletableFuture.completedFuture(badRequest());
        }

        if (dto.getDestinations().size() < 2) {
            return CompletableFuture.completedFuture(badRequest());
        }

        User user = request.attrs().get(Attrs.USER);


        return tripService.updateTrip(tripId, dto, user).thenApplyAsync((trip) -> {
            if (trip == null) {
                return notFound();
            }

            return ok(Json.toJson(new TripDTO(trip)));
        });
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
        // TODO ADD AUTH CHECK
        return tripService.getTripsForUser(userId).thenApplyAsync(trips -> {
            ArrayList<TripDTO> tripDTOS = new ArrayList<>();

            for (Trip trip : trips) {
                tripDTOS.add(new TripDTO(trip));
            }

            return ok(Json.toJson(tripDTOS));
        });

    }

    /**
     * Get a trip by tripId
     *
     * @param request
     * @param tripId
     * @return
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> getTripById(Http.Request request, Long tripId, Long userId) {
        // TODO ADD AUTH CHECK
        return tripService.getTripById(tripId).thenApplyAsync(trip -> {
            if (trip == null) {
                return notFound();
            }
            return ok(Json.toJson(new TripDTO(trip)));
        });
    }


    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> deleteTrip(Http.Request request, Long tripId, Long userId) {
        return tripService.deleteTrip(tripId).thenApplyAsync(success -> {
            if (success) {
                return ok();
            } else {
                return badRequest();
            }
        });
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
        Trip trip = Trip.find.findByIdIncludeDeleted(tripId);

        if (trip == null) return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        return tripService.toggleTripDeleted(tripId).thenApplyAsync(deleted -> {

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();

            response.put("id", tripId);
            response.put("deleted", deleted);

            return ok(response.toString());
        });
    }



//    /**
//     * Gets a list of trips that belongs to a user
//     * @param request the http request
//     * @return 200 with list of trips if all ok
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> getUserTrips(Http.Request request) {
//        User user = request.attrs().get(Attrs.USER);
//        return tripRepository.getTrips(user.id).thenApplyAsync(users -> ok(Ebean.json().toJson(users)));
//    }
//
//    //CREATE TRIP METHOD FOR WHEN ADMIN IS NOT TAKEN INTO ACCOUNT
//    /**
//     * Creates a trip for the user
//     * @param request the http request
//     * @return 201 with json object of new trip id in it if all ok
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> createTrip(Http.Request request) {
//        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
//        User user = request.attrs().get(Attrs.USER);
//
//        // Bad Request check
//        if (createTripForm.hasErrors()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
//        }
//
//        CreateTripReq req = createTripForm.get();
//
//        // Less than two destinations check
//        if (req.hasLessThanTwoDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
//        }
//
//        // Two same destinations in a row check
//        if (req.hasSameConsecutiveDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
//        }
//
//        return tripRepository.createTrip(req, user).thenApplyAsync(tripId -> {
//
//            CreateTripRes response = new CreateTripRes(tripId);
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonResponse = mapper.valueToTree(response);
//
//            return created(jsonResponse);
//        });
//    }
//
//
//    /**
//     * Creates a trip for a user
//     * @param request the http request
//     * @param userId the user's ID that new trip will be associated with
//     * @return 201 with json object of new trip id in it if all ok
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> createTripWithUser(Http.Request request, Long userId) {
//
//        // middleware stack
//        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
//        if (middlewareRes != null) return middlewareRes;
//
//        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
//        //user that we want to create trip for
//        User user = User.find.findById(userId); //never null as checked by middleware userIdRequiredMiddlewareStack above
//
//        // Bad Request check
//        if (createTripForm.hasErrors()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
//        }
//
//        CreateTripReq req = createTripForm.get();
//
//        // Less than two destinations check
//        if (req.hasLessThanTwoDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
//        }
//
//        // Two same destinations in a row check
//        if (req.hasSameConsecutiveDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
//        }
//
//        return tripRepository.createTrip(req, user).thenApplyAsync(tripId -> {
//
//            CreateTripRes response = new CreateTripRes(tripId);
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonResponse = mapper.valueToTree(response);
//
//            return created(jsonResponse);
//        });
//    }
//
//    /**
//     * Gets a single trip that belongs to a user and matches the given id
//     * @param request the http request
//     * @param id the trip id
//     * @return 200 with trip if all ok
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> getUserTrip(Http.Request request, Long id) {
//        User user = request.attrs().get(Attrs.USER);
//        return tripRepository.getTrip(id).thenApplyAsync(trip -> {
//            // Not Found Check
//            if (trip == null) {
//                return notFound(APIResponses.TRIP_NOT_FOUND);
//            }
//
//            // Forbidden Check
//            if (trip.user.id != user.id) {
//                return forbidden("Forbidden: Access Denied");
//            }
//            //Hacky work around to get the arrival and departure dates returning
//            for (TripDestination dest: trip.destinations) {
//                dest.getArrivalDate();
//            }
//
//            GetTripRes response = new GetTripRes(trip);
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonResponse = mapper.valueToTree(response);
//
//            return ok(jsonResponse);
//        });
//    }
//
//
//
//    /**
//     * Gets a single trip that belongs to a user and matches the given id
//     * @param request the http request
//     * @param userId the user id
//     * @param tripId the trip id
//     * @return 200: ok, 403: user not an admin or logged in user, 404: user or trip not found
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> getUserTripWhenUserGiven(Http.Request request, Long userId, Long tripId) {
//
//
//        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
//        if (middlewareRes != null) return middlewareRes;
//        // Not Found Check
//        CompletionStage<Result> tripExists = Authorization.doesTripExist(tripId);
//        if (tripExists != null) return tripExists;
//        // User allowed to see trip
//        Trip trip = Trip.find.findOne(tripId);
//        CompletionStage<Result> authorisedToView = Authorization.isUserAuthorisedToViewTrip(request, userId, trip.user.id);
//        if (authorisedToView != null) return authorisedToView;
//
//        //Hacky work around to get the arrival and departure dates returning
//        for (TripDestination dest: trip.destinations) {
//            dest.getArrivalDate();
//        }
//        GetTripRes response = new GetTripRes(trip);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonResponse = mapper.valueToTree(response);
//
//        return CompletableFuture.completedFuture(ok(jsonResponse));
//
//    }
//
//    /**
//     * Gets a users trips by given id
//     * @param id the user id
//     * @return 200 if item exists
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> getTrips(Http.Request request, Long id) {
//        CompletionStage<Result> middlewareRes = Authorization.doesUserByIdExist(id);
//        if (middlewareRes != null) return middlewareRes;
//
//        return tripRepository.getTrips(id).thenApplyAsync(trips -> {
//            ArrayList<GetTripRes> correctTrips = new ArrayList<>();
//            for (Trip trip : trips) {
//                GetTripRes tripRes = new GetTripRes(trip);
//                List<TripDestination> correctDests = new ArrayList<>();
//                //Setting the blank name to the correct destination name
//                for (TripDestination dest : trip.destinations) {
//                    dest.name = dest.destination.getName();
//                    correctDests.add(dest);
//                }
//                tripRes.setDestinations(correctDests);
//                correctTrips.add(tripRes);
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonResponse = mapper.valueToTree(correctTrips);
//            return ok(jsonResponse);
//        });
//    }
//
//    /**
//     * Updates a trip that belongs to a user with the given id
//     * @param request the http request
//     * @param id the trip id
//     * @return 200 with string if all ok
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> updateUserTrip(Http.Request request, Long id) {
//        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
//        User user = request.attrs().get(Attrs.USER);
//
//        // Bad Request check
//        if (createTripForm.hasErrors()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
//        }
//
//        CreateTripReq req = createTripForm.get();
//
//        // Less than two destinations check
//        if (req.hasLessThanTwoDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
//        }
//
//        // Two same destinations in a row check
//        if (req.hasSameConsecutiveDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
//        }
//
//        return tripRepository.getTrip(id).thenComposeAsync(trip -> {
//            // Not Found Check
//            if (trip == null) {
//                return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
//            }
//
//            // Forbidden Check
//            if (trip.user.id != user.id) {
//                return CompletableFuture.completedFuture(forbidden("Forbidden: Access Denied"));
//            }
//
//            trip.name = req.name;
//            trip.destinations.clear();
//            return tripRepository.updateTrip(req, trip).thenApplyAsync(tripId -> ok("Trip updated"));
//        });
//    }
//
//
//    /**
//     * Updates a trip that belongs to a user with the given id
//     * @param request the http request
//     * @param userId the userID
//     * @param id the trip id
//     * @return 200 with string if all ok
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> updateUserTripGivenUser(Http.Request request, Long id, Long userId) {
//
//        // middleware stack
//        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
//        if (middlewareRes != null) return middlewareRes;
//
//        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
//
//        // Bad Request check
//        if (createTripForm.hasErrors()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
//        }
//
//        CreateTripReq req = createTripForm.get();
//
//        // Less than two destinations check
//        if (req.hasLessThanTwoDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
//        }
//
//        // Two same destinations in a row check
//        if (req.hasSameConsecutiveDestinations()) {
//            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
//        }
//
//        return tripRepository.getTrip(id).thenComposeAsync(trip -> {
//            // Not Found Check
//            if (trip == null) {
//                return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
//            }
//
//
//            trip.name = req.name;
//            trip.destinations.clear();
//            return tripRepository.updateTrip(req, trip).thenApplyAsync(tripId -> ok("Trip updated"));
//        });
//    }
//
//    /**
//     * Soft Deletes a trip
//     * @param req the http request
//     * @param userId the id of the user
//     * @param tripId the id of the destination
//     */
//    @Authorization.RequireAuth
//    public CompletionStage<Result> softDeleteTrip(Http.Request req, Long userId, Long tripId) {
//        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(req, userId);
//
//        if (middlewareRes != null) return middlewareRes;
//        Trip trip = Trip.find.findByIdIncludeDeleted(tripId);
//
//        if (trip == null) return  CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
//        return tripRepository.toggleTripDeleted(tripId).thenApplyAsync(deleted -> {
//
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode response = mapper.createObjectNode();
//
//            response.put("id", tripId);
//            response.put("deleted", deleted);
//
//            return ok(response.toString());
//        });
//

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

        Trip trip = Trip.find.byId(tripId);

        if (trip == null) return  CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));

        iCalCreator creator = new iCalCreator();
        Calendar iCalString = creator.createCalendarFromTrip(trip);
        try {
            File tempFile = File.createTempFile(trip.name, ".ics");
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write(iCalString.toString());
            bw.close();
            return CompletableFuture.completedFuture(ok(tempFile));
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(internalServerError());
        }
    }

    public Result test() throws Exception {
//        User user = new User("first", "last", "emaill@email.email", 1);
//        user.insert();

        User user = User.find.findById(1L);

        Destination dest1 = new Destination("name", 1.0, 1.0, "type", "district", "country", user);
        dest1.insert();

        Destination dest2 = new Destination("second", 2.0, 2.0, "type2", "district2", "country2", user);
        dest2.insert();

        DestinationNode tdf1 = new DestinationNode("sdfsdf", user, dest1);
        DestinationNode tdf2 = new DestinationNode("abcabc", user, dest2);

        TripNode tc = new TripNode("DFLKDJ", user);
        tc.add(tdf1);
        tc.add(tdf2);


        Destination dest3 = new Destination("third", 3.0, 3.0, "type3", "district3", "country3", user);
        dest3.insert();

        DestinationNode tdf3 = new DestinationNode("eifsjdf", user, dest3);

        TripNode tc2 = new TripNode("erer", user);
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


//        TripNode trip= TripNode.find.byId(tc2.getId());
//        System.out.println(trip.getNodes().size());
//        for (Node tripNode : trip.getNodes()) {
//            System.out.println(tripNode.getName());
//        }

        return ok(Json.toJson(nodeDTOS));
    }
}
