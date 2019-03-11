package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";
}


//    // returns an authToken
//    public Result login() {
////        Form<LoginRequest> loginForm = formFactory.form(LoginRequest.class).bindFromRequest();
//
////        if (loginForm.hasErrors()) {
////            return badRequest(loginForm.errorsAsJson());
////        }
////
////        LoginRequest login = loginForm.get();
////
////        //User user = User.findByEmailAddressAndPassword(login.emailAddress, login.password);
////
////        if (user == null) {
////            return unauthorized();
////        }
////        else {
////            String authToken = user.createToken();
////            ObjectNode authTokenJson = Json.newObject();
////            authTokenJson.put(AUTH_TOKEN, authToken);
////            response().setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());
////            return ok(authTokenJson);
////        }
////    }
////
////    public static class LoginRequest {
////
////        @Constraints.Required
////        @Constraints.Email
////        public String emailAddress;
////
////        @Constraints.Required
////        public String password;
////
////    }
//
//}

