package javaSteps.steps.Media;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class UploadMediaSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to upload media")
    public void iWantToUploadMedia() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/albums/%s ", state.getUser().getId(), state.getAlbum().getId())));
        state.getRequest().method("POST");
    }

    @Then("The media exists")
    public void theMediaExists() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

}
