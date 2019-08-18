package javaSteps.steps.userGroups;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class GetUserGroupsSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to get all of the groups that belongs to the user")
    public void iWantToGetAllOfTheGroupsThatBelongsToTheUser() {
        state.getRequest().method("GET");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group", state.getUser().getId()));
    }
}
