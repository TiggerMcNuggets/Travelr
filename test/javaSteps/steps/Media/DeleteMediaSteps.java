package javaSteps.steps.Media;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Media;
import org.junit.Assert;

public class DeleteMediaSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to delete the media in the album")
    public void i_want_to_delete_the_media_in_the_album() {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/albums/%s/media/%s?removeAll=0", state.getUser().getId(), state.getAlbum().getId(), state.getMedia().getId()));
    }

    @When("I want to delete the media in all albums")
    public void iWantToDeleteTheMediaInAllAlbums() {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/albums/%s/media/%s?removeAll=1", state.getUser().getId(), state.getAlbum().getId(), state.getMedia().getId()));
    }

    @Then("The media does not exist in the album")
    public void the_media_does_not_exist_in_the_album() {
        Album album = Album.find.findAlbumById(state.getAlbum().getId());
        Media deletedMedia = Media.find.findMediaById(state.getMedia().getId());

        for (Media mediaItem : album.getMedia()) {
            if (mediaItem == deletedMedia) {
                Assert.fail();
            }
        }
    }

    @Then("The media does not exist")
    public void theMediaDoesNotExistInAllAlbums() {
        Media deletedMedia = Media.find.findMediaById(state.getMedia().getId());
        Assert.assertNull(deletedMedia);
    }
}
