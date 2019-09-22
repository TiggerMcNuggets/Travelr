package javaSteps.steps.comments;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Comment;
import org.junit.Assert;

public class AddCommentEmoteSteps {

    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to add an emoji")
    public void i_want_to_add_an_emoji() {
        state.getRequest()
                .uri(String.format("http://localhost:9000/api/users/%s/trips/%s/comments/%s/emoji",
                        state.getUser().getId().toString(),
                        state.getTrip().getId().toString(),
                        state.getComment().getId().toString())
                );
        state.getRequest().method("PUT");
    }

    @When("I want to add an emoji to a non existent comment")
    public void i_want_to_add_an_emoji_to_a_non_existent_comment() {
        state.getRequest()
                .uri(String.format("http://localhost:9000/api/users/%s/trips/%s/comments/%s/emoji",
                        state.getUser().getId().toString(),
                        state.getTrip().getId().toString(),
                        "12323")
                );
        state.getRequest().method("PUT");
    }

    @Then("the comment has the emoji react {int} times")
    public void the_comment_has_the_emoji_react_times(long numReacts) {
        Comment comment = Comment.find.byId(state.getComment().getId());
        Assert.assertEquals(numReacts, comment.getCommentEmojis().size());
    }

    @Then("the emoji is related to the user who posted")
    public void the_emoji_is_related_to_the_user_who_posted() {
        Comment comment = Comment.find.byId(state.getComment().getId());
        Assert.assertTrue(comment.getCommentEmojis().get(0).getUsers().contains(state.getUser()));
    }

    @Then("the emote is {string}")
    public void the_emote_is(String emoji) {
        Comment comment = Comment.find.byId(state.getComment().getId());
        Assert.assertEquals(emoji, comment.getCommentEmojis().get(0).getEmoji());
    }

}
