package javaSteps.steps.traveller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing deleting a traveller
 */
public class DeleteTravellerSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I delete the traveller")
    public void iDeleteTheTraveller() {
        try {
            // Create request object
            Http.RequestBuilder edit = Helpers.fakeRequest()
                    .method("DELETE")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/travellers/" + state.getTravellerId());

            // Send request
            state.setResult(route(state.getApplication(), edit));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }
}
