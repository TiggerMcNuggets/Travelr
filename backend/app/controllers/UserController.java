package controllers;

import akka.dispatch.sysmsg.Create;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import io.ebean.Ebean;
import models.Nationality;
import models.User;
import play.core.j.HttpExecutionContext;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.validation.Constraint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class UserController extends Controller {

    @Inject
    FormFactory formFactory;

    private UserRepository userRepository;

    @Inject
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public CompletionStage<Result> getAllUsers(Http.Request request) {
        return userRepository.getAllUsers().thenApplyAsync(users -> {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userList = mapper.createArrayNode();

            try {
                userList = mapper.readTree(Ebean.json().toJson(users));
            } catch (IOException e) {
                return internalServerError();
            }

            // Format json to remove unnecessary fields
            for (JsonNode user: userList) {
                for (JsonNode nationality: user.get("nationalities")) {
                    ((ObjectNode)nationality).put("id", nationality.get("nationality").get("id").asInt());
                    ((ObjectNode)nationality).put("name", nationality.get("nationality").get("name").asText());
                    ((ObjectNode)nationality).remove("nationality");
                }
            }

            return ok(userList);
        });
    }

    public CompletionStage<Result> addUser(Http.Request request) {

        // Turns the post data into a form object
        Form<CreateUserRequest> userRequestForm = formFactory.form(CreateUserRequest.class).bindFromRequest(request);

        // Bad Request Check
        if (userRequestForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Bad Request"));
        }

        // Create an object from the request
        CreateUserRequest req = userRequestForm.get();

        // Call add user
        return userRepository.createNewUser(req).thenApplyAsync(id -> {
            ObjectNode userResponse = Json.newObject();
            userResponse.put("id", id);
            return created(userResponse);
        });
    }

    public CompletionStage<Result> getUser(Http.Request request, Long id) {
        return userRepository.getUser(id).thenApplyAsync(user -> {

            // Not Found Check
            if (user == null) {
                return notFound("Traveller not found");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode userNode;

            try {
                userNode = mapper.readTree(Ebean.json().toJson(user));
            } catch (IOException e) {
                return internalServerError();
            }

            // Format json to remove unnecessary fields
            for (JsonNode nationality: userNode.get("nationalities")) {
                ((ObjectNode)nationality).put("id", nationality.get("nationality").get("id").asLong());
                ((ObjectNode)nationality).put("name", nationality.get("nationality").get("name").asText());
                ((ObjectNode)nationality).remove("nationality");
            }

            return ok(userNode);
        });
    }

    @Authorization.RequireAuth
    public CompletionStage<Result> updateUser(Http.Request request, Long id) {

        // Turns the post data into a form object
        Form<UpdateUserRequest> userRequestForm = formFactory.form(UpdateUserRequest.class).bindFromRequest(request);

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
        UpdateUserRequest req = userRequestForm.get();

        return userRepository.getUser(id).thenComposeAsync(newUser -> {

            // Not Found Check
            if (newUser == null) {
                return null;
            } else {
                return userRepository.updateUser(req, id);
            }
        }).thenApplyAsync(uid -> {
            if (uid == null) {
                return notFound("Not Found");
            } else {
                return ok("Traveller Updated");
            }
        });
    }

    public static class CreateUserRequest {
        @Constraints.Required
        public String firstName;

        @Constraints.Required
        public String lastName;

        public String middleName;

        @Constraints.Required
        public String gender;

        @Constraints.Required
        @Constraints.Email
        public String email;

        @Constraints.Required
        public String password;

        @Constraints.Required
        public int dateOfBirth;

        @Constraints.Required
        public List<NationalityRequest> nationalities;

        @Constraints.Required
        public List<Integer> travellerTypes;
    }

    public static class UpdateUserRequest {
        @Constraints.Required
        public String firstName;

        @Constraints.Required
        public String lastName;

        public String middleName;

        @Constraints.Required
        public String gender;

        @Constraints.Required
        public int dateOfBirth;

        @Constraints.Required
        public List<NationalityRequest> nationalities;

        @Constraints.Required
        public List<Long> travellerTypes;
    }

    public static class NationalityRequest {
        public Long id;
        public boolean hasPassport;
    }


    // DW ABOUT THIS
    public Result index() {
        return ok("Travel EA - Home");
    }



}
