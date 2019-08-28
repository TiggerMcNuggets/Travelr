package models;

import finders.EmojiFinder;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * Emoji entity managed by Ebean
 */
@Entity
public class CommentEmoji extends BaseModel {

    public static final EmojiFinder find = new EmojiFinder();

    @Constraints.Required
    public String emoji;

    @ManyToMany(cascade= CascadeType.ALL)
    public List<User> users;

    @ManyToOne
    public Comment comment;

    /**
     * Constructor
     * @param emoji The emoji string representation TODO: Find out how we'll represent emojis
     * @param comment The comment
     * @param user The user
     */
    public CommentEmoji(String emoji, Comment comment, User user) {
        this.emoji = emoji;
        this.comment = comment;
        this.users = new ArrayList<>();
        this.users.add(user);
    }

    /**
     * Getters and setters
     */
    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
