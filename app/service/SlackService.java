package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.HttpHandlerModels.ResponseHandler;
import play.api.Configuration;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SlackService {

    private DatabaseExecutionContext context;
    private String slackClientID = "737773912711.735910477760";
    private String slackClientSecret = "08b8f234097b1ccd346c87dfd3277c0c";
    private String slackApi = "https://slack.com/api/oauth.access";
    private WSClient ws;

    @Inject
    public SlackService(DatabaseExecutionContext context, WSClient ws, Configuration configuration) {
        this.context = context;
        this.ws = ws;
    }

    private WSRequest addParamsToSlackAccessTokenRequest(String code) {
        WSRequest request = ws.url(slackApi).setContentType("application/x-www-form-urlencoded");
        request.addQueryParameter("client_id", slackClientID);
        request.addQueryParameter("client_secret", slackClientSecret);
        request.addQueryParameter("code", code);
        request.addQueryParameter("redirect_uri", "http://localhost:8080/user/3/profile");
        return request;
    }

    public CompletableFuture<ResponseHandler> requestAccessToken(String code, Long userId) {
        WSRequest slackAccessTokenRequest = addParamsToSlackAccessTokenRequest(code);
        return sendSlackRequest(slackAccessTokenRequest).toCompletableFuture();
    }

    private CompletionStage<ResponseHandler> sendSlackRequest(WSRequest request) {
        return request.post("").thenApplyAsync(wsResponse -> {
            JsonObject resBody = new JsonParser().parse(wsResponse.getBody()).getAsJsonObject();
            return new ResponseHandler(wsResponse.getStatus(), resBody);
        });
    }
}