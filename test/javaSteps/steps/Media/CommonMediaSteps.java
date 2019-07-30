package javaSteps.steps.Media;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Media;
import models.User;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class CommonMediaSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("An album exists")
    public void an_album_exists() {
        Album album = new Album(state.getUser(), "Test Album", false);
        album.insert();
        state.setAlbum(album);
    }

    @Given("I own the album")
    public void i_own_the_album(List<Map<String, String>> dataTable) {
        Map<String, String> albumInfo = dataTable.get(0);
        createTestAlbum(albumInfo, state.getUser());
    }

    @Given("They own the album")
    public void they_own_the_album(List<Map<String, String>> dataTable) {
        Map<String, String> destInfo = dataTable.get(0);
        createTestAlbum(destInfo, state.getUser());
    }

    @Given("the album contains the media")
    public void the_album_contains_the_media(List<Map<String, String>> dataTable) {
        Map<String, String> mediaInfo = dataTable.get(0);
        createMediaInAlbum(mediaInfo, state.getUser());
    }

    private void createTestAlbum(Map<String, String> albumInfo, User user) {
        state.setAlbum(new Album(user, albumInfo.get("name"), false));
        state.getAlbum().insert();
    }

    private void createMediaInAlbum(Map<String, String> mediaInfo, User user) {
        if (state.getMedia() == null || state.getMedia().getUriString() != mediaInfo.get("uriString")) {
            state.setMedia(new Media(user, mediaInfo.get("uriString")));
            state.getMedia().insert();
        }
        state.getAlbum().addMedia(state.getMedia());
        state.getMedia().addAlbum(state.getAlbum());
        state.getMedia().update();
        state.getAlbum().update();
    }

    @Then("The album is")
    public void the_album_is(List<Map<String, String>> dataTable) {
        Album album = Album.find.findAlbumById(state.getAlbum().getId());

        Map<String, String> albumInfo = dataTable.get(0);

        Assert.assertEquals(albumInfo.get("name"), album.getName());
    }

    @Given("I do not own an album")
    public void i_do_not_own_an_album() {
        Album album = new Album(state.getUser(), "Album", true);
        album.setId(100L);
        state.setAlbum(album);
        // By not inserting, the album does not exist
        // Album needed to be created to pass id into request uri
    }
}
