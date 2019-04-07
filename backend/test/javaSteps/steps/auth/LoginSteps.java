package javaSteps.steps.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing logging in
 */
public class LoginSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    private JsonNode loginData;

    /**
     * Sets loginData to a JSON object with and email and password field set to the values given
     * @param email The user's email as a string
     * @param password The user's password as a string (plaintext)
     */
    @Given("I provide the email {string} and the password {string}")
    public void iProvideTheEmailAndThePassword(String email, String password) {
        ObjectMapper mapper = new ObjectMapper();
        loginData = mapper.createObjectNode();

        ((ObjectNode)loginData).put("email", email);
        ((ObjectNode)loginData).put("password", password);
    }

    /**
     * Sets loginData to an empty JSON object
     */
    @Given("I provide an incomplete login json")
    public void iProvideAnIncompleteLoginJson() {
        ObjectMapper mapper = new ObjectMapper();
        loginData = mapper.createObjectNode();
    }

    /**
     * Executes the fake request to log the user in
     */
    @When("I login")
    public void iLogin() {
        try {
            // Create request objeect
            Http.RequestBuilder login = Helpers.fakeRequest()
                    .method("POST")
                    .bodyJson(loginData)
                    .uri("https://localhost:9000/api/login");

            // Send request
            state.setResult(route(state.getApplication(), login));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }
}

