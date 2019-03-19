package controllers;

import models.Nationality;
import models.TravellerType;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class StaticDataController extends Controller {
    public Result populateDatabase() {
        new Nationality("Australian").insert();
        new Nationality("Belgian").insert();
        new Nationality("Chinese").insert();
        new Nationality("Danish").insert();
        new Nationality("Egyption").insert();
        new Nationality("French").insert();
        new Nationality("Greek").insert();
        new Nationality("Hungarian").insert();
        new Nationality("Italian").insert();
        new Nationality("Japanese").insert();
        new Nationality("Kenyan").insert();
        new Nationality("Lithuanian").insert();
        new Nationality("Mexican").insert();
        new Nationality("Portuguese").insert();

        new TravellerType("Thrill-seeker").insert();
        new TravellerType("Functional/Business Traveller").insert();
        new TravellerType("Gap Year").insert();
        new TravellerType("Frequent Weekender").insert();
        new TravellerType("Holidaymaker").insert();
        new TravellerType("Backpacker").insert();
        new TravellerType("Groupies").insert();

        return ok();
    }
}
