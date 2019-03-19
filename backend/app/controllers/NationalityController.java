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

    public CompletionStage<Result> getNationalities() {
        return nationalityRepository.getNationalities().thenApplyAsync(nationalities -> {
            return ok(Json.toJson(nationalities));
        });
    }
}
