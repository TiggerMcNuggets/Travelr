package javaSteps.steps.traveller;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing getting travellers
 */
public class GetTravellersSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Executes a fake request to get travellers
     */
    @When("I get travellers")
    public void iGetTravellers() {
        try {
            // Create request object
            Http.RequestBuilder getTravellers = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/travellers");

            // Send request
            state.setResult(route(state.getApplication(), getTravellers));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }
}