package javaSteps.steps.trips;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class GetTripSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to get the trips album")
    public void i_want_to_get_the_trips_album() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/albums/%s", state.getUser().getId(), state.getTrip().getDefaultAlbum().getId())));
        state.getRequest().method("GET");
    }

    @When("I want to get the trip")
    public void i_want_to_get_the_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("GET");
    }

}
