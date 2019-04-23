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
 * Contains steps for testing creating a traveller
 */
public class CreateTravellerSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    private JsonNode signupData;

    /**
     * Sets signupData to a JSON object that has all fields validated
     */
    @Given("I provide a complete traveller json")
    public void iProvideACompleteTravellerJson() {
        signupData = Json.parse("{ \"firstName\": \"string\", \"middleName\": \"string\", \"lastName\": \"string\", \"dateOfBirth\": 0, \"gender\": \"string\", \"email\": \"string@string.string\", \"password\": \"string\", \"nationalities\": [ { \"id\": 1, \"hasPassport\": true } ], \"travellerTypes\": [1], \"accountType\": 0 }");
    }

    /**
     * Sets signupData to an empty JSON object
     */
    @Given("I provide an incomplete traveller json")
    public void iProvideAnIncompleteTravellerJson() {
        signupData = Json.parse("{}");
    }

    /**
     * Sets the email field in signupData to the given email
     * @param email the given email as a string
     */
    @Given("I provide the email {string}")
    public void iProvideTheEmail(String email) {
        ((ObjectNode)signupData).put("email", email);
    }

    /**
     * Executes a fake request to sign a user up
     */
    @When("I signup")
    public void iSignup() {
        try {
            // Create request object
            Http.RequestBuilder signup = Helpers.fakeRequest()
                    .method("POST")
                    .bodyJson(signupData)
                    .uri("https://localhost:9000/api/travellers");

            // Send request
            state.setResult(route(state.getApplication(), signup));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }
}