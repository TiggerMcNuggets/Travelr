package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.User.*;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;
import repository.TripRepository;

import javax.inject.Inject;

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
    public CompletionStage<Result> getFilteredUsers(Http.Request request, String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities, List<String> travellerTypes, String orderBy) {
        return userRepository.getFilteredUsers(fname, lname, gender, minAge, maxAge, nationalities, travellerTypes, orderBy).thenApplyAsync(users -> {

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
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
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
                return notFound(APIResponses.TRAVELLER_NOT_FOUND);
            }

            User userGivenToken = request.attrs().get(Attrs.USER);
            Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);
            Object response;

            if (userGivenToken.id == id || isAdmin) {
                response = new GetOwnUserRes(user);
            } else {
                response = new GetUserRes(user);
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
     * @param userId the user id
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserGivenUser(Http.Request request, Long userId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        // Turns the post data into a form object
        Form<UpdateUserReq> userRequestForm = formFactory.form(UpdateUserReq.class).bindFromRequest(request);

        // Bad Request Check
        if (userRequestForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        // Create an object from the request
        UpdateUserReq req = userRequestForm.get();

        return userRepository.getUser(userId).thenComposeAsync(newUser -> {
            // Not Found Check
            if (newUser == null) {
                return CompletableFuture.completedFuture(notFound(APIResponses.TRAVELLER_NOT_FOUND));
            }
            return userRepository.updateUser(req, userId).thenApplyAsync(uid -> ok("Traveller Updated"));
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
                return notFound(APIResponses.TRAVELLER_NOT_FOUND);
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
