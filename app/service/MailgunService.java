package service;

import com.google.gson.JsonObject;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.ok;

/**
 * A service which holds the Email notification logic using the MailGun API
 */
public class MailgunService {

    private DatabaseExecutionContext context;

    private WSClient ws;
    private String mailgunApi = "https://api.mailgun.net/v3/sandboxc7b3b2d7b248471d9e3c50aa8687d1c4.mailgun.org/messages";
    private String mailgunKey = "369f89d26186533f02492395d4086aef-73ae490d-9c3ed2ca";
    private String mailgunFromAddress = "fd15@uclive.ac.nz";

    @Inject
    public MailgunService(DatabaseExecutionContext context, WSClient ws) {
        this.context = context;
        this.ws = ws;
    }


    /**
     *
     * @param recipientEmail
     * @param recipientFirstName
     * @param recipientLastName
     * @return
     */
    public CompletableFuture<Integer> sendWelcomeEmail(String recipientEmail, String recipientFirstName, String recipientLastName) {
        return supplyAsync(() -> {
            String welcomeEmailSubject = "Travelr - Welcome Aboard!";

            JsonObject recipientVariableFields = new JsonObject();
            recipientVariableFields.addProperty("firstName", recipientFirstName);
            recipientVariableFields.addProperty("lastName", recipientLastName);

            JsonObject recipientVariable = new JsonObject();
            recipientVariable.add(recipientEmail, recipientVariableFields);

            WSRequest request = ws.url(mailgunApi);
            request.addQueryParameter("from", mailgunFromAddress);
            request.addQueryParameter("to", recipientEmail);
            request.addQueryParameter("subject", welcomeEmailSubject);
            request.addQueryParameter("template", "welcome-email");
            request.addQueryParameter("recipient-variables", recipientVariable.toString());
            request.setAuth("api", mailgunKey);
            //request.setAuth("api", mailgunKey, WSAuthScheme.BASIC);

            CompletionStage<WSResponse> asyncResponse = request.post("");

            return asyncResponse.thenApply(response -> response.getStatus()).toCompletableFuture().join();

        }, context);
    }





//
//
//    public CompletableFuture<Result> sendAddedToGroupEmail(User user) {
//        return supplyAsync(() -> {
//
//
//
//            return Trip.find.byId(tripId);
//
//        }, context);
//    }
//
//    public CompletableFuture<Result> sendTripUpdatedEmail(ArrayList<String> recipients, String templateName) {
//        return supplyAsync(() -> {
//
//
//
//            return Trip.find.byId(tripId);
//
//        }, context);
//    }
//
//    public CompletableFuture<Result> sendEmail(ArrayList<String> recipients, String templateName) {
//        return supplyAsync(() -> {
//
//
//            return Trip.find.byId(tripId);
//
//        }, context);
//    }
//

}
