package javaSteps.steps.Media;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Destination;
import org.junit.Assert;

public class DeleteAlbumSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Then("The album does exist")
    public void the_album_does_exist() {
        Assert.assertEquals(Album.find.findAlbumById(state.getAlbum().getId()), state.getAlbum());
    }
    
    @When("I want to delete the album")
    public void i_want_to_delete_the_album() {
        state.getRequest().method("DELETE");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/albums/%s", state.getUser().getId(), state.getAlbum().getId()));
    }

    @Then("The album does not exist")
    public void the_album_does_not_exist() {
        Album album = Album.find.findAlbumById(state.getAlbum().getId());
        Assert.assertNull(album);
    }

}
