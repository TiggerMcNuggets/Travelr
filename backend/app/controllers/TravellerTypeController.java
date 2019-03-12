package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.Traveller;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TravellerTypeRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Scala.asScala;


public class TravellerTypeController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final TravellerTypeRepository travellerTypeRepository;

    @Inject
    public TravellerTypeController(
            TravellerTypeRepository travellerTypeRepository,
            HttpExecutionContext httpExecutionContext,
            MessagesApi messagesApi
    ) {
        this.travellerTypeRepository = travellerTypeRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * Displays traveller types for a specific user
     *
     * @param request the http request
     * @param id the traveller id
     * @return 200 response and traveller types json object if successful, 401 if unauthorized
     */
    public CompletionStage<Result> list(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request)) {
        return travellerTypeRepository.list(id).thenApplyAsync((travellerTypes) -> {
            PathProperties pathProperties = PathProperties.parse("id,tType");
            return ok(Ebean.json().toJson(travellerTypes, pathProperties));
        });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }


    }

    /**
     * Renders the travellersTypes.scale.html file and responds with 200 response or raises 400 response
     *
     * @param request http request
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> add(Http.Request request, long id) {
        if (controllers.LoginController.isLoggedIn(request)) {
        return travellerTypeRepository.add(request.body().asJson(), id).thenApplyAsync((traveller) -> {
            if (traveller == null) {
                return badRequest("Bad Request - Failed to add traveller type");
            }
            return ok("Traveller type added to traveller: " + traveller.fname + " " + traveller.lname);
        });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     *
     * @param request the http request
     * @param id user id
     * @return 200 response if 0 or more rows have been deleted, 400 response if user invalid
     */
    public CompletionStage<Result> delete(Http.Request request, Long id) {
        JsonNode data = request.body().asJson();
        return travellerTypeRepository.delete(id, data).thenApplyAsync((Integer deletedRows) -> {
            if (deletedRows == null) return badRequest("Bad Request - Invalid User");
            return ok("Success: " + deletedRows + " deleted rows");
             }
        );
    }

}
