package vo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by huihantao on 2017/5/15.
 */
public class CommentVO implements Serializable{

    // Stirng comment  means the content of the comment
    //commenttime means the time when comments
    // commenterName means the name who commens
    private static final long serialVersionUID = 3095942003508433966L;
    private String comment;
    private LocalDateTime commentTime;
    private String commenterName;

    public CommentVO(String comment, LocalDateTime commentTime, String commenterName) {
        this.comment = comment;
        this.commentTime = commentTime;
        this.commenterName = commenterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalDateTime commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }
}
