package javaSteps.steps.comments;

import cucumber.api.java.en.Given;
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

    @Given("I add a comment {string} to the trip")
    public void i_add_a_comment_to_the_trip(String message) {
        Comment comment = new Comment(message, state.getTrip(), state.getUser());
        comment.insert();
        state.setComment(comment);
    }

    @When("I check the comment")
    public void i_check_the_comment() {
        Optional<Comment> comment = Comment.find.findById(Json.parse(contentAsString(state.getResult())).get("id").asLong());
        if (!comment.isPresent()) {
            Assert.fail("The comment does not exist");
        }
        state.setComment(comment.get());
    }

    @Then("The comment is")
    public void theCommentIs(List<Map<String, String>> dataTable) {
        Assert.assertEquals(state.getComment().getMessage(), dataTable.get(0).get("message"));

    }

    @Then("the comment has been removed")
    public void the_comment_has_been_removed() {
        Optional<Comment> comment = Comment.find.findById(state.getComment().getId());
        Assert.assertFalse(comment.isPresent());
    }

    @Then("the comment exists")
    public void the_comment_exists() {
        Optional<Comment> comment = Comment.find.findById(state.getComment().getId());
        Assert.assertTrue(comment.isPresent());
    }
}
