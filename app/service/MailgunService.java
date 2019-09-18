package service;

import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.google.gson.JsonObject;
import com.typesafe.config.ConfigFactory;
import models.Grouping;
import models.TripNode;
import models.User;
import models.UserGroup;
import net.fortuna.ical4j.model.Calendar;
import org.apache.commons.lang3.StringUtils;
import play.api.Configuration;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import utils.iCalCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import play.mvc.Http.MultipartFormData.*;

import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * A service which holds the email notification logic using the MailGun API
 */
public class MailgunService {

    private DatabaseExecutionContext context;


    private String websiteUrl;
    private WSClient ws;
    private String mailgunApi = "https://api.mailgun.net/v3/sandboxc7b3b2d7b248471d9e3c50aa8687d1c4.mailgun.org/messages";
    private String mailgunKey = "369f89d26186533f02492395d4086aef-73ae490d-9c3ed2ca";
    private String mailgunFromAddress = "fd15@uclive.ac.nz";
    private iCalCreator iCalCreator;

    @Inject
    public MailgunService(DatabaseExecutionContext context, WSClient ws, Configuration configuration) {
        this.context = context;
        this.ws = ws;
        this.websiteUrl = ConfigFactory.load().getString("baseRedirectUrl");
        this.iCalCreator = new iCalCreator();
    }

    /**
     * A general function for generating a ws request to be sent to mailgun. This is to reduce code duplication
     * in the mailgunService class
     *
     * @param recipients An array containing user objects intended to receive the email.
     * @param subject The subject of the email as a string.
     * @param template The name of the email template to be used as a string.
     * @param recipientVariables The custom variables to be included in the mailgun request, Json formatted as a string.
     * @return a WSRequest http request to be sent to the mailgun API.
     */
    private WSRequest buildMailgunRequest(List<User> recipients, String subject, String template, JsonObject recipientVariables){
        WSRequest request = ws.url(mailgunApi);
        request.addQueryParameter("from", mailgunFromAddress);
        for (User recipient : recipients) {
            request.addQueryParameter("to", recipient.email);
        }
        request.addQueryParameter("subject", subject);
        request.addQueryParameter("template", template);
        request.addQueryParameter("recipient-variables", recipientVariables.toString());
        request.setAuth("api", mailgunKey);
        return request;
    }


    /**
     * This methods handles the sending of emails to the Mailgun API. It requires
     * a built request to be sent, and returns the status code to the caller
     * once the request is executed.
     *
     * @param request to be sent to the Mailgun API
     * @return an integer response code from the Mailgun API
     */
    private CompletionStage<Integer> sendMailgunRequest(WSRequest request) {
        return request.post("").thenApplyAsync(WSResponse::getStatus);
    }

    /**
     * This methods handles the sending of emails to the Mailgun API. It requires
     * a built request to be sent, and returns the status code to the caller
     * once the request is executed.
     *
     * @param request to be sent to the Mailgun API
     * @return an integer response code from the Mailgun API
     */
    private CompletionStage<Integer> sendMailgunRequestWithAttachment(WSRequest request, File file) {
        Source<ByteString, ?> fileSource = FileIO.fromPath(Paths.get(file.getAbsolutePath()));
        FilePart<Source<ByteString, ?>> fp = new FilePart<>(getBaseName(file.getName()), file.getName(), "text/plain", fileSource);
        DataPart dp = new DataPart("key", "value");

        return request.post(Source.from(Arrays.asList(fp, dp))).thenApplyAsync(WSResponse::getStatus);
    }


