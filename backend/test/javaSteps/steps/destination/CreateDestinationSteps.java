package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.route;

public class CreateDestinationSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    private JsonNode destinationData;

    /**
     * Sets destinationData to a JSON object that has all fields validated
     */
    @Given("I provide complete destination information")
    public void iProvideCompleteDestinationInformation() {
        destinationData = Json.parse("{ \"name\": \"string\", \"latitude\": 5, \"longitude\": 5, \"type\": \"string\", \"district\": \"string\", \"country\": \"string\"}");
    }

    @When("I create a destination for user with id {int}")
    public void i_create_a_destination_for_user_with_id(int int1) {
        try {
            // Create request object
            Http.RequestBuilder createDestinationForUser = Helpers.fakeRequest()
                    .method("POST")
                    .header("X-Authorization", state.getToken())
                    .bodyJson(destinationData)
                    .uri("http://localhost:9000/api/users/"+ int1 +"/destinations");

            // Send request
            state.setResult(route(state.getApplication(), createDestinationForUser));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Given("I provide incomplete destination information")
    public void iProvideIncompleteDestinationInformation() {
        destinationData = Json.parse("{}");
    }


}
