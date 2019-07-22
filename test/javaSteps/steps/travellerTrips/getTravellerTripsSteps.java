//package javaSteps.steps.travellerTrips;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import cucumber.api.java.en.When;
//import javaSteps.models.StateSingleton;
//import org.junit.Assert;
//import play.mvc.Http;
//import play.test.Helpers;
//
//import static play.test.Helpers.route;
//public class getTravellerTripsSteps {
//
//    // Singleton object that holds shared values across steps
//    private StateSingleton state = StateSingleton.getInstance();
//
//    @When("I get all trips for traveller")
//    public void i_get_all_trips_for_traveller()  {
//        try {
//
//            String url = "http://localhost:9000/api/travellers/" + state.getTravellerId() + "/trips";
//
//            // Create request object
//            Http.RequestBuilder getTravellerTrips = Helpers.fakeRequest()
//                    .method("GET")
//                    .header("X-Authorization", state.getToken())
//                    .uri(url);
//
//            // Send request
//            state.setResult(route(state.getApplication(), getTravellerTrips));
//
//        } catch (Exception e) {
//            System.err.println(e);
//            Assert.assertTrue(false);
//        }
//    }
//
//}
