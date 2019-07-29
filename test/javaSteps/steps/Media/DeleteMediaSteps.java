package javaSteps.steps.Media;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Media;

import static junit.framework.TestCase.assertTrue;

public class DeleteMediaSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to delete the media in the album")
    public void i_want_to_delete_the_media_in_the_album() {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/albums/%s/media/%s", state.getUser().getId(), state.getAlbum().getId(), state.getMedia().getId()));
    }

    @Then("The media does not exist in the album")
    public void the_media_does_not_exist_in_the_album() {
        Album album = state.getAlbum();
        Media deletedMedia = state.getMedia();

        System.out.println(state.getAlbum().getMedia());

        for (Media mediaItem : album.getMedia()) {
            if (mediaItem == deletedMedia) {
                assertTrue(false);
            }
        }
        assertTrue(true);
    }


}
