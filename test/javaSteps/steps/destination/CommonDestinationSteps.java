package javaSteps.steps.destination;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import models.Album;
import models.Destination;
import models.User;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class CommonDestinationSteps {

    private StateSingleton state = StateSingleton.getInstance();

    @Given("I do not own a destination")
    public void i_do_not_own_a_destination() {
        state.setDestination(new Destination("test", 1.0, 1.0, "test", "test", "test", state.getUser()));
        state.getDestination().setId(10L);
        // By not inserting, the destination does not exist
        // Destination needed to be created to pass id into request uri
    }


    @Given("I own the destination")
    public void iOwnTheDestination(List<Map<String, String>> dataTable) {
        Map<String, String> destInfo = dataTable.get(0);
        createTestDestination(destInfo, state.getUser());
    }

    @Given("They own the destination")
    public void theyOwnTheDestination(List<Map<String, String>> dataTable) {
        Map<String, String> destInfo = dataTable.get(0);
        createTestDestination(destInfo, state.getUser());
    }

    @Given("The destination is public")
    public void theDestinationIsPublic() {
        Destination destination = Destination.find.findById(state.getDestination().getId());

        destination.setPublic(true);
        destination.save();

        state.setDestination(destination);
    }

    @Then("The destination is")
    public void theDestinationIs(List<Map<String, String>> dataTable) {
        // Refresh destination

        state.setDestination(Destination.find.findById(state.getDestination().getId()));

        Map<String, String> destInfo = dataTable.get(0);
        Assert.assertEquals(destInfo.get("name"), state.getDestination().getName());
        Assert.assertEquals(Double.valueOf(destInfo.get("latitude")), state.getDestination().getLatitude());
        Assert.assertEquals(Double.valueOf(destInfo.get("longitude")), state.getDestination().getLongitude());
        Assert.assertEquals(destInfo.get("type"), state.getDestination().getType());
        Assert.assertEquals(destInfo.get("district"), state.getDestination().getDistrict());
        Assert.assertEquals(destInfo.get("country"), state.getDestination().getCountry());
    }

    private void createTestDestination(Map<String, String> destInfo, User user) {
        state.setDestination(new Destination(
                destInfo.get("name"),
                Double.valueOf(destInfo.get("latitude")),
                Double.valueOf(destInfo.get("longitude")),
                destInfo.get("type"),
                destInfo.get("district"),
                destInfo.get("country"),
                user)
        );

        Album album = new Album(state.getUser(), "name", true);
        album.insert();
        state.getDestination().setDefaultAlbum(Album.find.findAlbumById(album.getId()));
        state.getDestination().insert();
    }
}
