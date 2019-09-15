
package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.*;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class GroupTripsSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to add the group to the trip")
    public void i_want_to_add_the_group_to_the_trip() {
       state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s/groups/%s",
                state.getUser().getId(), state.getTrip().getId(), state.getGroup().getId())));
        state.getRequest().method("PUT");
    }

    @When("I want to remove the group from the trip")
    public void i_want_to_remove_the_group_from_the_trip() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/trips/%s/groups/%s",
                state.getUser().getId(), state.getTrip().getId(), state.getGroup().getId())));
        state.getRequest().method("DELETE");
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

    @Given("the trip is associated with my user group")
    public void the_trip_is_associated_with_the_user_group() {
        state.getTrip().setUserGroup(state.getGroup());
        state.getTrip().update();
    }

    @Given("the trip is associated with the user group")
    public void the_trip_is_associated_with_the_user_group(List<Map<String, String>> dataTable) {
        Map<String, String> groupInfo = dataTable.get(0);
        Grouping group = new Grouping(groupInfo.get("name"), groupInfo.get("description"));
        group.insert();
        state.setGroup(group);

        // Add yourself to the group
        UserGroup userGroup = new UserGroup(state.getUser(), state.getGroup(), true);
        userGroup.insert();

        state.getTrip().setUserGroup(group);
        state.getTrip().update();
    }

    @Then("all trip status values associated with the trip are removed")
    public void all_trip_status_values_associated_with_the_trip_are_removed() {
        List<NodeUserStatus> tripUserStatuses = NodeUserStatus.find.query().where().eq("trip", state.getTrip()).findList();

        for (NodeUserStatus tripStatus : tripUserStatuses) {
            Assert.assertNull(tripStatus);
        }

        for (Node dest : state.getTrip().getNodes()) {
            List<NodeUserStatus> destUserStatuses = NodeUserStatus.find.query().where().eq("trip", dest).findList();

            for (NodeUserStatus destStatus : destUserStatuses) {
                Assert.assertNull(destStatus);
            }
        }

    }

    @Given("the group has the members, ownership and statuses")
    public void the_group_has_the_members_ownership_and_statuses(List<Map<String, String>> dataTable) {

        for (Map<String, String> memberInfo : dataTable) {
            User user = new User(memberInfo.get("first"), memberInfo.get("last"), memberInfo.get("email"),
                    Integer.valueOf(memberInfo.get("dob")));

            user.insert();
            UserGroup userGroup = new UserGroup(user, state.getGroup(), Boolean.valueOf(memberInfo.get("owner")));
            userGroup.insert();

            if (!memberInfo.get("status").equals("NOT GOING")) {
                NodeUserStatus nodeUserStatus = new NodeUserStatus(user, state.getTrip(),
                        TripStatus.valueOf(memberInfo.get("status")));
                nodeUserStatus.insert();
            }
        }
    }
}
