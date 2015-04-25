package com.paultech.rest.resources.asm;

import com.paultech.core.entities.UserBlogComment;
import com.paultech.rest.controllers.UserBlogCommentController;
import com.paultech.rest.resources.UserBlogCommentResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


/**
 * Created by paulzhang on 7/04/15.
 */
@Component
public class UserBlogCommentResourceAsm extends ResourceAssemblerSupport<UserBlogComment,UserBlogCommentResource> {
    public UserBlogCommentResourceAsm() {
        super(UserBlogCommentController.class, UserBlogCommentResource.class);
    }

    @Override
    public List<UserBlogCommentResource> toResources(Iterable<? extends UserBlogComment> entities) {
        List<UserBlogCommentResource> userBlogCommentResources = new ArrayList<>();
        for(UserBlogComment userBlogComment : entities) {
            UserBlogCommentResource userBlogCommentResource = this.toResource(userBlogComment);
            userBlogCommentResources.add(userBlogCommentResource);
        }
        return userBlogCommentResources;
    }

    @Override
    public UserBlogCommentResource toResource(UserBlogComment userBlogComment) {
        UserBlogCommentResource userBlogCommentResource = new UserBlogCommentResource();
        userBlogCommentResource.setComment(userBlogComment.getComment());
        userBlogCommentResource.setCommentDate(userBlogComment.getCommentDate());
        userBlogCommentResource.setCommenterUsername(userBlogComment.getUserEntity().getUsername());
        userBlogCommentResource.setCommenterUserId(userBlogComment.getUserEntity().getUserId());

        userBlogCommentResource.add(linkTo(methodOn(UserBlogCommentController.class).getUserBlogCommentByUserIdAndBlogId(userBlogComment.getUserEntity().getUserId(),userBlogComment.getBlogEntity().getBlogId())).withSelfRel());
        userBlogCommentResource.add(linkTo(methodOn(UserBlogCommentController.class).deleteUserBlogCommentByUserIdAndBlogId(userBlogComment.getUserEntity().getUserId(),userBlogComment.getBlogEntity().getBlogId())).withRel("delete"));

        return userBlogCommentResource;
    }
}
