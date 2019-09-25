package dto.trip;

import dto.user.UserSimpleDTO;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentEmojiDTO {


    public String emoji;

    public List<UserSimpleDTO> users;

    public CommentEmojiDTO(String emoji, List<User> usersList) {
        this.emoji = emoji;
        this.users = new ArrayList<>();
        for (User u : usersList) {
            users.add(new UserSimpleDTO(u));
        }
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<UserSimpleDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserSimpleDTO> users) {
        this.users = users;
    }
}
