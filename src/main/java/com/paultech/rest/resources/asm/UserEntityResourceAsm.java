package com.paultech.rest.resources.asm;

import com.paultech.core.entities.UserEntity;
import com.paultech.rest.controllers.BlogController;
import com.paultech.rest.controllers.UserController;
import com.paultech.rest.resources.UserEntityResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulzhang on 4/04/15.
 */
@Component
public class UserEntityResourceAsm extends ResourceAssemblerSupport<UserEntity,UserEntityResource> {

    public UserEntityResourceAsm() {
        super(UserController.class, UserEntityResource.class);
    }


    @Override
    public List<UserEntityResource> toResources(Iterable<? extends UserEntity> entities) {
        List<UserEntityResource> userEntityResources = new ArrayList<>();
        for(UserEntity userEntity : entities) {
            UserEntityResource resource = this.toResource(userEntity);
            userEntityResources.add(resource);
        }

        return userEntityResources;
    }

    @Override
    public UserEntityResource toResource(UserEntity userEntity) {
        UserEntityResource userEntityResource = new UserEntityResource();
        userEntityResource.setUserId(userEntity.getUserId());
        userEntityResource.setUsername(userEntity.getUsername());
        userEntityResource.setPassword(userEntity.getPassword());
        userEntityResource.setGender(userEntity.getGender());
        userEntityResource.setBirthday(userEntity.getBirthday());
        userEntityResource.setSelfIntroduce(userEntity.getSelfIntroduce());

        userEntityResource.add(linkTo(methodOn(UserController.class).getUserById(userEntity.getUserId())).withSelfRel());
        userEntityResource.add(linkTo(methodOn(BlogController.class).getAllBlogs(userEntity.getUserId(),null,null)).withRel("allBlogs"));

        return userEntityResource;
    }
}
