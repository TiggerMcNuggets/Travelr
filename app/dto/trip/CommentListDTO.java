package dto.trip;

import models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentListDTO {

    public int commentsLength;

    public List<CommentDTO> comments;

    public CommentListDTO(List<Comment> commentList, int count) {
        this.commentsLength = count;

        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment: commentList) {
            CommentDTO commentDTO = new CommentDTO(comment);
            commentDTOList.add(commentDTO);
        }
        this.comments = commentDTOList;
    }

    public int getCommentsLength() {
        return commentsLength;
    }

    public void setCommentsLength(int commentsLength) {
        this.commentsLength = commentsLength;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }


}