    /**
     * Composes and sends a welcome email to the given email address. This function
     * uses the 'welcome-email' template on our Mailgun account and provides the
     * user's first name to the template's placeholder field.
     *
     * @param recipient a user object that contains the recipient's data
     * @return a response code from the Mailgun API which is useful for testing.
     */
    public CompletableFuture<Integer> sendWelcomeEmail(User recipient) {
        String welcomeEmailSubject = "Travelr - Welcome Aboard!";

        ArrayList<User> recipientList = new ArrayList<>();
        recipientList.add(recipient);

        JsonObject recipientVariableFields = new JsonObject();
        recipientVariableFields.addProperty("firstName", StringUtils.capitalize(recipient.firstName));
        recipientVariableFields.addProperty("welcomeURL", websiteUrl);


        JsonObject recipientVariable = new JsonObject();
        recipientVariable.add(recipient.email, recipientVariableFields);

        WSRequest request = buildMailgunRequest(recipientList,
                welcomeEmailSubject,
                "welcome-email",
                recipientVariable);
        return sendMailgunRequest(request).toCompletableFuture();
    }

    /**
     * Composes and sends an email notification to the user's email address. This function
     * uses the 'added-to-group' email template on our Mailgun account and provides the
     * user's first name, the group's name and group page URL to the template's placeholder fields.
     *
     * @param recipient a user object that contains the recipient's data.
     * @param groupId the id of the group that the user has been added to.
     * @return a response code from the Mailgun API which is useful for testing.
     */
    public CompletableFuture<Integer> sendAddedToGroupEmail(User recipient, Long groupId) {
        Grouping grouping = UserGroup.find.query().where().eq("user_id", recipient.getId()).eq("grouping_id", groupId).findOne().getGrouping();

        if (grouping == null) {
            return CompletableFuture.completedFuture(404);
        }

        String groupingEmailSubject = "Travelr - You've been added to " + StringUtils.capitalize(grouping.getName()) +"!";

        ArrayList<User> recipientList = new ArrayList<>();
        recipientList.add(recipient);

        JsonObject recipientVariableFields = new JsonObject();
        recipientVariableFields.addProperty("firstName", StringUtils.capitalize(recipient.firstName));
        recipientVariableFields.addProperty("groupName", grouping.getName());
        recipientVariableFields.addProperty("groupURL", websiteUrl + "usergroups");

        JsonObject recipientVariable = new JsonObject();
        recipientVariable.add(recipient.email, recipientVariableFields);

        WSRequest request = buildMailgunRequest(recipientList,
                groupingEmailSubject,
                "added-to-group",
                recipientVariable);
        return sendMailgunRequest(request).toCompletableFuture();
    }


    /**
     * Creates a http request for the mailgun api endpoint to compose an email regarding the update of a trip to
     * a user following the trip. This function uses the "trip-update-email" template on the mailgun account
     * and provides the user's first name, email and the previous name of the trip updated.
     *
     * @param user The user that chose to send iCal
     * @param tripNode The tripNode
     * @return a response code given by the mailgun API
     */
    public CompletableFuture<Integer> sendTripiCalEmail(User user, TripNode tripNode) {
        Calendar calendar = iCalCreator.createCalendarFromTrip(tripNode);
        File tempFile = null;
        WSRequest request;
        try {
            tempFile = File.createTempFile(tripNode.getName(), ".ics");
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write(calendar.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject recipientVariables = new JsonObject();
        JsonObject recipientVariableFields = new JsonObject();
        String subject = "Travelr - Your trip " + tripNode.getName() + " was recently updated.";
        for (User recipient: tripNode.getUserGroup().getUsers()) {
            recipientVariableFields.addProperty("firstName", StringUtils.capitalize(recipient.getFirstName()));
            recipientVariableFields.addProperty("groupName", tripNode.getName());
            recipientVariableFields.addProperty("groupURL", websiteUrl + "user/"
                    + user.getId() + "/trips/" + tripNode.getId());
            recipientVariables.add(recipient.getEmail(), recipientVariableFields);
        }
        request = buildMailgunRequest(tripNode.getUserGroup().getUsers(),
                subject,
                "trip-updated",
                recipientVariables);
        return sendMailgunRequestWithAttachment(request, tempFile).toCompletableFuture();
    }
}
