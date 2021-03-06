package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.typesafe.config.ConfigFactory;
import dto.HttpHandlerModels.ResponseHandler;
import models.Grouping;
import models.SlackUser;
import models.User;
import models.UserGroup;
import play.api.Configuration;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SlackService {

    private DatabaseExecutionContext context;
    private String slackClientID = "737773912711.735910477760";
    private String slackClientSecret = "08b8f234097b1ccd346c87dfd3277c0c";
    private String slackApiURL = "https://slack.com/api";

    private String frontendUrl;
    private WSClient ws;

    @Inject
    public SlackService(DatabaseExecutionContext context, WSClient ws, Configuration configuration) {
        this.context = context;
        this.ws = ws;
        this.frontendUrl = ConfigFactory.load().getString("baseRedirectUrl");
    }

    /**
     * Builds an authorisation request
     * @param code the slack authorization grant code
     * @param userId the user's id
     * @return the constructed request
     */
    private WSRequest addParamsToSlackAccessTokenRequest(String code, Long userId) {
        String slackURL = slackApiURL + "/oauth.access";
        WSRequest request = ws.url(slackURL).setContentType("application/x-www-form-urlencoded");
        request.addQueryParameter("client_id", slackClientID);
        request.addQueryParameter("client_secret", slackClientSecret);
        request.addQueryParameter("code", code);
        request.addQueryParameter("redirect_uri", frontendUrl + "user/" + userId + "/profile");
        return request;
    }

    /**
     * Construct and send an authorisation request for the access token to the Slack API
     * Refer to: https://a.slack-edge.com/80588/img/api/slack_oauth_flow_diagram@2x.png
     * @param code the slack authorization grant code
     * @param userId the user's id
     * @return the status of the sent request
     */
    public CompletableFuture<ResponseHandler> requestAccessToken(String code, Long userId) {
        WSRequest slackAccessTokenRequest = addParamsToSlackAccessTokenRequest(code, userId);
        return sendSlackRequest(slackAccessTokenRequest).toCompletableFuture();
    }

    /**
     * Construct and send an request for creating a private channel to the Slack API
     * @param groupOwner the slack information for the user who owns the group
     * @param groupName the name of the group
     * @return the status of the sent request
     */
    public CompletableFuture<ResponseHandler> requestPrivateChannel(SlackUser groupOwner, String groupName) {
        String slackURL = slackApiURL + "/channels.create";
        WSRequest request = ws.url(slackURL).setContentType("application/x-www-form-urlencoded");
        request.addQueryParameter("token", groupOwner.getAccessToken());
        request.addQueryParameter("name", groupName);
        return sendSlackRequest(request).toCompletableFuture();
    }

    /**
     * Deprecated method, uses legacy token :(
     * @param groupOwner
     * @param grouping
     * @param channelId
     * @return
     */
    public CompletableFuture<Void> inviteUsersToWorkspace(SlackUser groupOwner, Grouping grouping, String channelId) {
        String slackURL = slackApiURL + "/users.admin.invite";
        WSRequest request = ws.url(slackURL).setContentType("application/x-www-form-urlencoded");

        List<UserGroup> recipients = grouping.getUserGroups();
        CompletableFuture itHurtsToDoItThisWay[] = new CompletableFuture[recipients.size()];

        int index = 0;
        for (UserGroup recipient: recipients) {
            String email = recipient.getUser().getEmail();
            request.addQueryParameter("token", groupOwner.getAccessToken());
            request.addQueryParameter("email", email);
            request.addQueryParameter("channels", channelId);
            itHurtsToDoItThisWay[index++] = sendSlackRequest(request).toCompletableFuture();
        }

        return CompletableFuture.allOf(itHurtsToDoItThisWay);
    }

    /**
     * Construct and send an request for fetching server data from the Slack API
     * @param slackUser the slack information for the user who owns the group
     * @return the status of the sent request
     */
    public CompletableFuture<ResponseHandler> requestServerInfo(SlackUser slackUser) {
        String slackURL = slackApiURL + "/team.info";
        WSRequest request = ws.url(slackURL).setContentType("application/x-www-form-urlencoded");
        request.addQueryParameter("token", slackUser.getAccessToken());
        return sendSlackRequest(request).toCompletableFuture();
    }

    /**
     * Sends any POST request to the slack API
     * @param request to be sent to the Slack API
     * @return the response handler with the status and body
     */
    private CompletionStage<ResponseHandler> sendSlackRequest(WSRequest request) {
        return request.post("").thenApplyAsync(wsResponse -> {
            JsonObject resBody = new JsonParser().parse(wsResponse.getBody()).getAsJsonObject();
            return new ResponseHandler(wsResponse.getStatus(), resBody);
        });
    }
}