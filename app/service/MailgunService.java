package service;

import com.google.gson.JsonObject;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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

            WSRequest request = ws.url(mailgunApi);
            request.addQueryParameter("from", mailgunFromAddress);
            request.addQueryParameter("to", recipientEmail);
            request.addQueryParameter("subject", welcomeEmailSubject);
            request.addQueryParameter("template", "welcome-email");
            request.addQueryParameter("recipient-variables", recipientVariable.toString());
            request.setAuth("api", mailgunKey);

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
//    public CompletableFuture<Integer> sendTripUpdatedEmail(ArrayList<Users> recipients) {
//        return supplyAsync(() -> {
//
//        }, context);
//    }


}
