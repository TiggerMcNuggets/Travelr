package javaSteps.steps.travellerTrips;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;
public class getTravellerTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();
    private JsonNode tripData;

    @When("I get all trips for traveller {string}")
    public void i_get_all_trips_for_traveller(String user_id) {
        try {
            // Create request object
            Http.RequestBuilder getTravellerTrips = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/travellers/"+user_id+"/trips");

            // Send request
            state.setResult(route(state.getApplication(), getTravellerTrips));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

}
