package controllers;

import models.User;
import play.mvc.Http;
import play.mvc.Security;

import java.util.Optional;

public class Secured extends Security.Authenticator {

    @Override
    public Optional<String> getUsername(Http.Request req) {
        Optional<Http.Cookie> cookie = req.cookies().getCookie(SecurityController.AUTH_TOKEN_HEADER);
        String authToken;
        if(cookie.isPresent()) {
            authToken = cookie.get().value();
            Optional<User> user = models.User.find.findByAuthToken(authTokenHeaderValues[0]);
            if (user != null) {

            }
        }
       return null;
    }
}
