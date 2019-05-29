package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import finders.DestinationFinder;
import javaSteps.models.StateSingleton;
import models.Destination;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

/**
 * Contains steps for testing getting a traveller
 */
public class MakeDestinationPublicSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();
    private DestinationFinder destinationFinder = new DestinationFinder();
    private int destinationId;
    private Integer firstCreatedId = null;

    @Given("The private destination id is {int}")
    public void thePrivateDestinationIdIs(Integer id) {
        destinationId = id;
    }

    @When("I make the destination public")
    public void iMakeTheDestinationPublic() {
        try {
            // Create request object
            Http.RequestBuilder info = Helpers.fakeRequest()
                    .method("PUT")
                    .header("X-Authorization", state.getToken())
                    .uri("https://localhost:9000/api/destinations/" + destinationId + "/make_public");

            // Send request
            state.setResult(route(state.getApplication(), info));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }
    }

    @Then("the owner of the destination with id {int} is {int}")
    public void the_owner_of_the_destination_with_id_is(Integer dest_id, Integer owner_id) {
        Assert.assertEquals(destinationFinder.findById(Long.valueOf(dest_id)).getUser().getId(), Long.valueOf(owner_id));

    }

    @Given("I create a destination for user id {int}")
    public void iCreateADestinationWithId(int userId) {
        JsonNode destinationData = Json.parse("{ \"name\": \"string\", \"latitude\": 5, \"longitude\": 5, \"type\": \"string\", \"district\": \"string\", \"country\": \"string\"}");
        try {
            // Create request object
            Http.RequestBuilder createDestinationForUser = Helpers.fakeRequest()
                    .method("POST")
                    .header("X-Authorization", state.getToken())
                    .bodyJson(destinationData)
                    .uri("http://localhost:9000/api/users/"+ userId +"/destinations");

            // Send request
            state.setResult(route(state.getApplication(), createDestinationForUser));

            JsonNode responseBody = Json.parse(contentAsString(state.getResult()));
            destinationId = responseBody.get("id").asInt();

            if (firstCreatedId == null) {
                firstCreatedId = destinationId;
            }

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }

    }

    @Then("My first destination should be deleted")
    public void myFirstDestinationShouldBeDeleted() {
        Destination destination = Destination.find.findById(firstCreatedId.longValue());
        Assert.assertNull(destination);
    }
}
