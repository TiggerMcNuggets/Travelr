package javaSteps.steps.destination;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

public class GetDestinationsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Executes a fake request to get destinations
     */
    @When("I get destinations")
    public void iGetDestinations() {
        try {
            // Create request object
            Http.RequestBuilder getDestinations = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/users/" + state.getTravellerId() + "/destinations");
            System.out.println(state.getTravellerId());

            // Send request
            state.setResult(route(state.getApplication(), getDestinations));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }



}