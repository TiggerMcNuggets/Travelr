package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class PopulateController extends Controller {
    /**
     * Populates the database with example values
     * @return 200 with string if all ok
     */
    public Result populateDatabase() {
        Nationality nationality = new Nationality("Australian");
        nationality.insert();
        new Nationality("Spain").insert();
        new Nationality("China").insert();
        new Nationality("Danish").insert();
        new Nationality("Egypt").insert();
        new Nationality("French").insert();
        new Nationality("Greek").insert();
        new Nationality("Hungarian").insert();
        new Nationality("Italy").insert();
        new Nationality("Japan").insert();
        new Nationality("Kenyan").insert();
        new Nationality("Lithuanian").insert();
        new Nationality("Mexico").insert();
        new Nationality("Portuguese").insert();

        new TravellerType("Thrill-seeker").insert();
        new TravellerType("Functional/Business Traveller").insert();
        TravellerType travellerType = new TravellerType("Gap Year");
        travellerType.insert();
        new TravellerType("Frequent Weekender").insert();
        new TravellerType("Holidaymaker").insert();
        new TravellerType("Backpacker").insert();
        new TravellerType("Groupies").insert();


        List<TravellerType> travellerTypes = new ArrayList<TravellerType>();
        travellerTypes.add(travellerType);

        User user1 = new User("Admin","Test", "admin@test.com", 1);
        user1.setTravellerTypes(travellerTypes);
        user1.setPassword("123");
        user1.setToken("123");
        user1.setAccountType(1);

        User user2 = new User("John", "Smith", "john@test.com", 1);
        user2.setTravellerTypes(travellerTypes);
        user2.setPassword("abc");
        user2.setToken("abc");

        User user3 = new User("Test", "123", "123@test.com", 1);
        user3.setTravellerTypes(travellerTypes);

        User user4 = new User("123", "123", "123333@test.com", 1);
        user4.setPassword("nonadmin");
        user4.setToken("nonadmin");
        user4.setTravellerTypes(travellerTypes);

        User user5 = new User("Master", "Splinter", "bigMouse@masterAdmin.net", 1);
        user5.setPassword("master");
        user5.setToken("111");
        user5.setAccountType(2);
        user5.setTravellerTypes(travellerTypes);

        user1.insert();
        user2.insert();
        user3.insert();
        user4.insert();
        user5.insert();

        new UserNationality(user1, nationality, true).insert();
        new UserNationality(user2, nationality, true).insert();
        new UserNationality(user3, nationality, true).insert();
        new UserNationality(user4, nationality, true).insert();
        new UserNationality(user5, nationality, true).insert();

        User admin = User.find.findByEmail("admin@admin.com");

        if (admin != null) {
            admin.setTravellerTypes(travellerTypes);
            admin.save();
            new UserNationality(admin, nationality, true).insert();
        }

        Destination dest1 = new Destination("River", 1.0, 1.0, "123", "123", "123", user1);
        dest1.setTravellerTypes(travellerTypes);

        Destination dest2 = new Destination("Mountain", 2.0, 2.0, "123", "123", "123", user1);
        dest2.setTravellerTypes(travellerTypes);

        Destination dest3 = new Destination("City", 3.0, 3.0, "123", "123", "123", user2);
        dest3.setTravellerTypes(travellerTypes);

        Destination dest4 = new Destination("Beach", 4.0, 4.0, "123", "123", "123", user2);
        dest4.setTravellerTypes(travellerTypes);

        Destination dest5 = new Destination("Ski", 5.0, 5.0, "123", "123", "123", user3);
        dest5.setTravellerTypes(travellerTypes);


        dest1.insert();
        dest2.insert();
        dest3.insert();
        dest4.insert();
        dest5.insert();

        Trip trip1 = new Trip("Backpacking 2018", user1);
        Trip trip2 = new Trip("New Trip", user2);

        trip1.insert();
        trip2.insert();

        new TripDestination(1, 2, 0, trip1, dest1).insert();
        new TripDestination(3, 4, 1, trip1, dest2).insert();
        new TripDestination(1, 2, 0, trip2, dest3).insert();
        new TripDestination(3, 4, 1, trip2, dest4).insert();

        return ok("Populated");
    }
}
