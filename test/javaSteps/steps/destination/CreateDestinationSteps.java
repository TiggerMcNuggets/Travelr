package javaSteps.steps.destination;

import com.fasterxml.jackson.databind.JsonNode;
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

    @When("I create the destination")
    public void i_create_the_destination(List<Map<String,String>> dataTable) {
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
