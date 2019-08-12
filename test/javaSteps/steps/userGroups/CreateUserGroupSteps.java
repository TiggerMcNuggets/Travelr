package javaSteps.steps.userGroups;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Grouping;
import models.UserGroup;
import org.junit.Assert;

public class CreateUserGroupSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to create a group")
    public void i_want_to_create_a_group() {
        state.getRequest().method("POST");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/groups", state.getUser().getId()));
    }

}
