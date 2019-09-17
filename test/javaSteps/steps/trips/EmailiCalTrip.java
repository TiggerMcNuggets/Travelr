package javaSteps.steps.trips;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class EmailiCalTrip {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to send everyone an iCal email")
    public void iWantToSendEveryoneAnICalEmail() {
        state.getRequest().uri(String.format("http://localhost:9000/api/users/%s/trips/%s/iCal/email", state.getUser().getId(), state.getTrip().getId()));
        state.getRequest().method("POST");
    }
}
