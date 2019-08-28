package models;

import finders.CommentFinder;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment entity managed by Ebean
 */
@Entity
public class Comment extends BaseModel {

    public static final CommentFinder find = new CommentFinder();

    @Constraints.Required
    public String message;

    @ManyToOne
    public TripNode tripNode;

    @ManyToOne
    public User user;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<CommentEmoji> commentEmojis;

    /**
     * Constructor
     * @param message The comment message
     * @param user The user
     */
    public Comment(String message, TripNode tripNode, User user) {
        this.message = message;
        this.tripNode = tripNode;
        this.user = user;
        commentEmojis = new ArrayList<>();
    }

    /**
     * Getters and setters
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TripNode getTripNode() {
        return tripNode;
    }

    public void setTripNode(TripNode tripNode) {
        this.tripNode = tripNode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CommentEmoji> getCommentEmojis() {
        return commentEmojis;
    }

    public void setCommentEmojis(List<CommentEmoji> commentEmojis) {
        this.commentEmojis = commentEmojis;
    }

    public void addCommentEmoji(CommentEmoji commentEmoji) {
        this.commentEmojis.add(commentEmoji);
    }

    public void removeCommentEmoji(CommentEmoji commentEmoji) {
        this.commentEmojis.remove(commentEmoji);
    }
}
