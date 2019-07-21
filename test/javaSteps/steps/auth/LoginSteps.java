package javaSteps.steps.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.User;
import org.junit.Assert;
import play.mvc.Http;
import play.test.Helpers;

import java.util.List;
import java.util.Map;

import static play.test.Helpers.route;

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

//    private JsonNode loginData;
//
//    /**
//     * Sets loginData to a JSON object with and email and password field set to the values given
//     * @param email The user's email as a string
//     * @param password The user's password as a string (plaintext)
//     */
//    @Given("I provide the email {string} and the password {string}")
//    public void iProvideTheEmailAndThePassword(String email, String password) {
//        ObjectMapper mapper = new ObjectMapper();
//        loginData = mapper.createObjectNode();
//
//        ((ObjectNode)loginData).put("email", email);
//        ((ObjectNode)loginData).put("password", password);
//    }
//
//    /**
//     * Sets loginData to an empty JSON object
//     */
//    @Given("I provide an incomplete login json")
//    public void iProvideAnIncompleteLoginJson() {
//        ObjectMapper mapper = new ObjectMapper();
//        loginData = mapper.createObjectNode();
//    }
//
//    /**
//     * Executes the fake request to log the user in
//     */
//    @When("I login")
//    public void iLogin() {
//        try {
//            // Create request objeect
//            Http.RequestBuilder login = Helpers.fakeRequest()
//                    .method("POST")
//                    .bodyJson(loginData)
//                    .uri("https://localhost:9000/api/login");
//
//            // Send request
//            state.setResult(route(state.getApplication(), login));
//
//        } catch (Exception e) {
//            System.err.println(e);
//            Assert.assertTrue(false);
//        }
//    }
}

