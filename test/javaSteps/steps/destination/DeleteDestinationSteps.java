package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Destination;
import models.User;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import java.util.List;
import java.util.Map;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

public class DeleteDestinationSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();
    private Long destinationId = 0L;

//    @Given("I own a destination")
//    public void i_own_a_destination() {
//        // Write code here that turns the phrase above into concrete actions
//        Assert.assertTrue(true);
//    }
//
//    @When("I want to soft delete the destination")
//    public void i_want_to_soft_delete_the_destination() {
//        // Write code here that turns the phrase above into concrete actions
//        Assert.assertTrue(true);
//    }
//
//    @Then("The destination does not exist")
//    public void the_destination_does_not_exist() {
//        // Write code here that turns the phrase above into concrete actions
//        Assert.assertTrue(true);
//    }
//
//    @Given("The global admin owns a destination")
//    public void the_global_admin_owns_a_destination() {
//        // Write code here that turns the phrase above into concrete actions
//        Assert.assertTrue(true);
//    }
//
//    @Then("The destination does exist")
//    public void the_destination_does_exist() {
//        // Write code here that turns the phrase above into concrete actions
//        Assert.assertTrue(true);
//    }
//
//    @Given("I do not own a destination")
//    public void i_do_not_own_a_destination() {
//        // Write code here that turns the phrase above into concrete actions
//        Assert.assertTrue(true);
//    }

//    @When("I soft delete the destination with id {int}")
//    public void i_soft_delete_the_destination_with_id(Integer destId) {
//        try {
//            // Create request object
//            Http.RequestBuilder delete = Helpers.fakeRequest()
//                    .method("PUT")
//                    .header("X-Authorization", state.getToken())
//                    .uri("https://localhost:9000/api/users/"+state.getTravellerId()+"/destinations/" + destId + "/toggle_deleted");
//
//            // Send request
//            state.setResult(route(state.getApplication(), delete));
//
//        } catch (Exception e) {
//            System.out.println(e);
//            Assert.assertTrue(false);
//        }
//    }
//
//    @Then("in the list of destinations the destination with id {int} will not be present.")
//    public void in_the_list_of_destinations_the_destination_with_id_will_not_be_present(Integer destId) {
//        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
//        boolean isFound = false;
//        for (JsonNode destination : responseBody) {
//            if (destination.get("id").asInt() == destId) {
//                isFound = true;
//            }
//        }
//        Assert.assertFalse(isFound);
//    }
//
//    @When("I undo the deletion status on destination with id {int}")
//    public void iUndoTheDeletionStatusOnDestinationWithId(Integer dest_id) {
//        i_soft_delete_the_destination_with_id(dest_id);
//    }
//
//    @Then("in the list of destinations the destination with id {int} will be present.")
//    public void inTheListOfDestinationsTheDestinationWithIdWillBePresent(Integer destId) {
//        JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
//        boolean isFound = false;
//        for (JsonNode destination : responseBody) {
//            if (destination.get("id").asInt() == destId) {
//                isFound = true;
//            }
//        }
//        Assert.assertTrue(isFound);
//    }

//    /****
//     * New
//     * ****/
//
    @When("I want to soft delete the destination")
    public void i_want_to_soft_delete_the_destination() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/destinations/%s/toggle_deleted", state.getUser().getId(), destinationId));
    }

    @Then("The destination does not exist")
    public void the_destination_does_not_exist() {
        Assert.assertNull(Destination.find.findById(destinationId));
    }

    @Then("The destination does exist")
    public void the_destination_does_exist() {
        Assert.assertNotNull(Destination.find.findById(destinationId));
    }

    @Given("I do not own a destination")
    public void i_do_not_own_a_destination() {
        destinationId = 0L;
    }

    @Given("I own a destination")
    public void i_own_a_destination() {
        Destination testDestination = createTestDestination(state.getUser());
        destinationId = testDestination.getId();
    }

    @Given("The global admin owns a destination")
    public void the_global_admin_owns_a_destination() {
        Destination testDestination = createTestDestination(User.find.findById(1L));
        destinationId = testDestination.getId();
    }

    private Destination createTestDestination(User user) {
        Destination destination = new Destination("Eiffel Tower", 5.0, 5.0, "Landmark", "Paris", "France", user );
        destination.insert();
        return destination;
    }
}

