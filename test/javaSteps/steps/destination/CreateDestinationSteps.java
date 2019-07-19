package javaSteps.steps.destination;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Destination;
import play.libs.Json;

import static play.test.Helpers.contentAsString;

public class CreateDestinationSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to create a destination")
    public void iWantToCreateADestination() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/destinations", state.getUser().getId())));
        state.getRequest().method("POST");
    }

    @When("I check my destination")
    public void iCheckMyDestination() {
        state.setDestination(Destination.find.findById(Json.parse(contentAsString(state.getResult())).get("id").asLong()));
    }
}
