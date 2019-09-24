
package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.DestinationNode;
import models.Node;
import models.TripNode;
import org.junit.Assert;

import java.util.List;
import java.util.Map;


public class DeleteTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to delete the trip")
    public void iWantToDeleteTheTrip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s/toggle_deleted", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("PUT");
    }
}

