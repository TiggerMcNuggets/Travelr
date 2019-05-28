package javaSteps.steps.traveller;

import com.fasterxml.jackson.databind.JsonNode;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;


/**
 * Contains steps for testing editing a traveller
 */
public class EditTravellerSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    private JsonNode editData;

    /**
     * Sets editData to a JSON object that has all fields validated
     */
    @Given("I provide a complete editTraveller json")
    public void iProvideACompleteEditTravellerJson() {
        editData = Json.parse("{ \"firstName\": \"string\", \"middleName\": \"string\", \"lastName\": \"string\", \"dateOfBirth\": 0, \"gender\": \"string\", \"email\": \"string@string.string\", \"password\": \"string\", \"nationalities\": [ { \"id\": 1, \"hasPassport\": true } ], \"travellerTypes\": [1], \"accountType\": 0 }");
    }
    /**
     * Sets editData to an empty JSON object
     */
    @Given("I provide an incomplete editTraveller json")
    public void iProvideAnIncompleteEditTravellerJson() {
        editData = Json.parse("{}");
    }

    /**
     * Executes a fake request to edit the traveller
     */
    @When("I edit the traveller")
    public void iEditTheTraveller() {
        try {
            // Create request object
            Http.RequestBuilder edit = Helpers.fakeRequest()
                    .method("PUT")
                    .bodyJson(editData)
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
