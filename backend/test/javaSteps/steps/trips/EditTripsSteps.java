package javaSteps.steps.trips;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

public class EditTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();
    private JsonNode tripData;
    private String tripId;

    @Given("I provide the trip id {string}")
    public void i_provide_the_trip_id(String string) {
        tripId = string;
    }

    @Given("A trip with id {string} exists")
    public void a_trip_with_id_exists(String string) {
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
            // Create request object
            Http.RequestBuilder getTrip = Helpers.fakeRequest()
                    .method("PUT")
                    .header("X-Authorization", state.getToken())
                    .bodyJson(tripData)
                    .uri("http://localhost:9000/api/trips/" + tripId);

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

}
