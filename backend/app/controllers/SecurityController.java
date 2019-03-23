package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    // Header key name
    public final static String AUTH_TOKEN_HEADER = "X-Authorization";
    // Database column name
    public final static String AUTH_TOKEN = "token";

    /**
     * Logs a user in with email and password and sets user token
     * @param request the http request
     * @return 200 with json object of id and token if all ok
     */
    public Result login(Http.Request request) {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest(request);

        if (loginForm.hasErrors()) {
            return badRequest("Bad Request");
        }

        Login login = loginForm.get();

        Optional<User> userOptional = User.find.findByLogin(login.email, login.password);

        // If no user exists, return unauthorized
        if (!userOptional.isPresent()) {
            return unauthorized("Incorrect login details");
        } else {
            User user = userOptional.get();
            // TODO delete before committing to vue-restructure
            String authToken = user.setToken("123");
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            authTokenJson.put("id", user.id);
            return ok(authTokenJson);
        }
    }

    /**
     * Logs a user out and removes token
     * @param request the http request
     * @return 200 with string if all ok
     */
    @Authorization.RequireAuth()
    public Result logout(Http.Request request) {
        User user = request.attrs().get(Attrs.USER);
        user.deleteAuthToken();
        return ok("Successfully logged out");
    }

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String email;

        @Constraints.Required
        public String password;
    }

}
