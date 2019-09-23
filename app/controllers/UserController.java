package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonElement;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.User.*;
import models.SlackUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;
import service.MailgunService;
import service.SlackService;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class UserController extends Controller {

    @Inject
    FormFactory formFactory;

    private UserRepository userRepository;
    private final MailgunService mailgunService;
    private final SlackService slackService;


    @Inject
    public UserController(UserRepository userRepository, MailgunService mailgunService, SlackService slackService) {
        this.userRepository = userRepository;
        this.mailgunService = mailgunService;
        this.slackService = slackService;

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
     * Creates a new user and sends a welcome email when successfully created.
     * @param request the http request
     * @return 201 with json object of new user id if all ok
     */
    public CompletionStage<Result> createUser(Http.Request request) {
        // Turns the post data into a form object
        Form<CreateUserReq> userRequestForm = formFactory.form(CreateUserReq.class).bindFromRequest(request);

        // Bad Request Check
        if (userRequestForm.hasErrors()) {
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
            mailgunService.sendWelcomeEmail(User.find.findByEmail(req.email));

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
     * Toggles a user's deletion status
     * @param request the http request
     * @param userId the user id
     * @return 200 with user id if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> toggleUserDeleted(Http.Request request, Long userId) {
        return userRepository.getUserIncludeDeleted(userId).thenComposeAsync(user -> {

            // Not Found Check
            if (user == null) {
                return CompletableFuture.completedFuture(notFound(APIResponses.TRAVELLER_NOT_FOUND));
            }

            // Forbidden Checks
            User userGivenToken = request.attrs().get(Attrs.USER);

            if (userGivenToken.id == user.id) {
                return CompletableFuture.completedFuture(forbidden("Cannot delete yourself"));
            }

            if (userGivenToken.accountType == 0) {
                return CompletableFuture.completedFuture(forbidden("Not an admin"));
            }

            if (user.accountType == 2) {
                return CompletableFuture.completedFuture(forbidden("You cannot delete a master admin"));
            }

            return userRepository.toggleUserDeleted(userId).thenApplyAsync(deleted -> {

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode response = mapper.createObjectNode();

                response.put("id", userId);
                response.put("deleted", deleted);

                return ok(response.toString());
            });
        });
    }

    /**
     * Takes a request and checks it for errors. If the access token request is fine, execute it with the Slack service.
     * Once a valid authorisation grant is returned, create a new slackUser object and store this in the database.
     * @param request the http request
     * @param userId the user's id
     * @return 200 if the request is executed
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> slackRequestAuth(Http.Request request, Long userId) {
        // TODO: Add slack logic
        Optional<String> code = Optional.ofNullable(request.body().asJson().get("code").asText());
        if (!code.isPresent()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.SLACK_MISSING_CODE));
        }

        return slackService.requestAccessToken(code.get(), userId).thenApplyAsync(resHandler -> {
            Optional<User> user = Optional.ofNullable(User.find.findById(userId));
            Optional<JsonElement> accessTokenJson = Optional.ofNullable(resHandler.getBody().get("access_token"));
            if (!user.isPresent()) {
                return badRequest(APIResponses.USER_NOT_FOUND);
            }

            if (!accessTokenJson.isPresent()) {
                return badRequest(APIResponses.SLACK_MISSING_ACCESS_TOKEN);
            }

            Optional<SlackUser> slackUser = Optional.ofNullable(SlackUser.find.findByUserId(user.get().getId()));

            if (!slackUser.isPresent()) {
                // create a new slack user in the table
                SlackUser newSlackUser = new SlackUser(user.get(), accessTokenJson.get().getAsString());
                newSlackUser.insert();
            } else {
                // update access token in the table
                SlackUser existingSlackUser = slackUser.get();
                existingSlackUser.setAccessToken(accessTokenJson.get().getAsString());
                existingSlackUser.update();
            }

            return ok(APIResponses.SLACK_TOKEN_SAVED);
        });
    }

    /**
     *
     * @param request the http request
     * @param userId the user's id
     * @return 200 if the request is executed
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> slackCreatePrivateChannel(Http.Request request, Long userId) {
        SlackUser groupOwner = SlackUser.find.findByUserId(userId);
        if (groupOwner == null) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.SLACK_USER_NOT_FOUND));
        }

        Optional<String> channelName = Optional.ofNullable(request.body().asJson().get("channelName").asText());
        if (!channelName.isPresent()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.SLACK_CHANNEL_MALFORMED_REQUEST));
        }

        /* Private channel names can only contain lowercase letters, numbers,
        hyphens, and underscores, and must be 80 characters or less. Slack will return specific errors
        if this is given */

        // Convert all spaces to '-'.
        String dashedChannelName = channelName.get().replaceAll(" ", "-").toLowerCase();

        // Remove special characters
        String sanitizedChannelName = dashedChannelName.replaceAll("[^a-zA-Z0-9\\-\\_]+","");

        return slackService.requestPrivateChannel(groupOwner, sanitizedChannelName).thenApplyAsync(resHandler -> {
            Optional<User> user = Optional.ofNullable(User.find.findById(userId));
            if (!user.isPresent()) {
                return badRequest(APIResponses.USER_NOT_FOUND);
            }

            Optional<JsonElement> success = Optional.ofNullable(resHandler.getBody().get("ok"));
            Optional<JsonElement> slackChannelName = Optional.ofNullable(resHandler.getBody().get("channel").getAsJsonObject().get("name"));

            if (!success.isPresent() || !success.get().getAsBoolean() || !slackChannelName.isPresent()) {
                return badRequest(APIResponses.SLACK_CHANNEL_CREATION_FAILURE);
            }
// abstract this away
//            slackService.requestServerInfo(groupOwner).thenApplyAsync(response -> {
//                Optional<JsonElement> domain = Optional.ofNullable(resHandler.getBody().get("team").getAsJsonObject().get("domain"));
//                if (!domain.isPresent()) {
//                    return badRequest(APIResponses.SLACK_CHANNEL_CREATION_FAILURE);
//                }
//                mailgunService.sendSlackChannelEmail(user.get(), slackChannelName.get().getAsString(), domain.get().getAsString());
//                return ok(APIResponses.SLACK_CHANNEL_CREATED);
//            });

            return badRequest(APIResponses.SLACK_CHANNEL_CREATION_FAILURE);

        });
    }
}
