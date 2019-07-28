package javaSteps.steps.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Destination;
import models.TravellerType;
import models.User;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

/**
 * Contains common steps shared across many features
 */
public class CommonSteps  {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("I am authenticated")
    public void iAmAuthenticated() {
        User testUser = createTestUser(); // User will have an id of 2L
        testUser.setToken();
        state.getRequest().header("X-Authorization", testUser.getToken());
    }

    @Given("I am not authenticated")
    public void iAmNotAuthenticated() {
        createTestUser(); // User will have an id of 2l
        state.getRequest().header("X-Authorization", "");
    }

    @When("The body is")
    public void theBodyIs(String reqBody) {
        state.getRequest().bodyJson(Json.parse(reqBody));
    }

    @When("I send the request")
    public void iSendTheRequest() {
        state.setResult(route(state.getApplication(), state.getRequest()));
    }

    @Then("I will receive the response code {int}")
    public void iWillReceiveTheResponseCode(int responseCode) {
        Assert.assertEquals(responseCode, state.getResult().status());
    }

    @Then("I will receive the response body")
    public void iWillReceiveTheResponseBody(String resBody) {

        // Remove public field as it only shows up in CI and is a duplicate of isPublic
        JsonNode res = Json.parse(contentAsString(state.getResult()));
        if (res.isArray()) {
            for (int i = 0; i < res.size(); i++) {
                ((ObjectNode)(res.get(i))).remove("public");
            }

        } else {
            ((ObjectNode)res).remove("public");
        }

        Assert.assertEquals(Json.parse(resBody), res);
    }

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

    @Given("The user does not exist")
    public void theUserDoesNotExist() {
        state.setUser(new User("first", "last", "e@mail.com", 1));
        state.getUser().setId(10L);
        // By not inserting, the user does not exist
        // User needed to be created to pass id into request uri through state
    }

    @Given("I am an admin")
    public void iAmAnAdmin() {
        state.getUser().setAccountType(1);
        state.getUser().update();
    }

    private User createTestUser() {
        User user = new User("Test", "User", "test.user@testuser.com", 1);
        user.insert();

        List<TravellerType> userTravellerTypes = new ArrayList<>();
        userTravellerTypes.add(TravellerType.find.byId(1L));// finds first traveller type
        user.setTravellerTypes(userTravellerTypes);

        user.setPassword("test");
        user.save();

        state.setUser(user);

        return user;

    }
}
