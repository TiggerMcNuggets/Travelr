package javaSteps.steps.destination;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Destination;
import org.junit.Assert;

public class DeleteDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to soft delete the destination")
    public void i_want_to_soft_delete_the_destination() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/destinations/%s/toggle_deleted", state.getUser().getId(), state.getDestination().getId()));
    }

    @Then("The destination does not exist")
    public void the_destination_does_not_exist() {
        Destination destination = Destination.find.findByIdIncludeDeleted(state.getDestination().getId());
        Assert.assertTrue(destination.isDeleted());
    }

    @Then("The destination does exist")
    public void the_destination_does_exist() {
        Assert.assertEquals(Destination.find.findById(state.getDestination().getId()), state.getDestination());
    }
}

