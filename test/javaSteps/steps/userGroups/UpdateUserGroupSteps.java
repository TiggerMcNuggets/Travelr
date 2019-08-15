package javaSteps.steps.userGroups;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Grouping;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class UpdateUserGroupSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Sets the request to the endpoint and method to edit a user grouping.
     */
    @When("I want to edit the user group")
    public void i_want_to_edit_the_user_group() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s", state.getUser().getId(), state.getGroup().getId()));
    }

    /**
     * Verifies the user grouping matches the specified data
     * @param dataTable The data to check against.
     */
    @Then("The user group is")
    public void the_user_group_is(List<Map<String, String>> dataTable) {
        Grouping grouping = Grouping.find.byId(state.getGroup().getId());
        Map<String, String> albumInfo = dataTable.get(0);

        Assert.assertEquals(albumInfo.get("name"), grouping.getName());
        Assert.assertEquals(albumInfo.get("description"), grouping.getDescription());
    }

}
