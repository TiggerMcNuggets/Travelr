package javaSteps.steps.comments;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Comment;
import org.junit.Assert;
import play.libs.Json;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static play.test.Helpers.contentAsString;

public class CommonCommentSteps {
    private StateSingleton state = StateSingleton.getInstance();

    @When("I check the comment")
    public void i_check_the_comment() {
        Optional<Comment> comment = Comment.find.findById(Json.parse(contentAsString(state.getResult())).get("id").asLong());
        state.setComment(comment.get());
    }

    @Then("The comment is")
    public void theCommentIs(List<Map<String, String>> dataTable) {
        Assert.assertEquals(state.getComment(), dataTable.get(0).get("message"));

    }
}
