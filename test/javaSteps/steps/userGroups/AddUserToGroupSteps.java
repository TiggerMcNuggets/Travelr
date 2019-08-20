package javaSteps.steps.userGroups;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.User;
import models.UserGroup;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static javaSteps.steps.common.CommonSteps.createUserFromTable;

public class AddUserToGroupSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Creates and inserts user and sets it to group member in state to be used for request URL in the future
     * @param dataTable The user data
     */
    @Given("The future member exists")
    public void theFutureMemberExists(List<Map<String, String>> dataTable) {
        User newGroupMember = createUserFromTable(dataTable);
        newGroupMember.insert();
        state.setGroupMember(newGroupMember);
    }

    /**
     * Creates user without inserting and sets it to group member in state to be used for request URL in the future
     * @param dataTable The user data
     */
    @Given("The future member does not exist")
    public void theFutureMemberDoesNotExist(List<Map<String, String>> dataTable) {
        User newGroupMember = createUserFromTable(dataTable);
        newGroupMember.setId(100L);
        state.setGroupMember(newGroupMember);
    }

    /**
     * Sets request to add a user to group
     */
    @When("I want to add the user to the group")
    public void iWantToAddTheUserToTheGroup() {
        state.getRequest().method("POST");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s/member/%s", state.getUser().getId(), state.getGroup().getId(), state.getGroupMember().getId()));
    }

    /**
     * Checks if the user exists in the group
     */
    @Then("The user now exists in the group")
    public void theUserNowExistsInTheGroup() {
        Assert.assertNotNull(findUserGroup());
    }

    /**
     * Checks if the user still does not exist in the group
     */
    @Then("The user still does not exist in the group")
    public void theUserStillDoesNotExistInTheGroup() {
        Assert.assertNull(findUserGroup());
    }

    /**
     * Checks if the user is an owner of the group
     */
    @Then("The user is now an owner of the group")
    public void theUserIsNowAnOwnerOfTheGroup() {
        Assert.assertTrue(findUserGroup().isOwner());
    }

    /**
     * Checks if the user is a normal member of the group
     */
    @Then("The user is now a normal member of the group")
    public void theUserIsNowANormalMemberOfTheGroup() {
        Assert.assertFalse(findUserGroup().isOwner());
    }

    /**
     * Finds user group by user and group objects
     * @return
     */
    private UserGroup findUserGroup() {
        return UserGroup
                .find
                .query()
                .where()
                .eq("user", state.getGroupMember())
                .and()
                .eq("grouping", state.getGroup())
                .findOne();
    }
}
