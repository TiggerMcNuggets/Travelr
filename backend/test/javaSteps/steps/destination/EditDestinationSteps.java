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

public class EditDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    // The destination edit data to be tested.
    private JsonNode destinationEditData;

    /**
     * Sets the edit data to a valid destination json
     */
    @Given("I provide valid destination edit values")
    public void i_provide_valid_destination_edit_values() {
        destinationEditData = Json.parse("{ \"name\": \"string\", \"latitude\": 1, \"longitude\": 1, \"type\": \"string\", \"district\": \"string\", \"country\": \"string\"}");
    }

    /**
     * Sets the edit data to a valid destination json
     */
    @Given("I provide invalid destination edit values")
    public void i_provide_invalid_destination_edit_values() {
        destinationEditData = Json.parse("{}");
    }

    /**
     * Sets the destination id to the desired integer value. This value will be used to specify which destination will be edited.
     * @param id The id of the destination.
     */
    @Given("The destination id is {int}")
    public void the_destination_id_is(Integer id) {
        state.setDestinationId(id);
    }

    /**
     * Performs a fake request to edit the destination using the correct api endpoint.
     */
    @When("I edit the destination")
    public void i_edit_the_destination() {
        try {
            // Create request object
            Http.RequestBuilder edit = Helpers.fakeRequest()
                    .method("PUT")
                    .bodyJson(destinationEditData)
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/destinations/" + state.getDestinationId());

            // Send request
            state.setResult(route(state.getApplication(), edit));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Given("Another user has created a destination with id {int}")
    public void another_user_has_created_a_destination_with_id(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }




}
