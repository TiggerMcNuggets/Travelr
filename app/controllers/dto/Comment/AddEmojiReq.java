package controllers.dto.Comment;

import play.data.validation.Constraints;

public class AddEmojiReq {
    @Constraints.Required
    public String emoji;

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}