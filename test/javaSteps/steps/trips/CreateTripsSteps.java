package javaSteps.steps.trips;

import cucumber.api.java.en.When;
import finders.NodeFinder;
import finders.TripNodeFinder;
import javaSteps.models.StateSingleton;
import models.TripNode;

public class CreateTripsSteps {

    StateSingleton state = StateSingleton.getInstance();
    TripNodeFinder tripNodeFinder = new TripNodeFinder();

    @When("I want to create a trip")
    public void i_want_to_create_a_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips", state.getUser().getId())));
        state.getRequest().method("POST");
    }

    @When("I want to add destinations to my trip which has id {int}")
    public void i_want_to_add_destinations_to_my_trip_which_had_id(int int1) {
        Long tripId = Long.valueOf(int1);
        TripNode createdTrip = TripNode.find.byId(tripId);
        state.setTrip(createdTrip);
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("PUT");
    }
}
