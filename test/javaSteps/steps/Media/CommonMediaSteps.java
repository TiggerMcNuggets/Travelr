package javaSteps.steps.Media;

import cucumber.api.java.en.Given;
import javaSteps.models.StateSingleton;
import models.Album;

public class CommonMediaSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("An album exists")
    public void an_album_exists() {
        Album album = new Album();
        album.insert();
        state.setAlbum(album);
    }
}
