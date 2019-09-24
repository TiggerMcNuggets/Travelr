package dto.trip;

import models.Comment;
import models.CommentEmoji;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentDTO {
    public Long id;

    public Long userId;

    public String userFirstName;

    public String userLastName;

    public String comment;

    public String profilePhoto;

    public Long timestamp;

    public List<CommentEmojiDTO> emojis;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public CommentDTO(Comment comment) {
        this.setId(comment.getId());
        this.setUserId(comment.getUser().getId());
        this.setUserFirstName(comment.getUser().getFirstName());
        this.setUserLastName(comment.getUser().getLastName());
        this.setProfilePhoto(comment.getUser().getUserProfilePhoto());
        this.setComment(comment.getMessage());
        this.setTimestamp(comment.getTimestamp());
        this.emojis = new ArrayList<CommentEmojiDTO>();
        for (CommentEmoji emoji : comment.commentEmojis) {
            emojis.add(new CommentEmojiDTO(emoji.emoji, emoji.users));
        }
    }

}
