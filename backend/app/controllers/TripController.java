package controllers;


import com.google.inject.Inject;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import io.ebean.Ebean;
import models.User;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TripRepository;

import java.util.concurrent.CompletionStage;

public class TripController extends Controller {

    @Inject
    FormFactory formFactory;

    private final TripRepository tripRepository;

    @Inject
    public TripController(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Authorization.RequireAuth
    public CompletionStage<Result> listUserTrips(Http.Request request) {
        User user = request.attrs().get(Attrs.USER);
        return tripRepository.getTrips(user.id).thenApplyAsync(users -> ok(Ebean.json().toJson(users)));
    }

}
