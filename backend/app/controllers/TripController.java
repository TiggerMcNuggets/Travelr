package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.dto.Trip.CreateTripReq;
import controllers.dto.Trip.CreateTripRes;
import controllers.dto.Trip.GetTripRes;
import controllers.dto.Trip.TripDestinationRes;
import io.ebean.Ebean;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TripRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


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
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        CreateTripReq req = createTripForm.get();

        // Less than two destinations check
        if (req.hasLessThanTwoDestinations()) {
            return CompletableFuture.completedFuture(badRequest("Less than two destinations"));
        }

        // Two same destinations in a row check
        if (req.hasSameConsecutiveDestinations()) {
            return CompletableFuture.completedFuture(badRequest("Two same destinations in a row"));
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
                return notFound("Trip not found");
            }

            // Forbidden Check
            if (trip.user.id != user.id) {
                return forbidden("Forbidden: Access Denied");
            }

            List<TripDestinationRes> destinations = new ArrayList<TripDestinationRes>();

            GetTripRes response = new GetTripRes(trip);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

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
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        CreateTripReq req = createTripForm.get();

        // Less than two destinations check
        if (req.hasLessThanTwoDestinations()) {
            return CompletableFuture.completedFuture(badRequest("Less than two destinations"));
        }

        // Two same destinations in a row check
        if (req.hasSameConsecutiveDestinations()) {
            return CompletableFuture.completedFuture(badRequest("Two same destinations in a row"));
        }

        return tripRepository.getTrip(id).thenComposeAsync(trip -> {
            // Not Found Check
            if (trip == null) {
                return CompletableFuture.completedFuture(notFound("Trip not found"));
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
}
