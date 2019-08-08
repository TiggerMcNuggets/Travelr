package controllers;

import com.google.inject.Inject;
import play.Application;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import static play.test.Helpers.route;

public class MailController extends Controller {
    private Application application;

    @Inject
    FormFactory formFactory;

    public Result sendEmailGivenTrip() {
        Http.RequestBuilder emailRequest = new Http.RequestBuilder();
        emailRequest.method("POST");
        emailRequest.uri("https://api.mailgun.net/v3/sandboxc7b3b2d7b248471d9e3c50aa8687d1c4.mailgun.org");
        String reqParameters;
        reqParameters = "{" +
            "\"api\": \"369f89d26186533f02492395d4086aef-73ae490d-9c3ed2ca\"," +
            "\"from\": \"frd15@uclive.ac.nz\"," +
            "\"to\": \"frd15@uclive.ac.nz\"," +
            "\"subject\": \"hello\"," +
            "\"text\": \"testing\"" +
        "}";
        emailRequest.bodyJson(Json.parse(reqParameters));
        emailRequest.header("api", "369f89d26186533f02492395d4086aef-73ae490d-9c3ed2ca");
        Result result = route(application, emailRequest);
        return result;

    }
}