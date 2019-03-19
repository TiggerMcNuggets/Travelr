package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repository.TravellerTypeRepository;

import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.concurrent.CompletionStage;

@Entity
public class TravellerTypeController extends Controller {

    private TravellerTypeRepository travellerTypeRepository;

    @Inject
    public TravellerTypeController(TravellerTypeRepository travellerTypeRepository) {
        this.travellerTypeRepository = travellerTypeRepository;
    }

    public CompletionStage<Result> getTravellerTypes() {
        return travellerTypeRepository.getTravellerTypes().thenApplyAsync(travellerTypes -> {
            return ok(Json.toJson(travellerTypes));
        });
    }
}
