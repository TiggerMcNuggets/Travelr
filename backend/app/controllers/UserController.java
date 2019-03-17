package controllers;

import akka.dispatch.sysmsg.Create;
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
import javax.validation.Constraint;
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


    public CompletionStage<Result> addUser(Http.Request request) {

        // Turns the post data into a form object
        Form<CreateUserRequest> userRequestForm = formFactory.form(CreateUserRequest.class).bindFromRequest(request);

        // Checks if the data matches all the constraints - if not, return bad request
        if (userRequestForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest());
        }

        // Create an object from the request
        CreateUserRequest req = userRequestForm.get();

        // Create a new user
        User newUser = new User(req);

        // Call add user
        return userRepository.addUser(newUser).thenApplyAsync(id -> {
            return ok();
        });
    }


    public CompletionStage<Result> getAllUsers(Http.Request request) {
        return userRepository.getAllUsers().thenApplyAsync(users -> {
            return ok(Json.toJson(users));
        });

    }


//    public CompletionStage<Result> updateUser(Http.Request request) {
//        // Turns the post data into a form object
//        Form<UpdateUserRequest> userRequestForm = formFactory.form(UpdateUserRequest.class).bindFromRequest(request);
//
//        // Checks if the data matches all the constraints - if not, return bad request
//        if (userRequestForm.hasErrors()) {
//            return CompletableFuture.completedFuture(badRequest());
//        }
//
//        // Create an object from the request
//        UpdateUserRequest req = userRequestForm.get();
//
//        // Create a new user
//
//
//        // Call add user
//        return userRepository.addUser(newUser).thenApplyAsync(id -> {
//            return ok();
//        });
//    }

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
        public List<Integer> travellerTypes;
    }

    public static class NationalityRequest {
        public int id;
        public boolean hasPassport;
    }


    // DW ABOUT THIS
    public Result index() {
        return ok("Travel EA - Home");
    }



}
