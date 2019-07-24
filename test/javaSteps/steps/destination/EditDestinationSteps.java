package javaSteps.steps.destination;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class EditDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to edit the destination")
    public void iWantToEditTheDestination() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/destinations/%s", state.getUser().getId(), state.getDestination().getId())));
        state.getRequest().method("PUT");
    }
}
