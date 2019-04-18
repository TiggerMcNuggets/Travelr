package javaSteps.steps.destination;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing getting a traveller
 */
public class GetDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Executes a fake request to get a single destination
     */
    @When("I get destination information")
    public void iGetDestinationInformation() {
        try {
            // Create request object
            Http.RequestBuilder info = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/destinations/" + state.getDestinationId());

            // Send request
            state.setResult(route(state.getApplication(), info));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

}
