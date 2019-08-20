
package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.DestinationNode;
import models.Grouping;
import models.Node;
import models.TripNode;
import org.junit.Assert;

import java.util.List;
import java.util.Map;


public class GroupTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to add the group to the trip")
    public void i_want_to_add_the_group_to_the_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s/groups/%s", state.getUser().getId(), state.getTrip().getId(), state.getGroup().getId())));
        state.getRequest().method("PUT");
    }

    @When("I want to remove the group from the trip")
    public void i_want_to_remove_the_group_from_the_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s/groups/%s", state.getUser().getId(), state.getTrip().getId(), state.getGroup().getId())));
        state.getRequest().method("PUT");
}

    @Then("the group will not be associated with the trip")
    public void the_group_will_not_be_associated_with_the_trip() {
        Grouping updatedTripGroup = Node.find.byId(state.getTrip().getId()).getUserGroup();
        Assert.assertNull(updatedTripGroup);
    }

    @Then("the group will be added to the trip")
    public void the_group_will_be_added_to_the_trip() {
        Grouping updatedTripGroup = Node.find.byId(state.getTrip().getId()).getUserGroup();
        Assert.assertEquals(state.getGroup(), updatedTripGroup);
    }

    @Given("the group is added to the trip")
    public void the_group_is_added_to_the_trip() {
        state.getTrip().setUserGroup(state.getGroup());
        state.getTrip().update();
    }
}

