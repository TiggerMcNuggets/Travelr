package repository;

import com.google.inject.Inject;
import controllers.constants.APIResponses;
import controllers.dto.Comment.CreateCommentReq;
import controllers.dto.Comment.AddEmojiReq;
import exceptions.NotFoundException;
import models.Comment;
import models.CommentEmoji;
import models.TripNode;
import models.User;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CommentRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public CommentRepository(DatabaseExecutionContext executionContext) { this.executionContext = executionContext; }

    /**
     * Inserts a comment on a trip
     * @param request The comment request DTO
     * @param tripNode The trip
     * @param user The user
     * @return The comment id
     */
    public CompletableFuture<Long> insert(CreateCommentReq request, TripNode tripNode, User user) {
        return supplyAsync(() -> {
           Comment comment = new Comment(request.getMessage(), tripNode, user);
           comment.insert();
           return comment.getId();
        });
    }

    /**
     * Updates a comment
     * @param comment The comment
     * @param message The comment message
     * @return null
     */
    public CompletableFuture<Void> update(Comment comment, String message) {
        return supplyAsync(() -> {
           comment.setMessage(message);
           comment.save();
           return null;
        });
    }

    /**
     * Deletes a comment
     * @param comment The comment
     * @return null
     */
    public CompletableFuture<Long> delete(Comment comment) {
        return supplyAsync(() -> {
            Long id = comment.getId();
            comment.setDeleted(!comment.isDeleted());
            comment.update();
            return id;
        });
    }

    /**
     * Gets one comment by id and throws not found exception if not found
     * @param id the comment id
     * @return completable future of comment
     */
    public CompletableFuture<Comment> getCommentHandler(Long id) {
        return supplyAsync(() -> {
            Comment comment = Comment.find.byId(id);
            if (comment == null) throw new NotFoundException(APIResponses.NOT_FOUND);
            return comment;
        }, executionContext);
    }

    /**
     * Adds an emoji for a user on a comment.
     * @param addEmojiReq The emote information
     * @param comment The comment to add the emoji to
     * @param user The user that is reacting
     * @return The id of the commment emoji
     */
    public CompletableFuture<Long> addEmoji(AddEmojiReq addEmojiReq, Comment comment, User user) {

        return supplyAsync(() -> {
            String emoji = addEmojiReq.getEmoji();

            CommentEmoji commentEmoji = CommentEmoji.find.query().where().eq("emoji", emoji).findOne();
            if (commentEmoji == null) {
                commentEmoji = new CommentEmoji(emoji, comment, user);
            } else if (!commentEmoji.getUsers().contains(user)) {
                commentEmoji.addUser(user);
            }

            commentEmoji.save();
            return commentEmoji.getId();
        }, executionContext);
    }
}
