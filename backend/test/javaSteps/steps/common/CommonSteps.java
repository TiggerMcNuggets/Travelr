package javaSteps.steps.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

/**
 * Contains common steps shared across many features
 */
public class CommonSteps  {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Populates the database and checks that it has all gone correctly
     */
    @Given("I populate the database")
    public void iPopulateTheDatabase() {
        try {
            // Create request object
            Http.RequestBuilder resample = Helpers.fakeRequest()
                    .method("POST")
                    .uri("https://localhost:9000/api/populate");

            // Send request
            state.setResult(route(state.getApplication(), resample));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    /**
     * Sets the token to the given token value
     * @param token the given token from the feature file
     */
    @Given("I provide the token {string}")
    public void iProvideTheToken(String token) {
        state.setToken(token);
    }

    /**
     * Sets the traveller id to the given id value
     */
    @Given("The traveller id is {int}")
    public void theTravellerIdIs(int id) {state.setTravellerId(id);}

    /**
     * Sets the destination id to the given id value
     */
    @Given("The destination id is {int}")
    public void theDestinationIdIs(int id) {state.setDestinationId(id);}

    @Given("I am logged in as a non-admin user")
    public void iAmLoggedInAsANonAdminUser() { state.setToken("abc");}

    /**
     * Asserts that the actual response is equal to the expected response
     * @param responseCode the three digit expected response code
     */
    @Then("I will receive a {int} response")
    public void iWillReceiveAResponse(int responseCode) {
        Assert.assertEquals(responseCode, state.getResult().status());
    }

    /**
     * Asserts that the actual id is equal to the expected id
     * @param id the expected id
     */
    @Then("I will receive the id {int}")
    public void iWillReceiveMyAccountIdAndAToken(int id) {
        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
        Assert.assertTrue(responseBody.get("id").asInt() == id);
    }

    @Then("I will receive the email {string}")
    public void i_will_receive_the_email(String email) {
        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
        Assert.assertEquals(responseBody.get("email").asText(), email);
    }

    @Then("I will receive not receive the email back")
    public void i_will_receive_not_receive_the_email_back() {
        boolean noEmail = false;
        try {
            JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
            responseBody.get("email").asText();
        } catch (NullPointerException e) {
            noEmail = true;
        } finally {
            Assert.assertTrue(noEmail);
        }
    }
}
