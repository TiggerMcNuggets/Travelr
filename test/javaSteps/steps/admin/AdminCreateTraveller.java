package javaSteps.steps.admin;

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
 * Contains steps for testing creating a traveller
 */
public class AdminCreateTraveller {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();
    private JsonNode signUpData;
    private String email = "string@string.com";

    @Given("I provide a complete normal traveller json")
    public void i_provide_a_complete_normal_traveller_json() {
        signUpData = Json.parse("{ \"firstName\": \"string\", " +
                "\"middleName\": \"string\", " +
                "\"lastName\": \"string\"," +
                " \"dateOfBirth\": 0, \"gender\": " +
                "\"string\", \"email\": \"" + email + "\", " +
                "\"password\": \"string\", " +
                "\"nationalities\": [ { \"id\": 1, \"hasPassport\": true } ], " +
                "\"travellerTypes\": [1], \"accountType\": 0 }");
    }

    @Given("I provide an email {string}")
    public void i_provide_an_email(String string) {
        email = string;
    }

    @When("I create traveller")
    public void i_create_traveller() {
        try {
            // Create request object
            Http.RequestBuilder signup = Helpers.fakeRequest()
                    .method("POST")
                    .bodyJson(signUpData)
                    .uri("https://localhost:9000/api/travellers");

            // Send request
            state.setResult(route(state.getApplication(), signup));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Given("I provide a complete admin traveller json")
    public void i_provide_a_complete_admin_traveller_json() {
        signUpData = Json.parse("{ \"firstName\": \"string\", " +
                "\"middleName\": \"string\", " +
                "\"lastName\": \"string\"," +
                " \"dateOfBirth\": 0, \"gender\": " +
                "\"string\", \"email\": \"" + email + "\", " +
                "\"password\": \"string\", " +
                "\"nationalities\": [ { \"id\": 1, \"hasPassport\": true } ], " +
                "\"travellerTypes\": [1], \"accountType\": 1 }");
    }
}