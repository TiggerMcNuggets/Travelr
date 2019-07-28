package javaSteps.steps.auth;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

/**
 * Contains steps for testing logging out
 */
public class LogoutSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to logout")
    public void iWantToLogout() {
        state.getRequest().method("POST");
        state.getRequest().uri("https://localhost:9000/api/logout");
    }
}

