package javaSteps.steps.Media;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Album;
import org.junit.Assert;
import play.libs.Json;

import static play.test.Helpers.contentAsString;

public class CreateAlbumSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();


    @When("I want to create an album")
    public void i_want_to_create_an_album() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/albums", state.getUser().getId())));
        state.getRequest().method("POST");
    }

    @When("I check the album")
    public void i_check_the_album() {
        state.setAlbum(Album.find.findAlbumById(Json.parse(contentAsString(state.getResult())).get("id").asLong()));

    }

    @Then("The album id is {long}")
    public void the_album_id_is(Long int1) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(state.getAlbum().id, int1);
    }

}
