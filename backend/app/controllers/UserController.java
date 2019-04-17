package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.dto.Trip.GetTripRes;
import controllers.dto.Trip.TripDestinationRes;
import controllers.dto.User.*;
import models.Destination;
import models.Trip;
import models.TripDestination;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;
import repository.TripRepository;
import utils.FileHelper;

import javax.inject.Inject;

import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class UserController extends Controller {

    @Inject
    FormFactory formFactory;

    private UserRepository userRepository;

    private TripRepository tripRepository;

    @Inject
    public UserController(UserRepository userRepository, TripRepository tripRepository) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }

    /**
     * Gets a list of users
     * @param request the http request
     * @return 200 with list of users if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUsers(Http.Request request) {
        return userRepository.getAllUsers().thenApplyAsync(users -> {

            GetUsersRes response = new GetUsersRes(users);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response.getGetUserRes());

            return ok(jsonResponse);
        });
    }

    /**
     * Gets a list of users
     * @param request the http request
     * @return 200 with list of users if all ok
     */
//    @Authorization.RequireAuth
    public CompletionStage<Result> getFilteredUsers(Http.Request request, String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities, List<String> traveller_types, String orderBy) {
        return userRepository.getFilteredUsers(fname, lname, gender, minAge, maxAge, nationalities, traveller_types, orderBy).thenApplyAsync(users -> {

            GetUsersRes response = new GetUsersRes(users);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response.getGetUserRes());

            return ok(jsonResponse);
        });
    }

    /**
     * Creates a new user
     * @param request the http request
     * @return 201 with json object of new user id if all ok
     */
    public CompletionStage<Result> createUser(Http.Request request) {
        // Turns the post data into a form object
        Form<CreateUserReq> userRequestForm = formFactory.form(CreateUserReq.class).bindFromRequest(request);

        // Bad Request Check
        if (userRequestForm.hasErrors()) {
            System.out.println(userRequestForm.errors());
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        CreateUserReq req = userRequestForm.get();

        // Email Taken Check
        return userRepository.getUserByEmail(req.email).thenComposeAsync(user -> {
            if (user != null) {
                return CompletableFuture.completedFuture(null);
            } else {
                return userRepository.createNewUser(req);
            }
        }).thenApplyAsync(id -> {
            if (id == null) {
                return badRequest("Email is already taken");
            }

            CreateUserRes response = new CreateUserRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

            return created(jsonResponse);
        });
    }

    /**
     * Gets a user by given id
     * @param request the http request
     * @param id the user id
     * @return 200 with user if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUser(Http.Request request, Long id) {
        return userRepository.getUser(id).thenApplyAsync(user -> {

            // Not Found Check
            if (user == null) {
                return notFound("Traveller not found");
            }

            boolean isSameUser = true;
            User userGivenToken = request.attrs().get(Attrs.USER);
            Object response;

            if (userGivenToken.id != id) {
                response = new GetUserRes(user);
            } else {
                response = new GetOwnUserRes(user);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

            return ok(jsonResponse);
        });
    }

    /**
     * Gets current user according to their auth token
     * @param request the http request
     * @return 200 with user if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getMe(Http.Request request) {
        User user = request.attrs().get(Attrs.USER);
        return getUser(request, user.id);
    }

    /**
     * Update a user that matches header and id
     * @param request the http request
     * @param id the user id
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUser(Http.Request request, Long id) {

        // Turns the post data into a form object
        Form<UpdateUserReq> userRequestForm = formFactory.form(UpdateUserReq.class).bindFromRequest(request);

        // Bad Request Check
        if (userRequestForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        // Forbidden Check
        User user = request.attrs().get(Attrs.USER);
        if (user.id != id) {
            return CompletableFuture.completedFuture(forbidden("Forbidden: Access Denied"));
        }

        // Create an object from the request
        UpdateUserReq req = userRequestForm.get();

        return userRepository.getUser(id).thenComposeAsync(newUser -> {
            // Not Found Check
            if (newUser == null) {
                return CompletableFuture.completedFuture(notFound("Traveller not found"));
            }
            return userRepository.updateUser(req, id).thenApplyAsync(uid -> ok("Traveller Updated"));
        });
    }

    /**
     * Gets a users trips by given id
     * @param id the user id
     * @return 200 if item exists
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getTrips(Long id) {
        System.out.println(userRepository.getUser(id));
        return userRepository.getUser(id).thenApplyAsync(user -> {
            if (user == null) {
                return notFound("Traveller not found");
            } else {
                return tripRepository.getTrips(id).thenApplyAsync(trips -> {
                    ArrayList<GetTripRes> correctTrips = new ArrayList<GetTripRes>();
                    //Turning the trip into trip response types
                    for (Trip trip : trips) {
                        GetTripRes tripRes = new GetTripRes(trip);
                        List<TripDestination> correctDests = new ArrayList<TripDestination>();
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
        });
    }

    /**
     * Deletes a user by given id
     * @param request the http request
     * @param id the user id
     * @return 200 with user if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> deleteUser(Http.Request request, Long id) {
        return userRepository.getUser(id).thenApplyAsync(user -> {

            // Not Found Check
            if (user == null) {
                return notFound("Traveller not found");
            }

            // Forbidden Check
            User userGiveToken = request.attrs().get(Attrs.USER);
            if (userGiveToken.id != id && userGiveToken.accountType == 0) {
                return forbidden("Forbidden: you have too low privileges and you are not the account owner");
            }

            if (user.accountType == 2) {
                return forbidden("You cannot delete a master admin");
            } else {
                user.delete();
                return ok("Deleted user with user id: " + id);
            }
        });
    }

    public Result index() {
        return ok("Travel EA - Home");
    }



}
