package controllers.dto.Comment;

import play.data.validation.Constraints;

public class CreateCommentReq {
    @Constraints.Required
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}