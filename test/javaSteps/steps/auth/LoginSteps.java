package javaSteps.steps.auth;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.User;

/**
 * Contains steps for testing logging in
 */
public class LoginSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("The password is {string}")
    public void thePasswordIs(String string) {
        User user = User.find.findById(state.getUser().getId());
        user.setPassword(string);
        user.save();
        state.setUser(user);
    }

    @When("I want to login")
    public void iWantToLogin() {
        state.getRequest().method("POST");
        state.getRequest().uri("https://localhost:9000/api/login");
    }
}

