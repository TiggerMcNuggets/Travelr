package javaSteps.steps.traveller;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

/**
 * Contains steps for testing deleting a traveller
 */
public class DeleteTravellerSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to delete the account")
    public void iWantToDeleteTheAccount() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/travellers/%s/toggle_deleted", state.getUser().getId()));
    }

}
