package javaSteps.steps.userGroups;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Grouping;
import models.UserGroup;
import org.junit.Assert;

import java.util.List;

public class DeleteUserGroupSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Sets request to delete a user grouping and all its members.
     */
    @When("I want to remove the user group and all its members")
    public void i_want_to_remove_the_user_group_and_all_its_members() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s/toggle_deleted", state.getUser().getId(), state.getGroup().getId()));
    }

    /**
     * Sends a request to delete a user grouping which doesn't exist.
     * @param nonExistentID The non existent user grouping id.
     */
    @When("I want to remove the user grouping with an id {long}")
    public void i_want_to_remove_the_user_group_with_an_id(Long nonExistentID) {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/grouping/%s", state.getUser().getId(), nonExistentID));
    }

    /**
     * Checks if the user grouping exists in the database
     */
    @Then("The user group does not exist")
    public void the_user_group_does_not_exist() {
        Grouping deletedGroup = Grouping.find.byId(state.getGroup().getId());
        Assert.assertNull(deletedGroup);
    }

    @Then("The user group exist")
    public void the_user_group_exist() {
        Grouping deletedGroup = Grouping.find.byId(state.getGroup().getId());
        Assert.assertNotNull(deletedGroup);
    }

    /**
     * Asserts that all the members are removed from the user grouping.
     */
    @Then("All its group members are removed")
    public void all_its_group_members_are_removed() {
        List<UserGroup> deletedGroupMembers = UserGroup.find.query().where().eq("grouping_id", state.getGroup().getId()).findList();
        Assert.assertEquals(0, deletedGroupMembers.size());
    }

    @Then("All its group members are in the group")
    public void all_its_group_members_are_in_the_group() {
        List<UserGroup> deletedGroupMembers = UserGroup.find.query().where().eq("grouping_id", state.getGroup().getId()).findList();
        Assert.assertNotEquals(0, deletedGroupMembers.size());
    }

}
