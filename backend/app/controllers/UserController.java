package controllers;

import akka.dispatch.sysmsg.Create;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.dto.User.*;
import io.ebean.Ebean;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
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

    @Authorization.RequireAuth
    public CompletionStage<Result> getAllUsers(Http.Request request) {
        return userRepository.getAllUsers().thenApplyAsync(users -> {

            GetUsersRes response = new GetUsersRes(users);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response.getGetUserRes());

            return ok(jsonResponse);
        });
    }

    public CompletionStage<Result> addUser(Http.Request request) {
        // Turns the post data into a form object
        Form<CreateUserReq> userRequestForm = formFactory.form(CreateUserReq.class).bindFromRequest(request);

        // Bad Request Check
        if (userRequestForm.hasErrors()) {
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
    @Authorization.RequireAuth
    public CompletionStage<Result> getUser(Http.Request request, Long id) {
        return userRepository.getUser(id).thenApplyAsync(user -> {

            // Not Found Check
            if (user == null) {
                return notFound("Traveller not found");
            }

            GetUserRes response = new GetUserRes(user);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);

            return ok(jsonResponse);
        });
    }

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

    // DW ABOUT THIS
    public Result index() {
        return ok("Travel EA - Home");
    }



}
