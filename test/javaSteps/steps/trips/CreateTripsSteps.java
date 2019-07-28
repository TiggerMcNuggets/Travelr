//package javaSteps.steps.trips;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.When;
//import javaSteps.models.StateSingleton;
//import org.junit.Assert;
//import play.libs.Json;
//import play.mvc.Http;
//import play.test.Helpers;
//
//import static play.test.Helpers.route;
//
//public class CreateTripsSteps {
//
//    // Singleton object that holds shared values across steps
//    private StateSingleton state = StateSingleton.getInstance();
//    private JsonNode tripData;
//
//
//
//    @Given("I provide complete trip information")
//    public void i_provide_complete_trip_information() {
//        tripData = Json.parse("{" +
//                "    \"name\": \"string123\"," +
//                "    \"destinations\": [" +
//                "        {" +
//                "            \"id\": 1," +
//                "            \"ordinal\": 1," +
//                "            \"arrivalDate\": 0," +
//                "            \"departureDate\": 12" +
//                "        }," +
//                "        {" +
//                "            \"id\": 2," +
//                "            \"ordinal\": 2," +
//                "            \"arrivalDate\": 23," +
//                "            \"departureDate\": 34" +
//                "        }" +
//                "    ]" +
//                "}");
//    }
//
//    @Given("I provide incomplete trip information")
//    public void i_provide_incomplete_trip_information() {
//        tripData = Json.parse("{}");
//    }
//
//    @Given("I provide trip information where I have no trip name")
//    public void i_provide_complete_trip_information_where_I_have_no_name() {
//        tripData = Json.parse("{" +
//                "    \"destinations\": [" +
//                "        {" +
//                "            \"id\": 1," +
//                "            \"ordinal\": 1," +
//                "            \"arrivalDate\": 0," +
//                "            \"departureDate\": 12" +
//                "        }," +
//                "        {" +
//                "            \"id\": 2," +
//                "            \"ordinal\": 2," +
//                "            \"arrivalDate\": 23," +
//                "            \"departureDate\": 34" +
//                "        }" +
//                "    ]" +
//                "}");
//    }
//
//    @Given("I provide trip information where the same destination appears consecutively")
//    public void i_provide_trip_information_where_the_same_destination_appears_consecutively() {
//        tripData = Json.parse("{" +
//                "    \"name\": \"string123\"," +
//                "    \"destinations\": [" +
//                "        {" +
//                "            \"id\": 1," +
//                "            \"ordinal\": 1," +
//                "            \"arrivalDate\": 0," +
//                "            \"departureDate\": 12" +
//                "        }," +
//                "        {" +
//                "            \"id\": 1," +
//                "            \"ordinal\": 2," +
//                "            \"arrivalDate\": 23," +
//                "            \"departureDate\": 34" +
//                "        }" +
//                "    ]" +
//                "}");
//    }
//
//    @Given("I provide trip information where only one destination is given")
//    public void i_provide_trip_information_where_only_one_destination_is_given() {
//        tripData = Json.parse("{" +
//                "    \"name\": \"string123\"," +
//                "    \"destinations\": [" +
//                "        {" +
//                "            \"id\": 1," +
//                "            \"ordinal\": 1," +
//                "            \"arrivalDate\": 0," +
//                "            \"departureDate\": 12" +
//                "        }" +
//                "    ]" +
//                "}");
//    }
//
//    @When("I create a trip")
//    public void i_create_a_trip() {
//        try {
//            // Create request object
//            Http.RequestBuilder createTrip = Helpers.fakeRequest()
//                    .method("POST")
//                    .header("X-Authorization", state.getToken())
//                    .bodyJson(tripData)
//                    .uri("http://localhost:9000/api/users/" + state.getTravellerId() + "/trips");
//
//            // Send request
//            state.setResult(route(state.getApplication(), createTrip));
//
//        } catch (Exception e) {
//            System.err.println(e);
//            Assert.assertTrue(false);
//        }
//    }
//
//    @When("I create a trip omitting auth token as header")
//    public void i_create_a_trip_omitting_auth_token_as_header() {
//        try {
//            // Create request object
//            Http.RequestBuilder createTrip = Helpers.fakeRequest()
//                    .method("POST")
//                    .bodyJson(tripData)
//                    .uri("http://localhost:9000/api/users/" + state.getTravellerId() + "/trips");
//
//            // Send request
//            state.setResult(route(state.getApplication(), createTrip));
//
//        } catch (Exception e) {
//            System.err.println(e);
//            Assert.assertTrue(false);
//        }
//    }
//
//
//}
