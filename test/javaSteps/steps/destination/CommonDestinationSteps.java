package javaSteps.steps.destination;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import models.Destination;
import models.User;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CommonDestinationSteps {

    private StateSingleton state = StateSingleton.getInstance();

    @Given("I do not own a destination")
    public void i_do_not_own_a_destination() {
        createTestDestination(state.getUser());
        state.getDestination().setId(10L);
        // By not inserting, the destination does not exist
    }

    @Given("I own a destination")
    public void i_own_a_destination() {
        createTestDestination(state.getUser());
        state.getDestination().insert();
    }

    @Given("The global admin owns a destination")
    public void the_global_admin_owns_a_destination() {
        createTestDestination(User.find.findById(1L));
        state.getDestination().insert();
    }

    @Given("The destination is public")
    public void theDestinationIsPublic() {
        Destination destination = Destination.find.findById(state.getDestination().getId());

        destination.setPublic(true);
        destination.save();

        state.setDestination(destination);
    }

    @Then("My destination is")
    public void myDestinationIs(List<Map<String, String>> dataTable) {
        // Refresh destination

        Destination destination = Destination.find.findById(state.getDestination().getId());
        state.setDestination(Destination.find.findById(state.getDestination().getId()));

        Map<String, String> destInfo = dataTable.get(0);
        Assert.assertEquals(destInfo.get("name"), state.getDestination().getName());
        Assert.assertEquals(Double.valueOf(destInfo.get("latitude")), state.getDestination().getLatitude());
        Assert.assertEquals(Double.valueOf(destInfo.get("longitude")), state.getDestination().getLongitude());
        Assert.assertEquals(destInfo.get("type"), state.getDestination().getType());
        Assert.assertEquals(destInfo.get("district"), state.getDestination().getDistrict());
        Assert.assertEquals(destInfo.get("country"), state.getDestination().getCountry());
    }

    private void createTestDestination(User user) {
        state.setDestination(new Destination("Eiffel Tower", 5.0, 5.0, "Landmark", "Paris", "France", user));
    }
}
