package javaSteps.steps.Media;

import cucumber.api.java.en.Given;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Destination;
import models.Media;
import models.User;

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
        state.setMedia(new Media(user, mediaInfo.get("uriString")));
        state.getAlbum().addMedia(state.getMedia());
        state.getMedia().addAlbum(state.getAlbum());
        state.getMedia().insert();
    }


}
