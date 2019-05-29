package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

public class DeleteDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I soft delete the destination with id {int}")
    public void i_soft_delete_the_destination_with_id(Integer destId) {
        try {
            // Create request object
            Http.RequestBuilder delete = Helpers.fakeRequest()
                    .method("PUT")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/users/"+state.getTravellerId()+"/destinations/" + destId + "/toggle_deleted");

            // Send request
            state.setResult(route(state.getApplication(), delete));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Then("in the list of destinations the destination with id {int} will not be present.")
    public void in_the_list_of_destinations_the_destination_with_id_will_not_be_present(Integer destId) {
        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
        boolean isFound = false;
        for (JsonNode destination : responseBody) {
            if (destination.get("id").asInt() == destId) {
                isFound = true;
            }
        }
        Assert.assertFalse(isFound);
    }

    @When("I undo the deletion status on destination with id {int}")
    public void iUndoTheDeletionStatusOnDestinationWithId(Integer dest_id) {
        i_soft_delete_the_destination_with_id(dest_id);
    }

    @Then("in the list of destinations the destination with id {int} will be present.")
    public void inTheListOfDestinationsTheDestinationWithIdWillBePresent(Integer destId) {
        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
        boolean isFound = false;
        for (JsonNode destination : responseBody) {
            if (destination.get("id").asInt() == destId) {
                isFound = true;
            }
        }
        Assert.assertTrue(isFound);
    }
}
