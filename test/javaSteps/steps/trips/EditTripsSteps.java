
package javaSteps.steps.trips;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Trip;
import models.TripDestination;
import org.junit.Assert;

import java.util.List;
import java.util.Map;


public class EditTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to edit the trip")
    public void iWantToEditTheTrip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("PUT");
    }

    @When("The trip is now")
    public void theTripIsNow(List<Map<String, String>> dataTable) {
        Map<String, String> tripInfo = dataTable.get(0);
        Trip trip = Trip.find.findByIdIncludeDeleted(state.getTrip().getId());

        Assert.assertEquals(trip.getName(), tripInfo.get("name"));
        Assert.assertEquals(trip.getDescription(), tripInfo.get("description"));

    }

    @Then("The trip's destinations are")
    public void theTripSDestinationsAre(List<Map<String, String>> dataTable) {
        List<TripDestination> tripDestinations = TripDestination.find.query().where().eq("trip.id", state.getTrip().getId()).findList();

        Assert.assertEquals(tripDestinations.size(), dataTable.size());

        for (Map<String, String> destInfo : dataTable) {
            TripDestination tripDestination = TripDestination.find.query().where().eq(
                    "trip.id", state.getTrip().getId()).and().eq(
                    "destination.id", Long.valueOf(destInfo.get("destinationId"))).findOne();

            Assert.assertEquals(tripDestination.getOrdinal(), Integer.parseInt(destInfo.get("ordinal")));
            Assert.assertEquals(tripDestination.getCustomName(), destInfo.get("customName"));
            Assert.assertEquals(tripDestination.getOrdinal(), Integer.parseInt(destInfo.get("arrivalDate")));
            Assert.assertEquals(tripDestination.getOrdinal(), Integer.parseInt(destInfo.get("departureDate")));
            Assert.assertEquals(tripDestination.getOrdinal(), Integer.parseInt(destInfo.get("depth")));
        }
    }
}

