package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.DestinationNode;
import models.Node;
import models.NodeUserStatus;
import models.TripStatus;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class UpdateTripUserStatusSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();


    @When("I want to change my status for this trip")
    public void i_want_to_change_my_status_for_this_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s/status", state.getUser().getId(), state.getTrip().getId())));
        state.getRequest().method("PUT");
    }

    @Then("my status for this trip is {string}")
    public void my_status_for_this_trip_is(String status) {
        NodeUserStatus userStatus = NodeUserStatus.find.query().where().eq("trip", state.getTrip()).eq("user", state.getUser()).findOne();
        Assert.assertEquals(TripStatus.valueOf(status), userStatus.getTripStatus());
    }

    @Given("the trip has this destination added as a part of the trip")
    public void the_trip_has_this_destination_added_as_a_part_of_the_trip() {
        DestinationNode dest_node = new DestinationNode("Custom Destination 2 Name", state.getUser(), state.getDestination());
        dest_node.setOrdinal(0);

        dest_node.setParent(state.getTrip());
        dest_node.save();
    }

    @Then("the status for the sub destination are also {string}")
    public void the_status_for_the_sub_destination_are_also(String status) {
        List< Node > childrenDestinations = Node.find.query().where().eq("parent", state.getTrip()).eq("dtype", "destination").findList();

        for (Node destinationNode : childrenDestinations) {
            NodeUserStatus userDestStatus = NodeUserStatus.find.query().where().eq("user", state.getUser()).eq("node_id", destinationNode.id).findOne();
            Assert.assertEquals(status, userDestStatus);
        }

    }

}
