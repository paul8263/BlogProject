package com.paultech.rest.resources.asm;

import com.paultech.core.entities.BlogEntity;
import com.paultech.rest.controllers.BlogController;
import com.paultech.rest.resources.BlogEntityResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulzhang on 6/04/15.
 */
@Component
public class BlogEntityResourceAsm extends ResourceAssemblerSupport<BlogEntity,BlogEntityResource> {
    public BlogEntityResourceAsm() {
        super(BlogController.class, BlogEntityResource.class);
    }

    @Override
    public List<BlogEntityResource> toResources(Iterable<? extends BlogEntity> entities) {
        List<BlogEntityResource> blogEntityResources = new ArrayList<>();
        for(BlogEntity blogEntity : entities) {
            BlogEntityResource blogEntityResource = this.toResource(blogEntity);
            blogEntityResources.add(blogEntityResource);
        }
        return blogEntityResources;
    }

    @Override
    public BlogEntityResource toResource(BlogEntity blogEntity) {
        BlogEntityResource blogEntityResource = new BlogEntityResource();
        blogEntityResource.setBlogId(blogEntity.getBlogId());
        blogEntityResource.setTitle(blogEntity.getTitle());
        blogEntityResource.setContent(blogEntity.getContent());
        blogEntityResource.setCreateDate(blogEntity.getCreateDate());
        blogEntityResource.setModifyDate(blogEntity.getModifyDate());
        blogEntityResource.setOwnerUsername(blogEntity.getOwner().getUsername());
        blogEntityResource.setOwnerId(blogEntity.getOwner().getUserId());

        blogEntityResource.add(linkTo(methodOn(BlogController.class).getBlogById(blogEntity.getOwner().getUserId(),blogEntity.getBlogId())).withSelfRel());
        blogEntityResource.add(linkTo(methodOn(BlogController.class).deleteBlog(blogEntity.getOwner().getUserId(),blogEntity.getBlogId())).withRel("delete"));
        blogEntityResource.add(linkTo(methodOn(BlogController.class).getAllBlogs(blogEntity.getOwner().getUserId(),null,null)).withRel("allHisBlogs"));

        return blogEntityResource;
    }
}
