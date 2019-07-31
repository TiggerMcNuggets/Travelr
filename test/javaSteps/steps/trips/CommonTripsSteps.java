package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import models.Destination;
import models.Trip;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class CommonTripsSteps {

    StateSingleton state = StateSingleton.getInstance();

    @Given("The destinations are")
    public void the_destinations_are(List<Map<String, String>> dataTable) {
        for (Map<String, String> destInfo : dataTable) {
            Destination dest = new Destination(
                    destInfo.get("name"),
                    Double.valueOf(destInfo.get("latitude")),
                    Double.valueOf(destInfo.get("longitude")),
                    destInfo.get("type"),
                    destInfo.get("district"),
                    destInfo.get("country"),
                    state.getUser());
            dest.insert();
        }
    }

    @Then("The user has the trip")
    public void the_trip_exists() {
        Trip trip = Trip.find.query().where().eq("user", state.getUser()).findOne();
        Assert.assertNotNull(trip);
    }
}
