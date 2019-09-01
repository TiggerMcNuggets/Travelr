package finders;

import io.ebean.Finder;
import io.ebean.Query;
import models.Comment;
import models.TripNode;

import java.util.List;
import java.util.Optional;

/**
 * Ebean query finder for comments
 */
public class CommentFinder extends Finder<Long, Comment> {

    /**
     * Constructor
     */
    public CommentFinder() { super(Comment.class); }

    /**
     * Finds an optional comment by its id
     * @param id The comments id
     * @return The optional comment
     */
    public Optional<Comment> findById(Long id) {
        return commentFetcher().where().eq("id", id).findOneOrEmpty();
    }

    /**
     * Finds all comments that belong to a trip
     * @param tripNode The trip
     * @return The list of comments
     */
    public List<Comment> findByTrip(TripNode tripNode) {
        return commentFetcher().where().eq("tripNode", tripNode).findList();
    }

    /**
     * Fetches the emojis and emoji users related to a comment
     * @return The query
     */
    private Query<Comment> commentFetcher() {
        return query().fetch("commentEmojis").fetch("commentEmojis.users");
    }
}
