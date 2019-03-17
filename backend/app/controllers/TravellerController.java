package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.text.PathProperties;
import models.PersonalPhoto;
import play.i18n.MessagesApi;
import play.libs.Files;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.PersonalPhotoRepository;
import repository.TravellerRepository;
import utils.FileHelper;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.libs.Scala.asScala;

public class TravellerController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final TravellerRepository travellerRepository;
    private final PersonalPhotoRepository personalPhotoRepository;

    @Inject
    public TravellerController(
            TravellerRepository travellerRepository,
            HttpExecutionContext httpExecutionContext,
            MessagesApi messagesApi,
            PersonalPhotoRepository personalPhotoRepository
    ) {
        this.travellerRepository = travellerRepository;
        this.personalPhotoRepository = personalPhotoRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     *
     * @param request the http request
     * @param fname the traveller first name
     * @param lname the traveller last name
     * @param gender the traveller gender
     * @param minAge the traveller min age
     * @param maxAge the traveller max age
     * @param nationalities the nationality the traveller has to be from
     * @param traveller_types the traveller types the traveller must have
     * @param orderBy value the list needs to be ordered to
     * @return @return 200 response and list of travellers in JSON format if successful, 401 if unauthorised
     */
    public CompletionStage<Result> list(Http.Request request, String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities,List<String> traveller_types, String orderBy) {
        if (controllers.LoginController.isLoggedIn(request)) {
            return travellerRepository.list(fname, lname, gender, minAge, maxAge, nationalities, traveller_types, orderBy).thenApplyAsync((travellers) -> {
                PathProperties pathProperties = PathProperties.parse("fname,mname,lname,dob,gender,nationalities(*),types(*)");
                return ok(Ebean.json().toJson(travellers, pathProperties));
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
     * @param id the user id
     * @param request the http request
     * @return 200 response and traveller information as JSON object if successful, 401 if unauthorised or trying to view someone else's profile
     */
    public CompletionStage<Result> showProfile(Http.Request request, Long id) {
        if (controllers.LoginController.isLoggedIn(request) && !extractValueFromOptional(request.session().getOptional("connected").toString()).equals(id.toString())) {
            return CompletableFuture.completedFuture(unauthorized("Access Denied - Trying to access another user"));
        }
        else if (controllers.LoginController.isLoggedIn(request)) {
            return travellerRepository.profile(id).thenApplyAsync((travellers) -> {
                PathProperties pathProperties = PathProperties.parse("fname,mname,lname,dob,email,gender,nationalities(*),types(*)");
                return ok(Ebean.json().toJson(travellers, pathProperties));
            });
        } else {
            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }

    /**
     * Adds traveller to db
     *
     * @param request http request
     * @return 200 if successful, 404 for bad request
     */
    public CompletionStage<Result> add(Http.Request request) {
        return travellerRepository.add(request).thenApplyAsync((traveller) -> {
            if (traveller == null) {
                return badRequest("Bad Request - Failed to add traveller");
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



}
