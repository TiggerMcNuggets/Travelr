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

    /**
     * Gets all traveller types
     * @return 200 with list of traveller types if all ok
     */
    public CompletionStage<Result> getTravellerTypes() {
        return travellerTypeRepository.getTravellerTypes().thenApplyAsync(travellerTypes -> ok(Json.toJson(travellerTypes)));
    }
}
