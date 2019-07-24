//package javaSteps.steps.trips;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import javaSteps.models.StateSingleton;
//import org.junit.Assert;
//import play.libs.Json;
//import play.mvc.Http;
//import play.test.Helpers;
//
//import static play.test.Helpers.contentAsString;
//import static play.test.Helpers.route;
//
//public class DeleteTripsSteps {
//
//    // Singleton object that holds shared values across steps
//    private StateSingleton state = StateSingleton.getInstance();
//
//    @When("I soft delete trip with id {int}")
//    public void i_soft_delete_trip_with_id(Integer trip_id) {
//        try {
//            Http.RequestBuilder deleteTrip = Helpers.fakeRequest()
//                    .method("PUT")
//                    .header("X-Authorization", state.getToken())
//                    .uri("https://localhost:9000/api/users/" + this.state.getTravellerId() + "/trips/" + trip_id  + "/toggle_deleted");
//
//            // Send request
//            state.setResult(route(state.getApplication(), deleteTrip));
//
//            JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
//        } catch (Exception e) {
//            System.out.println(e);
//            Assert.assertTrue(false);
//        }
//    }
//
//    @When("I undo the deletion status on trip with id {int}")
//    public void i_undo_the_deletion_status_on_trip_with_id(Integer trip_id) {
//        try {
//            Http.RequestBuilder deleteTrip = Helpers.fakeRequest()
//                    .method("PUT")
//                    .header("X-Authorization", state.getToken())
//                    .uri("https://localhost:9000/api/users/" + this.state.getTravellerId() + "/trips/" + trip_id  + "/toggle_deleted");
//
//            // Send request
//            state.setResult(route(state.getApplication(), deleteTrip));
//
//            JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
//        } catch (Exception e) {
//            System.out.println(e);
//            Assert.assertTrue(false);
//        }
//    }
//
//    @Then("in the list of trips the trip with id {int} will not be present.")
//    public void in_the_list_of_trips_the_trip_with_id_will_not_be_present(Integer tripId) {
//        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
//        boolean isFound = false;
//        for (JsonNode trip : responseBody) {
//            if (trip.get("id").asInt() == tripId) {
//                isFound = true;
//            }
//        }
//        Assert.assertFalse(isFound);
//    }
//
//    @Then("in the list of trips the trip with id {int} will be present")
//    public void in_the_list_of_trips_the_trip_with_id_will_be_present(Integer tripId) {
//        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
//        boolean isFound = false;
//        for (JsonNode trip : responseBody) {
//            if (trip.get("id").asInt() == tripId) {
//                isFound = true;
//            }
//        }
//        Assert.assertTrue(isFound);
//    }
//
//}
