package javaSteps.steps.destination;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

public class GetDestinationsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to get destinations")
    public void iWantToGetDestinations() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/destinations", state.getUser().getId())));
        state.getRequest().method("GET");
    }
}