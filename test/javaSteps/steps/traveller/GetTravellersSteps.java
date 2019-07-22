package javaSteps.steps.traveller;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing getting travellers
 */
public class GetTravellersSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to get profiles")
    public void iWantToGetProfiles() {
        state.getRequest().method("GET");
        state.getRequest().uri("https://localhost:9000/api/travellers");
    }
}