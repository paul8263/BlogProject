package com.paultech.rest.controllers;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserEntity;
import com.paultech.core.services.BlogEntityService;
import com.paultech.core.services.UserEntityService;
import com.paultech.core.services.exceptions.EntityNotFoundException;
import com.paultech.rest.controllers.exceptions.ForbiddenException;
import com.paultech.rest.controllers.exceptions.NotFoundException;
import com.paultech.rest.resources.BlogEntityResource;
import com.paultech.rest.resources.asm.BlogEntityResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by paulzhang on 6/04/15.
 */
@Controller
public class BlogController {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private BlogEntityService blogEntityService;

    @Autowired
    private BlogEntityResourceAsm blogEntityResourceAsm;


    @RequestMapping(value = "/blog", method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody PagedResources<BlogEntityResource> getAllBlogs(Pageable pageable, PagedResourcesAssembler assembler) {
        return assembler.toResource(blogEntityService.findAll(pageable), blogEntityResourceAsm);
    }


    @RequestMapping(value = "/user/{userId}/blog", method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody
    PagedResources<BlogEntityResource> getAllBlogs(@PathVariable("userId") Long userId, Pageable pageable, PagedResourcesAssembler assembler) {
        try {
            UserEntity userEntity = userEntityService.findById(userId);
            return assembler.toResource(blogEntityService.findAll(userEntity,pageable),blogEntityResourceAsm);

        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        }
    }

    @RequestMapping(value = "/user/{userId}/blog", method = RequestMethod.POST)
    public @ResponseBody BlogEntityResource createBlog(@PathVariable("userId") Long userId, @RequestBody BlogEntityResource blogEntityResource) {
        try {
            UserEntity userEntity = userEntityService.findById(userId);
            BlogEntity blogEntity = blogEntityResource.toBlogEntity();
            blogEntity.setOwner(userEntity);
            blogEntityService.save(blogEntity);
            return blogEntityResourceAsm.toResource(blogEntity);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        }
    }


    @RequestMapping(value = "/user/{userId}/blog/{blogId}", method = RequestMethod.PUT)
    public @ResponseBody BlogEntityResource updateBlogById(@PathVariable("userId") Long userId, @PathVariable("blogId") Long blogId, @RequestBody BlogEntityResource blogEntityResource) {
        try {
            UserEntity userEntity = userEntityService.findById(userId);
            BlogEntity blogEntity = blogEntityResource.toBlogEntity();
            blogEntity.setBlogId(blogId);
            blogEntity.setOwner(userEntity);
            blogEntityService.save(blogEntity);
            return blogEntityResourceAsm.toResource(blogEntity);

        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        }
    }


    @RequestMapping(value = "/user/{userId}/blog/{blogId}", method = RequestMethod.GET)
    public @ResponseBody BlogEntityResource getBlogById(@PathVariable("userId") Long userId, @PathVariable("blogId") Long blogId) {
        try {
            BlogEntity blogEntity = blogEntityService.findById(blogId);

            if(blogEntity.getOwner().getUserId().equals(userId)) {
                return blogEntityResourceAsm.toResource(blogEntity);
            } else {
                throw new ForbiddenException("User with id: " + userId + " does not have blog with id: " + blogId);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        }
    }

    @RequestMapping(value = "/user/{userId}/blog/{blogId}",method = RequestMethod.DELETE)
    public @ResponseBody BlogEntityResource deleteBlog(@PathVariable("userId") Long userId, @PathVariable("blogId") Long blogId) {
        try {
            BlogEntity blogEntity = blogEntityService.findById(blogId);

            if(blogEntity.getOwner().getUserId().equals(userId)) {
                blogEntityService.delete(blogEntity);
                return blogEntityResourceAsm.toResource(blogEntity);
            } else {
                throw new ForbiddenException("User with id: " + userId + " does not have blog with id: " + blogId);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        }
    }

}
