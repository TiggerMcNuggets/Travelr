package javaSteps.steps.trips;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class DeleteTripsSteps {

    StateSingleton state = StateSingleton.getInstance();

    @When("I want to delete the trip")
    public void i_want_to_delete_the_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("DELETE");
    }
}