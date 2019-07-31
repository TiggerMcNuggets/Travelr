package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

public class GetTripSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();


    @When("I want to get the trip")
    public void i_want_to_get_the_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("GET");
    }

}
