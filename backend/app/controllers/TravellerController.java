package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TravellerRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.libs.Scala.asScala;

public class TravellerController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final TravellerRepository travellerRepository;

    @Inject
    public TravellerController(
            TravellerRepository travellerRepository,
            HttpExecutionContext httpExecutionContext,
            MessagesApi messagesApi
    ) {
        this.travellerRepository = travellerRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * Renders travellers.scala.html and sends 200 response
     *
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> list(Http.Request request, String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities,List<String> traveller_types, String orderBy) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return travellerRepository.list(fname, lname, gender, minAge, maxAge, nationalities, traveller_types, orderBy).thenApplyAsync((travellers) -> {
                return ok(views.html.travellers.render(asScala(travellers)));
            });
        } else {
            return travellerRepository.list(fname, lname, gender, minAge, maxAge, nationalities, traveller_types, orderBy).thenApplyAsync((travellers) -> {
                return unauthorized("Not Logged In: Access Denied");
            });
        }
    }

    /**
     * Temporary helper function to return the single string value from an optional type, since cannot find method which currently does this.
     * Used currently to extract the id from the connected session variable.
     * @param optional The string representation of an Optional Type
     * @return
     */
    private String extractValueFromOptional(String optional) {
        return optional.substring(9, optional.length() -1);
    }


    /**
     * Renders a profile page for a particular user when logged in. Gives denied access if not logged in or trying to access another user.
     *
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> showProfile(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request) && !extractValueFromOptional(request.session().getOptional("connected").toString()).equals(id.toString())) {
            return CompletableFuture.completedFuture(unauthorized("Access Denied - Trying to access another user"));
        }
        else if (controllers.LoginController.isLoggedIn(request)) {
            return travellerRepository.profile(id).thenApplyAsync((travellers) -> {
                return ok(views.html.profile.render(asScala(travellers)));
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }


    /**
     * Renders the travellers.scale.html file and responds with 200 response or raises 400 response
     *
     * @param request http request
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> add(Http.Request request) {
        return travellerRepository.add(request).thenApplyAsync((traveller) -> {
            if (traveller == null) {
                return badRequest("Bad Request - Failed to add " + traveller);
            }
            return ok("Traveller: " + traveller + " added");
        });
    }

    /**
     *
     * @param request http request
     * @param id user id
     * @return 200 response if success in updating traveller, 400 response otherwise
     */
    public CompletionStage<Result> update(Http.Request request, Long id) {
        JsonNode data = request.body().asJson();
        if (controllers.LoginController.isLoggedIn(request)) {
            return travellerRepository.update(data, id).thenApplyAsync((isTransactionCompleted) -> {
                if (isTransactionCompleted) {
                    return ok("Traveller updated");
                } else {
                    return badRequest("Could not update traveller");
                }
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    /**
     *
     * @param request http request
     * @param id user id
     * @return 200 response if success in deleting traveller, 400 response otherwise
     */
    public CompletionStage<Result> delete(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return travellerRepository.delete(id).thenApplyAsync((isTransactionCompleted) -> {
                if (isTransactionCompleted) {
                    return ok("Traveller deleted");
                } else {
                    return badRequest("Could not delete traveller");
                }
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }

    }

    public Result index() {
        return ok("TravelEA Home Page");
    }

}
