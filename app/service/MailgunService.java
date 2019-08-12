package service;

import com.google.gson.JsonObject;
import models.User;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A service which holds the email notification logic using the MailGun API
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
     * A general function for generating a ws request to be sent to mailgun. This is to reduce code duplication
     * in the mailgunService class
     *
     * @param recipientEmail The email address of the recipient as a string.
     * @param recipientFirstName The first name of the recipient as a string.
     * @param subject The subject of the email as a string.
     * @param template The name of the email template to be used as a string.
     * @param recipientVariables The custom variables to be included in the mailgun request, Json formatted as a string.
     * @return a WSRequest http request to be sent to the mailgun API.
     */
    private WSRequest generateMailgunRequest(String recipientEmail, String recipientFirstName, String subject, String template, String recipientVariables){
        WSRequest request = ws.url(mailgunApi);
        request.addQueryParameter("from", mailgunFromAddress);
        request.addQueryParameter("to", recipientEmail);
        request.addQueryParameter("subject", subject);
        request.addQueryParameter("template", template);
        request.addQueryParameter("recipient-variables", recipientVariables);
        System.out.println(recipientVariables);
        request.setAuth("api", mailgunKey);
        return request;
    }

    /**
     * Composes and sends a welcome email to the given email address. This function
     * uses the 'welcome-email' template on our Mailgun account and provides the
     * user's first name to the template's placeholder field.
     *
     * @param recipientEmail a string that contains the recipient's email address
     * @param recipientFirstName a string that contains the recipient's first name
     * @return a response code from the Mailgun API which is useful for testing.
     */
    public CompletableFuture<Integer> sendWelcomeEmail(String recipientEmail, String recipientFirstName) {
        return supplyAsync(() -> {
            String welcomeEmailSubject = "Travelr - Welcome Aboard!";

            JsonObject recipientVariableFields = new JsonObject();
            recipientVariableFields.addProperty("firstName", recipientFirstName);

            JsonObject recipientVariable = new JsonObject();
            recipientVariable.add(recipientEmail, recipientVariableFields);
            WSRequest request = generateMailgunRequest(recipientEmail,
                    recipientFirstName,
                    welcomeEmailSubject,
                    "welcome-email",
                    recipientVariable.toString());

            CompletionStage<WSResponse> asyncResponse = request.post("");

            CompletionStage<Integer> responseCode = asyncResponse.thenApply(response -> {
                return response.getStatus();
            });

            return responseCode.toCompletableFuture().join();

        }, context);
    }

//    public CompletableFuture<Integer> sendAddedToGroupEmail(User user) {
//        return supplyAsync(() -> {
//
//        }, context);
//    }
//

    /**
     * Creates a http request for the mailgun api endpoint to compose an email regarding the update of a trip to
     * a user following the trip. This function uses the "trip-update-email" template on the mailgun account
     * and provides the user's first name, email and the previous name of the trip updated.
     *
     * @param recipient A user following the trip updated
     * @param tripName the previous name of the trip
     * @return a response code given by the mailgun API
     */
    public CompletableFuture<Integer> sendTripUpdateEmail(User recipient, String tripName, Long userId, Long tripId) {
        return supplyAsync(() -> {
            JsonObject recipientVariables = new JsonObject();
            JsonObject recipientVariableFields = new JsonObject();
            String subject = "Travelr - Your trip " + tripName + " was recently updated.";
            recipientVariableFields.addProperty("firstName", recipient.firstName);
            recipientVariables.add(recipient.email, recipientVariableFields);
            WSRequest request = generateMailgunRequest(recipient.email,
                    recipient.firstName,
                    subject,
                    "welcome-email", // TODO change this to the trip update email template
                    recipientVariables.toString());
            CompletionStage<WSResponse> asyncResponse = request.post("");

            CompletionStage<Integer> responseCode = asyncResponse.thenApply(response -> {
                return response.getStatus();
            });

            return responseCode.toCompletableFuture().join();
        }, context);
    }


}
