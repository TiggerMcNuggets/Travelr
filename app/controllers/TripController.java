package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.trip.CreateTripReq;
import controllers.dto.trip.CreateTripRes;
import controllers.dto.trip.GetTripRes;
import io.ebean.Ebean;

import models.Trip;
import models.TripDestination;
import models.User;
import net.fortuna.ical4j.model.Calendar;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TripRepository;

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

    private final TripRepository tripRepository;

    @Inject

    public TripController(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * Gets a list of trips that belongs to a user
     * @param request the http request
     * @return 200 with list of trips if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserTrips(Http.Request request) {
        User user = request.attrs().get(Attrs.USER);
        return tripRepository.getTrips(user.id).thenApplyAsync(users -> ok(Ebean.json().toJson(users)));
    }

    //CREATE TRIP METHOD FOR WHEN ADMIN IS NOT TAKEN INTO ACCOUNT
    /**
     * Creates a trip for the user
     * @param request the http request
     * @return 201 with json object of new trip id in it if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> createTrip(Http.Request request) {
        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
        User user = request.attrs().get(Attrs.USER);

        // Bad Request check
        if (createTripForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateTripReq req = createTripForm.get();

        // Less than two destinations check
        if (req.hasLessThanTwoDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
        }

        // Two same destinations in a row check
        if (req.hasSameConsecutiveDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
        }

        return tripRepository.createTrip(req, user).thenApplyAsync(tripId -> {

            CreateTripRes response = new CreateTripRes(tripId);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

            return created(jsonResponse);
        });
    }


    /**
     * Creates a trip for a user
     * @param request the http request
     * @param userId the user's ID that new trip will be associated with
     * @return 201 with json object of new trip id in it if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> createTripWithUser(Http.Request request, Long userId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
        //user that we want to create trip for
        User user = User.find.findById(userId); //never null as checked by middleware userIdRequiredMiddlewareStack above

        // Bad Request check
        if (createTripForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateTripReq req = createTripForm.get();

        // Less than two destinations check
        if (req.hasLessThanTwoDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
        }

        // Two same destinations in a row check
        if (req.hasSameConsecutiveDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
        }

        return tripRepository.createTrip(req, user).thenApplyAsync(tripId -> {

            CreateTripRes response = new CreateTripRes(tripId);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

            return created(jsonResponse);
        });
    }

    /**
     * Gets a single trip that belongs to a user and matches the given id
     * @param request the http request
     * @param id the trip id
     * @return 200 with trip if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserTrip(Http.Request request, Long id) {
        User user = request.attrs().get(Attrs.USER);
        return tripRepository.getTrip(id).thenApplyAsync(trip -> {
            // Not Found Check
            if (trip == null) {
                return notFound(APIResponses.TRIP_NOT_FOUND);
            }

            // Forbidden Check
            if (trip.user.id != user.id) {
                return forbidden("Forbidden: Access Denied");
            }
            //Hacky work around to get the arrival and departure dates returning
            for (TripDestination dest: trip.destinations) {
                dest.getArrivalDate();
            }

            GetTripRes response = new GetTripRes(trip);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

            return ok(jsonResponse);
        });
    }



    /**
     * Gets a single trip that belongs to a user and matches the given id
     * @param request the http request
     * @param userId the user id
     * @param tripId the trip id
     * @return 200: ok, 403: user not an admin or logged in user, 404: user or trip not found
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserTripWhenUserGiven(Http.Request request, Long userId, Long tripId) {


        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;
        // Not Found Check
        CompletionStage<Result> tripExists = Authorization.doesTripExist(tripId);
        if (tripExists != null) return tripExists;
        // User allowed to see trip
        Trip trip = Trip.find.findOne(tripId);
        CompletionStage<Result> authorisedToView = Authorization.isUserAuthorisedToViewTrip(request, userId, trip.user.id);
        if (authorisedToView != null) return authorisedToView;

        //Hacky work around to get the arrival and departure dates returning
        for (TripDestination dest: trip.destinations) {
            dest.getArrivalDate();
        }
        GetTripRes response = new GetTripRes(trip);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.valueToTree(response);

        return CompletableFuture.completedFuture(ok(jsonResponse));

    }

    /**
     * Gets a users trips by given id
     * @param id the user id
     * @return 200 if item exists
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getTrips(Http.Request request, Long id) {
        CompletionStage<Result> middlewareRes = Authorization.doesUserByIdExist(id);
        if (middlewareRes != null) return middlewareRes;

        return tripRepository.getTrips(id).thenApplyAsync(trips -> {
            ArrayList<GetTripRes> correctTrips = new ArrayList<>();
            for (Trip trip : trips) {
                GetTripRes tripRes = new GetTripRes(trip);
                List<TripDestination> correctDests = new ArrayList<>();
                //Setting the blank name to the correct destination name
                for (TripDestination dest : trip.destinations) {
                    dest.name = dest.destination.getName();
                    correctDests.add(dest);
                }
                tripRes.setDestinations(correctDests);
                correctTrips.add(tripRes);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(correctTrips);
            return ok(jsonResponse);
        });
    }

    /**
     * Updates a trip that belongs to a user with the given id
     * @param request the http request
     * @param id the trip id
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserTrip(Http.Request request, Long id) {
        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);
        User user = request.attrs().get(Attrs.USER);

        // Bad Request check
        if (createTripForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateTripReq req = createTripForm.get();

        // Less than two destinations check
        if (req.hasLessThanTwoDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
        }

        // Two same destinations in a row check
        if (req.hasSameConsecutiveDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
        }

        return tripRepository.getTrip(id).thenComposeAsync(trip -> {
            // Not Found Check
            if (trip == null) {
                return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
            }

            // Forbidden Check
            if (trip.user.id != user.id) {
                return CompletableFuture.completedFuture(forbidden("Forbidden: Access Denied"));
            }

            trip.name = req.name;
            trip.destinations.clear();
            return tripRepository.updateTrip(req, trip).thenApplyAsync(tripId -> ok("Trip updated"));
        });
    }


    /**
     * Updates a trip that belongs to a user with the given id
     * @param request the http request
     * @param userId the userID
     * @param id the trip id
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserTripGivenUser(Http.Request request, Long id, Long userId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        Form<CreateTripReq> createTripForm = formFactory.form(CreateTripReq.class).bindFromRequest(request);

        // Bad Request check
        if (createTripForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        CreateTripReq req = createTripForm.get();

        // Less than two destinations check
        if (req.hasLessThanTwoDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.LESS_THAN_TWO_DESTINATIONS));
        }

        // Two same destinations in a row check
        if (req.hasSameConsecutiveDestinations()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.TWO_SAME_DESTINATIONS_IN_A_ROW));
        }

        return tripRepository.getTrip(id).thenComposeAsync(trip -> {
            // Not Found Check
            if (trip == null) {
                return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
            }


            trip.name = req.name;
            trip.destinations.clear();
            return tripRepository.updateTrip(req, trip).thenApplyAsync(tripId -> ok("Trip updated"));
        });
    }

    /**
     * Soft Deletes a trip
     * @param req the http request
     * @param userId the id of the user
     * @param tripId the id of the destination
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> softDeleteTrip(Http.Request req, Long userId, Long tripId) {
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(req, userId);

        if (middlewareRes != null) return middlewareRes;
        Trip trip = Trip.find.findByIdIncludeDeleted(tripId);

        if (trip == null) return  CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        return tripRepository.toggleTripDeleted(tripId).thenApplyAsync(deleted -> {

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();

            response.put("id", tripId);
            response.put("deleted", deleted);

            return ok(response.toString());
        });

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

        Trip trip = Trip.find.findOne(tripId);

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
}
