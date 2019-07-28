package javaSteps.steps.destination;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Destination;
import org.junit.Assert;
import play.libs.Json;

import static play.test.Helpers.contentAsString;

public class CreateDestinationSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to create a destination")
    public void iWantToCreateADestination() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/destinations", state.getUser().getId())));
        state.getRequest().method("POST");
    }

    @When("I check the destination")
    public void iCheckTheDestination() {
        state.setDestination(Destination.find.findById(Json.parse(contentAsString(state.getResult())).get("id").asLong()));
    }

    @Then("The destination belongs to the user")
    public void theDestinationBelongsToTheUser() {
        Assert.assertEquals(state.getDestination().getUser(), state.getUser());
    }
}
