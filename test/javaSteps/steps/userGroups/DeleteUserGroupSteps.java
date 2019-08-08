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

    @When("I want to remove the user group and all its members")
    public void i_want_to_remove_the_user_group_and_all_its_members() {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s", state.getUser().getId(), state.getGroup().getId()));
    }

    @When("I want to remove the user group with an id {long}")
    public void i_want_to_remove_the_user_group_with_an_id(Long nonExistentID) {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s", state.getUser().getId(), nonExistentID));
    }

    @Then("The user group does not exist")
    public void the_user_group_does_not_exist() {
        Grouping deletedGroup = Grouping.find.byId(state.getGroup().getId());
        Assert.assertNull(deletedGroup);
    }

    @Then("All its group members are removed")
    public void all_its_group_members_are_removed() {
        List<UserGroup> deletedGroupMembers = UserGroup.find.query().where().eq("group_id", state.getGroup().getId()).findList();
        Assert.assertEquals(0, deletedGroupMembers.size());
    }

}
