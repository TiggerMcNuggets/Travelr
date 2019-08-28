package finders;

import io.ebean.Finder;
import io.ebean.Query;
import models.Comment;
import models.CommentEmoji;

import java.util.List;
import java.util.Optional;

/**
 * Ebean query finder for emojis
 */
public class EmojiFinder extends Finder<Long, CommentEmoji> {

    /**
     * Constructor
     */
    public EmojiFinder() { super(CommentEmoji.class); }

    /**
     * Finds an optional emoji by its id
     * @param id The emojis id
     * @return The optional emoji
     */
    public Optional<CommentEmoji> findById(Long id) {
        return emojiFetcher().where().eq("id", id).findOneOrEmpty();
    }

    /**
     * Finds all emojis that belong to a comment
     * @param comment The comment
     * @return The list of emojis
     */
    public List<CommentEmoji> findByComment(Comment comment) {
        return emojiFetcher().where().eq("comment", comment).findList();
    }

    /**
     * Fetches users related to an emoji
     * @return The query
     */
    private Query<CommentEmoji> emojiFetcher() {
        return query().fetch("users");
    }
}
