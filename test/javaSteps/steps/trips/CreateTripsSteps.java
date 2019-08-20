package javaSteps.steps.trips;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class CreateTripsSteps {

    StateSingleton state = StateSingleton.getInstance();

    @When("I want to create a trip")
    public void i_want_to_create_a_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips", state.getUser().getId())));
        state.getRequest().method("POST");
    }

}
