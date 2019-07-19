package javaSteps.steps.destination;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing getting a traveller
 */
public class GetDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to get a destination")
    public void iWantToGetADestination() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/destinations/%s", state.getUser().getId(), state.getDestination().getId())));
        state.getRequest().method("GET");
    }



//    /**
//     * Executes a fake request to get a single destination
//     */
//    @When("I get destination information")
//    public void iGetDestinationInformation() {
//        try {
//            // Create request object
//            Http.RequestBuilder info = Helpers.fakeRequest()
//                    .method("GET")
//                    .header("X-Authorization", state.getToken())
//                    .uri("https://localhost:9000/api/users/" + state.getTravellerId() + "/destinations/" + state.getDestinationId());
//
//            // Send request
//            state.setResult(route(state.getApplication(), info));
//
//        } catch (Exception e) {
//            System.out.println(e);
//            Assert.assertTrue(false);
//        }
//    }


}
