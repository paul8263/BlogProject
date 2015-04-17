package com.paultech.core.entities;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by paulzhang on 18/03/15.
 */
@Embeddable
public class UserBlogCommentPK implements Serializable {

    private UserEntity userEntity;
    private BlogEntity blogEntity;


    @ManyToOne
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @ManyToOne
    public BlogEntity getBlogEntity() {
        return blogEntity;
    }

    public void setBlogEntity(BlogEntity blogEntity) {
        this.blogEntity = blogEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBlogCommentPK that = (UserBlogCommentPK) o;

        if (blogEntity != null ? !blogEntity.equals(that.blogEntity) : that.blogEntity != null) return false;
        if (userEntity != null ? !userEntity.equals(that.userEntity) : that.userEntity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userEntity != null ? userEntity.hashCode() : 0;
        result = 31 * result + (blogEntity != null ? blogEntity.hashCode() : 0);
        return result;
    }
}
