package controllers;

import models.Destination;
import models.Nationality;
import models.TravellerType;
import models.User;
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


        User user1 = new User("Adam", "Conway", "adam@test.com", 1);
        user1.setPassword("123");
        User user2 = new User("John", "Smith", "john@test.com", 1);
        User user3 = new User("Test", "123", "123@test.com", 1);
        User user4 = new User("123", "123", "123333@test.com", 1);
        user1.insert();
        user2.insert();
        user3.insert();
        user4.insert();

        new Destination("River", 1.0, 1.0, "123", "123", "123", user1).insert();
        new Destination("Mountain", 1.0, 1.0, "123", "123", "123", user1).insert();
        new Destination("City", 1.0, 1.0, "123", "123", "123", user1).insert();
        new Destination("Beach", 1.0, 1.0, "123", "123", "123", user2).insert();
        new Destination("Ski", 1.0, 1.0, "123", "123", "123", user3).insert();

        return ok();
    }
}
