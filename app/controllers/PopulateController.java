package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

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

        // Adding usergroups
        Grouping grouping = new Grouping("Our First Group", "We Da Best");
        grouping.insert();

        UserGroup group1 = new UserGroup(user1, grouping, true);
        group1.insert();

        UserGroup group2 = new UserGroup(user2, grouping, false);
        group2.insert();



        Destination dest1 = new Destination("Christchurch", -43.522, 172.581, "City", "Canterbury", "New Zealand", user1);
        dest1.setTravellerTypes(travellerTypes);

        Destination dest2 = new Destination("Big River", -45.160, 168.291, "City", "Auckland", "country", user1);
        dest2.setTravellerTypes(travellerTypes);

        Destination dest3 = new Destination("Nice Park", -25.777, 125.198, "City", "NSW", "Australia", user1);
        dest3.setTravellerTypes(travellerTypes);

        Destination dest4 = new Destination("Slum", -14.152, -58.905, "City", "Alaska", "USA", user1);
        dest4.setTravellerTypes(travellerTypes);

        Destination dest5 = new Destination("Nice View", 15.693, 9.764, "Mountain", "Mckenzie", "New Zealand", user1);
        dest5.setTravellerTypes(travellerTypes);

        Destination dest6 = new Destination("Ski", 41.522, 13.287, "Mountain", "Nigeria", "South Africa", user1);
        dest6.setTravellerTypes(travellerTypes);

        Destination dest7 = new Destination("Boating Beach", 40.779, -98.072, "River", "Slumville", "Brazil", user1);
        dest7.setTravellerTypes(travellerTypes);
        dest7.isPublic = true;

        Destination dest8 = new Destination("Big Mountain", 17.585, -98.688, "Natural Wonder", "Texas", "America", user1);
        dest8.setTravellerTypes(travellerTypes);
        dest8.isPublic = true;

        Destination dest9 = new Destination("Westfield Riccarton", 50.958, -70.967, "Shopping Mall", "South Auckland", "Wukanda", user1);
        dest9.setTravellerTypes(travellerTypes);
        dest9.isPublic = true;

        Destination dest10 = new Destination("Ski Slopes", 52.778, 88.796, "City", "Wellington", "New Zealand", user2);
        dest10.setTravellerTypes(travellerTypes);
        dest10.isPublic = true;

        dest1.insert();
        dest2.insert();
        dest3.insert();
        dest4.insert();
        dest5.insert();
        dest6.insert();
        dest7.insert();
        dest8.insert();
        dest9.insert();
        dest10.insert();

        return ok("Populated");
    }
}
