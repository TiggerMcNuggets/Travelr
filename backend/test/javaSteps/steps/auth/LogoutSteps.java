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

    /**
     * Executes the fake request to log a user out
     */
    @When("I logout")
    public void iLogout() {
        try {
            // Create request object
            Http.RequestBuilder logout = Helpers.fakeRequest()
                    .method("POST")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/logout");

            // Send request
            state.setResult(route(state.getApplication(), logout));

        } catch (Exception e) {
            System.err.println(e);
            Assert.assertTrue(false);
        }
    }
}

