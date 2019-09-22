package javaSteps.steps.comments;

import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;

public class DeleteCommentSteps {

    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to delete the comment")
    public void i_delete_the_comment() {
        state.getRequest()
             .uri(String.format("http://localhost:9000/api/users/%s/trips/%s/comments/%s/toggle_deleted",
                 state.getUser().getId().toString(),
                 state.getTrip().getId().toString(),
                 state.getComment().getId().toString())
             );
        state.getRequest().method("PUT");
    }
}
