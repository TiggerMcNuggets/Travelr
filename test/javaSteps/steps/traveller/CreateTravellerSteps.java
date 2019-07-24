package javaSteps.steps.traveller;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

/**
 * Contains steps for testing creating a traveller
 */
public class CreateTravellerSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to sign up")
    public void iWantToSignUp() {
        state.getRequest().method("POST");
        state.getRequest().uri("https://localhost:9000/api/travellers");
    }
}