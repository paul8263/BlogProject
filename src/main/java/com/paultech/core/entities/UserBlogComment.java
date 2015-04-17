package com.paultech.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by paulzhang on 18/03/15.
 */
@Entity
@Table(name = "user_blog_comment", schema = "", catalog = "myblog")
@AssociationOverrides({
        @AssociationOverride(name="userBLogCommentPK.userEntity",joinColumns = @JoinColumn(name="user_id")),
        @AssociationOverride(name="userBlogCommentPK.blogEntity",joinColumns = @JoinColumn(name="blog_id"))
})
public class UserBlogComment implements Serializable {
    private UserBlogCommentPK userBlogCommentPK = new UserBlogCommentPK();
    private String comment;
    private Date commentDate;

    @EmbeddedId
    public UserBlogCommentPK getUserBlogCommentPK() {
        return userBlogCommentPK;
    }

    public void setUserBlogCommentPK(UserBlogCommentPK userBlogCommentPK) {
        this.userBlogCommentPK = userBlogCommentPK;
    }

    @Transient
    public UserEntity getUserEntity() {
        return userBlogCommentPK.getUserEntity();
    }

    public void setUserEntity(UserEntity userEntity) {
        userBlogCommentPK.setUserEntity(userEntity);
    }

    @Transient
    public BlogEntity getBlogEntity() {
        return userBlogCommentPK.getBlogEntity();
    }

    public void setBlogEntity(BlogEntity blogEntity) {
        userBlogCommentPK.setBlogEntity(blogEntity);
    }

    @Lob
    @Column(length = 2000)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "comment_date")
    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBlogComment that = (UserBlogComment) o;

        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (commentDate != null ? !commentDate.equals(that.commentDate) : that.commentDate != null) return false;
        if (userBlogCommentPK != null ? !userBlogCommentPK.equals(that.userBlogCommentPK) : that.userBlogCommentPK != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userBlogCommentPK != null ? userBlogCommentPK.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (commentDate != null ? commentDate.hashCode() : 0);
        return result;
    }
}
