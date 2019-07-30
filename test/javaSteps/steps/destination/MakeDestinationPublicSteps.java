package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import finders.DestinationFinder;
import javaSteps.models.StateSingleton;
import models.Destination;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

/**
 * Contains steps for testing getting a traveller
 */
public class MakeDestinationPublicSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to make the destination public")
    public void iWantToMakeTheDestinationPublic() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/destinations/%s/make_public", state.getDestination().getId()));
    }

    @When("I want to make the destination private")
    public void iWantToMakeTheDestinationPrivate() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/destinations/%s/make_private", state.getDestination().getId()));
    }

    @Then("The destination is now public")
    public void theDestinationIsNowPublic() {
        Assert.assertTrue((Destination.find.findById(state.getDestination().getId())).isPublic());
    }

    @Then("The destination is now private")
    public void theDestinationIsNowPrivate() {
        Assert.assertFalse((Destination.find.findById(state.getDestination().getId())).isPublic());
    }

}
