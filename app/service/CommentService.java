package service;

import exceptions.ForbiddenException;
import exceptions.NotFoundException;
import models.Comment;
import models.User;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommentService {


    private DatabaseExecutionContext context;

    @Inject
    public CommentService(DatabaseExecutionContext context) {
        this.context = context;
    }

    /**
     *
     * Checks if the given user owns the comment
     * @param commentId the comment id
     * @param user the user
     * @return CompletableFuture of the comment
     */
    public CompletableFuture<Comment> isUserComment(Long commentId, User user) {
        return CompletableFuture.supplyAsync(() -> {

            Optional<Comment> optionalComment = Comment.find.findByIdIncludingSoftDelete(commentId);
            if (!optionalComment.isPresent()) {
                throw new NotFoundException("Comment not found");
            }

            if (!optionalComment.get().getUser().getId().equals(user.getId())) {
                throw new ForbiddenException("User does not own the comment");
            }

            return optionalComment.get();
        }, context);
    }

    /**
     * Gets user comment assuming user is permitted
     * @param commentId the comment id
     * @return CompletableFuture of the comment
     */
    public CompletableFuture<Comment> getUserComment(Long commentId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Comment> optionalComment = Comment.find.findByIdIncludingSoftDelete(commentId);
            if (!optionalComment.isPresent()) {
                throw new NotFoundException("Comment not found");
            }

            return optionalComment.get();
        }, context);
    }
}
