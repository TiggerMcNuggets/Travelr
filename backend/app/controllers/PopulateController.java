package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class PopulateController extends Controller {
    /**
     * Populates the database with example values
     * @return 200 with string if all ok
     */
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


        User user1 = new User("Global", "Admin", "admin@test.com", 1);
        user1.setPassword("123");
        user1.setToken("123");
        user1.setAccountType(1);
        User user2 = new User("John", "Smith", "john@test.com", 1);
        user2.setPassword("abc");
        user2.setToken("abc");
        User user3 = new User("Test", "123", "123@test.com", 1);
        User user4 = new User("123", "123", "123333@test.com", 1);
        user4.setPassword("nonadmin");
        user4.setToken("nonadmin");
        User user5 = new User("Master", "Splinter", "bigMouse@masterAdmin.net", 1);
        user5.setPassword("master");
        user5.setToken("111");
        user5.setAccountType(2);
        user1.insert();
        user2.insert();
        user3.insert();
        user4.insert();
        user5.insert();

        new Destination("River", 1.0, 1.0, "123", "123", "123", user1).insert();
        new Destination("Mountain", 1.0, 1.0, "123", "123", "123", user1).insert();
        new Destination("City", 1.0, 1.0, "123", "123", "123", user1).insert();
        new Destination("Beach", 1.0, 1.0, "123", "123", "123", user2).insert();
        new Destination("Ski", 1.0, 1.0, "123", "123", "123", user3).insert();

        new Trip("Backpacking 2018", user1).insert();
        new Trip("New Trip", user2).insert();


        return ok("Populated");
    }
}
