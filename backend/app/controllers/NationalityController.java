package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.NationalityRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Scala.asScala;

public class NationalityController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final NationalityRepository nationalityRepository;

    @Inject
    public NationalityController(
            NationalityRepository nationalityRepository,
            HttpExecutionContext httpExecutionContext,
            MessagesApi messagesApi
    ) {
        this.nationalityRepository = nationalityRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * Renders traveller_nationalitites.scala.html and sends 200 response
     *
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> list(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request)) {
        return nationalityRepository.list(id).thenApplyAsync((nationalities) -> {
                    PathProperties pathProperties = PathProperties.parse("id,nationality,hasPassport");
                    return ok(Ebean.json().toJson(nationalities, pathProperties));
                }
        );
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }



    }

    /**
     * @param request the http request
     * @param id user id
     * @return 200 response if 0 or more rows have been inserted, 400 response if user invalid
     */
    public CompletionStage<Result> add(Http.Request request, Long id) {
        JsonNode data = request.body().asJson();
        if (controllers.LoginController.isLoggedIn(request)) {
            return nationalityRepository.add(id, data).thenApplyAsync((Integer insertedRows) -> {
                if (insertedRows == null) return badRequest("Bad Request - Invalid User");
                return ok("Success: " + insertedRows + " inserted rows");
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
        if (controllers.LoginController.isLoggedIn(request)) {
            return nationalityRepository.delete(id, data).thenApplyAsync((Integer insertedRows) -> {
                        if (insertedRows == null) return badRequest("Bad Request - Invalid User");
                        return ok("Success: " + insertedRows + " deleted rows");
                    }
            );
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

}
