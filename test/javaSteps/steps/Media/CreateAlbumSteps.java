package javaSteps.steps.Media;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Destination;
import org.junit.Assert;
import play.libs.Json;

import static play.test.Helpers.contentAsString;

public class CreateAlbumSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to create an album")
    public void iWantToCreateADestination() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/:id/album", state.getUser().getId())));
        state.getRequest().method("POST");
    }

    @When("I check the album")
    public void iCheckTheDestination() {
        state.setAlbum(Album.find.findAlbumById(Json.parse(contentAsString(state.getResult())).get("id").asLong()));
    }

    @Then("The album id is {int1}")
    public void the_album_id_is(Integer int1) {
        Assert.assertEquals(state.getAlbum().id, int1);
    }
}
