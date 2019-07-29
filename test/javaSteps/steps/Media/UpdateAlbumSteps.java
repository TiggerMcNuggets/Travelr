package javaSteps.steps.Media;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class UpdateAlbumSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to edit the album")
    public void i_want_to_edit_the_album() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/albums/%s", state.getUser().getId(), state.getAlbum().getId()));
    }
}
