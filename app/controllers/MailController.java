package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class MailController extends Controller {
    private WSClient ws;

    @Inject
    public MailController(WSClient ws) {
        this.ws = ws;
    }

    public CompletionStage<Result> sendEmailGivenTrip() {
        String reqParameters;

        reqParameters = "{" +
            "\"from\": \"rsh134@uclive.ac.nz\"," +
            "\"to\": \"rsh134@uclive.ac.nz\"," +
            "\"subject\": \"JAVATEST\"," +
            "\"text\": \"testing\"" +
        "}";

        JsonNode body = Json.parse(reqParameters);
        WSRequest request = ws.url("https://api.mailgun.net/v3/sandboxc7b3b2d7b248471d9e3c50aa8687d1c4.mailgun.org");
        request.addHeader("api", "369f89d26186533f02492395d4086aef-73ae490d-9c3ed2ca");
        request.setAuth("api:", "369f89d26186533f02492395d4086aef-73ae490d-9c3ed2ca", WSAuthScheme.BASIC);

        CompletionStage<WSResponse> asyncResponse = request.post(body);
        return asyncResponse.thenApplyAsync(response -> {
            return ok();
        });
    }
}