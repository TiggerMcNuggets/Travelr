package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repository.NationalityRepository;

import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.concurrent.CompletionStage;

@Entity
public class NationalityController extends Controller {

    private NationalityRepository nationalityRepository;

    @Inject
    public NationalityController(NationalityRepository nationalityRepository) {
        this.nationalityRepository = nationalityRepository;
    }

    /**
     * Gets list of all nationalities
     * @return 200 with list of all nationalities if all ok
     */
    public CompletionStage<Result> getNationalities() {
        return nationalityRepository.getNationalities().thenApplyAsync(nationalities -> ok(Json.toJson(nationalities)));
    }

    public Result test() {
        return ok();
    }
}
