package repository;

import com.google.inject.Inject;
import models.Comment;
import models.CommentEmoji;
import models.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class EmojiRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public EmojiRepository(DatabaseExecutionContext executionContext) { this.executionContext = executionContext; }

    /**
     * Toggles the emoji
     * @param comment The comment
     * @param emoji The emoji string
     * @return null
     */
    public CompletableFuture<Void> toggle(Comment comment, String emoji, User user) {
        return supplyAsync(() -> {
            Optional<CommentEmoji> optionalEmoji = CommentEmoji.find.findByCommentAndEmoji(comment, emoji);

            if(optionalEmoji.isPresent()) {
                CommentEmoji commentEmoji = optionalEmoji.get();

                // Toggle user using emoji
                if (commentEmoji.getUsers().contains(user)) {
                    commentEmoji.getUsers().remove(user);
                } else {
                    commentEmoji.getUsers().add(user);
                }
                commentEmoji.update();

            } else {
                CommentEmoji commentEmoji = new CommentEmoji(emoji, comment, user);
                commentEmoji.insert();
            }
            return null;
        });
    }
}
