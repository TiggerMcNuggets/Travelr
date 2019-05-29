package javaSteps.steps.trips;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

public class EditTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();
    private JsonNode tripData;

    @Given("I provide the invalid token {string}")
    public void i_provide_the_invalid_token(String string) {
        state.setToken(string);
    }

    @Given("I provide the trip id {string}")
    public void i_provide_the_trip_id(String string) {
        state.setTripId(string);
    }

    @Given("A trip with id {string} exists")
    public void a_trip_with_id_exists(String string) {
        try {
            // Create request object
            Http.RequestBuilder getTrip = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/users/" + state.getTravellerId() + "/trips/" + string);

            // Send request
            state.setResult(route(state.getApplication(), getTrip));
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @When("I provide trip information missing name")
    public void i_provide_trip_information_missing_name() {
        try {
            tripData = Json.parse("{" +
                    "    \"name\": \"\"," +
                    "    \"destinations\": [" +
                    "        {" +
                    "            \"id\": 1," +
                    "            \"ordinal\": 1," +
                    "            \"arrivalDate\": 0," +
                    "            \"departureDate\": 12" +
                    "        }," +
                    "        {" +
                    "            \"id\": 2," +
                    "            \"ordinal\": 2," +
                    "            \"arrivalDate\": 23," +
                    "            \"departureDate\": 34" +
                    "        }" +
                    "    ]" +
                    "}");
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @When("I provide trip information with one destination")
    public void i_provide_trip_information_with_one_destination() {
        try {
            tripData = Json.parse("{" +
                    "    \"name\": \"New Zealand\"," +
                    "    \"destinations\": [" +
                    "        {" +
                    "            \"id\": 1," +
                    "            \"ordinal\": 1," +
                    "            \"arrivalDate\": 0," +
                    "            \"departureDate\": 12" +
                    "        }" +
                    "    ]" +
                    "}");
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @When("I provide trip information with two consecutive identical destinations")
    public void i_provide_trip_information_with_two_consecutive_identical_destinations() {
        try {
            tripData = Json.parse("{" +
                    "    \"name\": \"New Zealand\"," +
                    "    \"destinations\": [" +
                    "        {" +
                    "            \"id\": 1," +
                    "            \"ordinal\": 1," +
                    "            \"arrivalDate\": 0," +
                    "            \"departureDate\": 12" +
                    "        }," +
                    "        {" +
                    "            \"id\": 1," +
                    "            \"ordinal\": 2," +
                    "            \"arrivalDate\": 23," +
                    "            \"departureDate\": 34" +
                    "        }" +
                    "    ]" +
                    "}");
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Given("A trip with id {string} does not exist")
    public void a_trip_with_id_does_not_exist(String string) {
        try {
            // Create request object
            Http.RequestBuilder getTrip = Helpers.fakeRequest()
                    .method("GET")
                    .header("X-Authorization", state.getToken())
                    .uri("http://localhost:9000/api/trips/" + string);

            // Send request
            state.setResult(route(state.getApplication(), getTrip));
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Given("I do not provide a token")
    public void i_do_not_provide_a_token() {
        state.setToken(null);
    }

    @When("I edit a trip")
    public void i_edit_a_trip() {
        try {
            System.out.println(state.getToken());
            // Create request object
            Http.RequestBuilder getTrip = Helpers.fakeRequest()
                    .method("PUT")
                    .header("X-Authorization", state.getToken())
                    .bodyJson(tripData)
                    .uri("http://localhost:9000/api/users/" + state.getTravellerId() + "/trips/" + state.getTripId());

            // Send request
            state.setResult(route(state.getApplication(), getTrip));
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Given("provide complete trip information")
    public void provide_complete_trip_information() {
        try {
            tripData = Json.parse("{" +
                    "    \"name\": \"New Zealand\"," +
                    "    \"destinations\": [" +
                    "        {" +
                    "            \"id\": 1," +
                    "            \"ordinal\": 1," +
                    "            \"arrivalDate\": 0," +
                    "            \"departureDate\": 12" +
                    "        }," +
                    "        {" +
                    "            \"id\": 2," +
                    "            \"ordinal\": 2," +
                    "            \"arrivalDate\": 23," +
                    "            \"departureDate\": 34" +
                    "        }" +
                    "    ]" +
                    "}");
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Given("the trip has name of the {string}")
    public void the_trip_has_name_of_the(String tripName) {
        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
        Assert.assertEquals(responseBody.get("name").asText(), tripName);
    }

    @Given("provide complete trip information with name {string}")
    public void provide_complete_trip_information_with_name(String tripName) {
        try {
            tripData = Json.parse("{" +
                    "    \"name\": \"" + tripName + "\"," +
                    "    \"destinations\": [" +
                    "        {" +
                    "            \"id\": 1," +
                    "            \"ordinal\": 1," +
                    "            \"arrivalDate\": 0," +
                    "            \"departureDate\": 12" +
                    "        }," +
                    "        {" +
                    "            \"id\": 2," +
                    "            \"ordinal\": 2," +
                    "            \"arrivalDate\": 13," +
                    "            \"departureDate\": 14" +
                    "        }" +
                    "    ]" +
                    "}");
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Then("the trip has name {string}")
    public void the_trip_has_name(String tripName) {
        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
        Assert.assertEquals(responseBody.get("name").asText(), tripName);
    }

}
