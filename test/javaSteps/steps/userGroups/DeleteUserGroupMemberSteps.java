package javaSteps.steps.userGroups;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.User;
import models.UserGroup;
import org.junit.Assert;

public class DeleteUserGroupMemberSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Creates a user and adds this sets this as the grouping member but they are not added to the grouping.
     * Used for testing 404
     */
    @Given("the user group does not have another group member")
    public void the_user_group_does_not_have_another_group_member() {
        User newGroupMember = new User(
                "Invisible",
                "Person",
                "nothing@nothing.com",
                0
        );
        newGroupMember.insert();
        state.setGroupMember(newGroupMember);
    }

    /**
     * Sets request to delete the added grouping member from the grouping
     */
    @When("I want to remove the group member in the user group")
    public void i_want_to_remove_the_group_member_in_the_user_group() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s/member/%s/toggle_deleted", state.getUser().getId(), state.getGroup().getId(), state.getGroupMember().getId()));
    }

    /**
     * Checks that a grouping member does not exist in a grouping.
     */
    @Then("The group member does not exist in the user group")
    public void the_group_member_does_not_exist_in_the_user_group() {
        UserGroup deletedGroupMember = UserGroup.find.query().setIncludeSoftDeletes().where().eq("user_id", state.getGroupMember().getId()).findOne();
        Assert.assertNotNull(deletedGroupMember);
        Assert.assertTrue(deletedGroupMember.isDeleted());
    }

}
