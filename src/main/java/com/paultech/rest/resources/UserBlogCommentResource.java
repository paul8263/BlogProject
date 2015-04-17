package com.paultech.rest.resources;

import com.paultech.core.entities.UserBlogComment;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Created by paulzhang on 7/04/15.
 */
public class UserBlogCommentResource extends ResourceSupport {
    private String comment;
    private Date commentDate;
    private String commenterUsername;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommenterUsername() {
        return commenterUsername;
    }

    public void setCommenterUsername(String commenterUsername) {
        this.commenterUsername = commenterUsername;
    }

    public UserBlogComment toUserBlogComment() {
        UserBlogComment userBlogComment = new UserBlogComment();
        userBlogComment.setComment(this.comment);
        userBlogComment.setCommentDate(this.commentDate);

        return userBlogComment;
    }
}
