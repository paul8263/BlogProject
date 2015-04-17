package com.paultech.rest.resources;

import com.paultech.core.entities.BlogEntity;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Created by paulzhang on 6/04/15.
 */
public class BlogEntityResource extends ResourceSupport {
    private Long blogId;
    private String title;
    private String content;
    private Date createDate;
    private Date modifyDate;
    private String ownerUsername;
    private Long ownerId;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BlogEntity toBlogEntity() {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setBlogId(this.blogId);
        blogEntity.setTitle(this.title);
        blogEntity.setContent(this.content);
        blogEntity.setCreateDate(this.createDate);
        blogEntity.setModifyDate(this.modifyDate);
        return blogEntity;
    }
}
