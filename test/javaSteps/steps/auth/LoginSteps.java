package javaSteps.steps.auth;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.User;

import java.util.List;
import java.util.Map;

/**
 * Contains steps for testing logging in
 */
public class LoginSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("The user exists")
    public void theUserExists(List<Map<String, String>> dataTable) {
        Map<String, String> userInfo = dataTable.get(0);
        User user = new User(
                userInfo.get("first"),
                userInfo.get("last"),
                userInfo.get("email"),
                Integer.valueOf(userInfo.get("dob"))
        );
        user.insert();
        state.setUser(user);
    }

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

