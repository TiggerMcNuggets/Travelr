package javaSteps.steps.comments;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class CreateCommentSteps {
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to add a comment")
    public void iWantToAddAComment() {
        state.getRequest().uri(String.format("http://localhost:9000/api/users/%s/trips/%s/comments", state.getUser().getId(), state.getTrip().getId()));
        state.getRequest().method("POST");
    }
}