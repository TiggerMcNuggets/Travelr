package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

public class GetTripSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();
    private String tripId;



    @Given("I provide a tripId of {string}")
    public void i_provide_a_tripId_of(String string) {
        tripId = string;
    }

    @When("I get a trip without an auth token")
    public void i_get_a_trip_without_an_auth_token() {
        try {
            // Create request object
            Http.RequestBuilder getTrip = Helpers.fakeRequest()
                    .method("GET")
                    .uri("https://localhost:9000/api/users/" + this.state.getTravellerId() + "/trips/" + tripId);

            // Send request
            state.setResult(route(state.getApplication(), getTrip));
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @When("I get a trip")
    public void i_get_a_trip() {
        try {
            // Create request object
            Http.RequestBuilder getTrip = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/users/" + this.state.getTravellerId() + "/trips/" + tripId);
            System.out.println("https://localhost:9000/api/users/" + this.state.getTravellerId() + "/trips/" + tripId);
            // Send request
            state.setResult(route(state.getApplication(), getTrip));
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }

    }


}