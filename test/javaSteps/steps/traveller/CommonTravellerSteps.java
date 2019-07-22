package javaSteps.steps.traveller;

import cucumber.api.java.en.Given;
import javaSteps.models.StateSingleton;
import models.User;

public class CommonTravellerSteps {
    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("All previous users are removed")
    public void allPreviousUsersAreRemoved() {
        User.find.query().delete();
    }
}
