package controllers;

import models.User;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;

import javax.inject.Inject;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    public final static String AUTH_TOKEN_HEADER = "X-AUTHORIZATION";
    public final static String AUTH_TOKEN = "authToken";

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }
}
