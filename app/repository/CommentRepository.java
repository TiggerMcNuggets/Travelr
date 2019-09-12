package repository;

import com.google.inject.Inject;
import controllers.dto.Comment.CreateCommentReq;
import models.Comment;
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
     * @param message The comment message
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
    public CompletableFuture<Void> delete(Comment comment) {
        return supplyAsync(() -> {
            comment.setDeleted(!comment.isDeleted());
            return null;
        });
    }
}
