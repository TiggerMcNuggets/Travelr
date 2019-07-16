package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.User;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import java.util.List;
import java.util.Map;

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



    @Given("I create the destination")
    public void i_create_the_destination(List<Map<String,String>> dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.

        System.out.println(dataTable);
        Map<String,String> destInfo = dataTable.get(0);
        String requestStr = String.format(String.format("{ \"name\": \"%s\", \"latitude\": %s, \"longitude\": %s, \"type\": \"%s\", \"district\": \"%s\", \"country\": \"%s\"}",
                destInfo.get("name"),
                destInfo.get("latitude"),
                destInfo.get("longitude"),
                destInfo.get("type"),
                destInfo.get("district"),
                destInfo.get("country")
                ));
        destinationData = Json.parse(requestStr);
        User authenticatedUser = User.find.byId(2L);
        System.out.println(authenticatedUser.getToken());
        try {
            // Create request object
            Http.RequestBuilder createDestinationForUser = Helpers.fakeRequest()
                    .method("POST")
                    .header("X-Authorization", authenticatedUser.getToken())
                    .bodyJson(destinationData)
                    .uri("http://localhost:9000/api/users/"+ authenticatedUser.getId() +"/destinations");

            // Send request
            state.setResult(route(state.getApplication(), createDestinationForUser));

        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);
        }

    }


}
