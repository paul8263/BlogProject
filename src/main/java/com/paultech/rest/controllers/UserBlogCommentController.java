package com.paultech.rest.controllers;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserBlogComment;
import com.paultech.core.entities.UserBlogCommentPK;
import com.paultech.core.entities.UserEntity;
import com.paultech.core.services.BlogEntityService;
import com.paultech.core.services.UserBlogCommentService;
import com.paultech.core.services.UserEntityService;
import com.paultech.core.services.exceptions.EntityNotFoundException;
import com.paultech.rest.controllers.exceptions.BadRequestException;
import com.paultech.rest.controllers.exceptions.ForbiddenException;
import com.paultech.rest.controllers.exceptions.NotFoundException;
import com.paultech.rest.resources.UserBlogCommentResource;
import com.paultech.rest.resources.asm.UserBlogCommentResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by paulzhang on 6/04/15.
 */
@Controller
@RequestMapping(value = "/comment")
public class UserBlogCommentController extends ParentController {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private BlogEntityService blogEntityService;

    @Autowired
    private UserBlogCommentService userBlogCommentService;

    @Autowired
    private UserBlogCommentResourceAsm userBlogCommentResourceAsm;

    @RequestMapping(value = "/user/{userId}",method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody
    PagedResources<UserBlogCommentResource> getUserBlogCommentByUserId(@PathVariable("userId") Long userId, Pageable pageable,PagedResourcesAssembler assembler) {
        try {
            UserEntity userEntity = userEntityService.findById(userId);
            return assembler.toResource(userBlogCommentService.findByUserEntity(userEntity, pageable),userBlogCommentResourceAsm);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        }
    }

    @RequestMapping(value = "/blog/{blogId}",method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody PagedResources<UserBlogCommentResource> getUserBlogCommentByBlogId(@PathVariable("blogId") Long blogId, Pageable pageable,PagedResourcesAssembler assembler) {
        try {
            BlogEntity blogEntity = blogEntityService.findById(blogId);
            return assembler.toResource(userBlogCommentService.findByBlogEntity(blogEntity, pageable),userBlogCommentResourceAsm);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + blogId + " does not exist");
        }

    }

    @RequestMapping(value = "/user/{userId}/blog/{blogId}",method = RequestMethod.GET)
    public @ResponseBody UserBlogCommentResource getUserBlogCommentByUserIdAndBlogId(@PathVariable("userId") Long userId,@PathVariable("blogId") Long blogId) {
        try {
            UserBlogCommentPK pk = new UserBlogCommentPK();
            UserEntity userEntity = userEntityService.findById(userId);
            BlogEntity blogEntity = blogEntityService.findById(blogId);
            pk.setUserEntity(userEntity);
            pk.setBlogEntity(blogEntity);

            UserBlogComment userBlogComment = userBlogCommentService.findById(pk);

            return userBlogCommentResourceAsm.toResource(userBlogComment);

        } catch (EntityNotFoundException e) {
            throw new NotFoundException("UserEntity or BlogEntity does not exist");
        }
    }

    @RequestMapping(value = "/user/{userId}/blog/{blogId}",method = RequestMethod.POST)
    public @ResponseBody UserBlogCommentResource createUserBlogCommentByUserIdAndBlogId(@PathVariable("userId") Long userId,@PathVariable("blogId") Long blogId, @RequestBody @Valid UserBlogCommentResource userBlogCommentResource, BindingResult result) {

        if(result.hasErrors()) {
            throw new BadRequestException("Comment is illegal");
        }

        if(!getAuthenticatedUserId(userEntityService).equals(userId)) {
            throw new ForbiddenException("Id of the authenticated user is different from the userId in URL");
        }

        try {
            UserBlogCommentPK pk = new UserBlogCommentPK();
            UserEntity userEntity = userEntityService.findById(userId);
            BlogEntity blogEntity = blogEntityService.findById(blogId);
            pk.setUserEntity(userEntity);
            pk.setBlogEntity(blogEntity);
            UserBlogComment userBlogComment = userBlogCommentResource.toUserBlogComment();
            userBlogComment.setUserBlogCommentPK(pk);
            userBlogCommentService.save(userBlogComment);
            return userBlogCommentResourceAsm.toResource(userBlogComment);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("UserEntity or BlogEntity does not exist");
        }
    }

    @RequestMapping(value = "/user/{userId}/blog/{blogId}",method = RequestMethod.PUT)
    public @ResponseBody UserBlogCommentResource updateUserBlogCommentByUserIdAndBlogId(@PathVariable("userId") Long userId,@PathVariable("blogId") Long blogId, @RequestBody @Valid UserBlogCommentResource userBlogCommentResource, BindingResult result) {

        if(result.hasErrors()) {
            throw new BadRequestException("Comment is illegal");
        }

        if(!getAuthenticatedUserId(userEntityService).equals(userId)) {
            throw new ForbiddenException("Id of the authenticated user is different from the userId in URL");
        }

        try {
            UserBlogCommentPK pk = new UserBlogCommentPK();
            UserEntity userEntity = userEntityService.findById(userId);
            BlogEntity blogEntity = blogEntityService.findById(blogId);
            pk.setUserEntity(userEntity);
            pk.setBlogEntity(blogEntity);
            UserBlogComment userBlogComment = userBlogCommentResource.toUserBlogComment();
            userBlogComment.setUserBlogCommentPK(pk);
            userBlogCommentService.update(userBlogComment);
            return userBlogCommentResourceAsm.toResource(userBlogComment);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("UserEntity or BlogEntity does not exist");
        }
    }


    @RequestMapping(value = "/user/{userId}/blog/{blogId}",method = RequestMethod.DELETE)
    public @ResponseBody UserBlogCommentResource deleteUserBlogCommentByUserIdAndBlogId(@PathVariable("userId") Long userId,@PathVariable("blogId") Long blogId) {

        if(!getAuthenticatedUserId(userEntityService).equals(userId)) {
            throw new ForbiddenException("Id of the authenticated user is different from the userId in URL");
        }

        try {
            UserBlogCommentPK pk = new UserBlogCommentPK();
            UserEntity userEntity = userEntityService.findById(userId);
            BlogEntity blogEntity = blogEntityService.findById(blogId);
            pk.setUserEntity(userEntity);
            pk.setBlogEntity(blogEntity);
            UserBlogComment userBlogComment = userBlogCommentService.findById(pk);
            userBlogCommentService.delete(userBlogComment);
            return userBlogCommentResourceAsm.toResource(userBlogComment);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("UserEntity or BlogEntity does not exist");
        }
    }

}
