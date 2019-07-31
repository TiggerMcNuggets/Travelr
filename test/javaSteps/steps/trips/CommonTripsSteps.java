package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import models.Destination;
import models.Trip;
import models.TripDestination;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class CommonTripsSteps {

    StateSingleton state = StateSingleton.getInstance();


    @Given("I own the trip")
    public void i_own_the_trip(List<Map<String, String>> dataTable) {
        for (Map<String, String> tripData : dataTable) {
            Trip newTrip = new Trip(tripData.get("name"), tripData.get("description"), state.getUser());
            newTrip.insert();
            state.setTrip(newTrip);
        }
    }

    @Given("They own the trip")
    public void they_own_the_trip(List<Map<String, String>> dataTable) {
        for (Map<String, String> tripData : dataTable) {
            Trip newTrip = new Trip(tripData.get("name"), tripData.get("description"), state.getUser());
            newTrip.insert();
            state.setTrip(newTrip);
        }
    }

    @Given("The trip contains the trip destinations")
    public void the_trip_contains_the_trip_destinations(List<Map<String, String>> dataTable) {
        for (Map<String, String> destinationData : dataTable) {
            TripDestination destination = new TripDestination(
                    destinationData.get("customName"),
                    Integer.valueOf(destinationData.get("ordinal")),
                    Integer.valueOf(destinationData.get("depth")),
                    Integer.valueOf(destinationData.get("arrivalDate")),
                    Integer.valueOf(destinationData.get("departureDate")),
                    Destination.find.findById(Long.valueOf(destinationData.get("destinationId"))));
            state.getTrip().getDestinations().add(destination);
            state.getTrip().update();
        }
    }

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
