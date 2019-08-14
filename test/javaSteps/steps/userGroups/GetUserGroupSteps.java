package javaSteps.steps.userGroups;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class GetUserGroupSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Sets the request for getting a user group
     */
    @When("I want to get the user group")
    public void i_want_to_get_the_user_group() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/groups/%s", state.getUser().getId(), state.getGroup().getId())));
        state.getRequest().method("GET");
    }


}
