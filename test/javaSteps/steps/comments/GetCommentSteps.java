package javaSteps.steps.comments;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class GetCommentSteps {

    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to retrieve comments")
    public void i_want_to_retrieve_comments() {
        state.getRequest().uri(String.format("http://localhost:9000/api/users/%s/trips/%s/comments", state.getUser().getId(), state.getTrip().getId()));
        state.getRequest().method("GET");
    }
}
