package com.paultech.rest.controllers;

import com.paultech.core.entities.UserEntity;
import com.paultech.core.services.UserEntityService;
import com.paultech.core.services.exceptions.EntityNotFoundException;
import com.paultech.core.services.exceptions.UserNameConflictException;
import com.paultech.rest.controllers.exceptions.BadRequestException;
import com.paultech.rest.controllers.exceptions.ConflictException;
import com.paultech.rest.controllers.exceptions.ForbiddenException;
import com.paultech.rest.controllers.exceptions.NotFoundException;
import com.paultech.rest.resources.UserEntityResource;
import com.paultech.rest.resources.asm.UserEntityResourceAsm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by paulzhang on 21/03/15.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends ParentController {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private UserEntityResourceAsm userEntityResourceAsm;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody UserEntityResource createUser(@RequestBody @Valid UserEntityResource userEntityResource, BindingResult result) {

        if (result.hasErrors()) {
            throw new BadRequestException("User information is illegal");
        }

        try {
            UserEntity userEntity = userEntityResource.toUserEntity();

            userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));

            userEntityService.createUser(userEntity);
            userEntity = userEntityService.findByUsername(userEntityResource.getUsername());
            return userEntityResourceAsm.toResource(userEntity);
        } catch (UserNameConflictException e) {
            throw new ConflictException("Username: \"" + userEntityResource.getUsername() + "\" already  exists.");
        }
    }



    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody PagedResources<UserEntityResource> getAllUsers(Pageable pageable, PagedResourcesAssembler assembler) {
        return assembler.toResource(userEntityService.findAllByPage(pageable),userEntityResourceAsm);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public @ResponseBody UserEntityResource getUserById(@PathVariable("userId") Long id) {
        try {
            UserEntity userEntity = userEntityService.findById(id);
            return userEntityResourceAsm.toResource(userEntity);
        } catch(EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + id + " does not exist");
        }
    }

    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    public @ResponseBody UserEntityResource getUserByUsername(@PathVariable("username") String username) {
        try {
            UserEntity userEntity = userEntityService.findByUsername(username);
            return userEntityResourceAsm.toResource(userEntity);
        } catch (EntityNotFoundException e ) {
            throw new NotFoundException("User with name: " + username + " does not exist");
        }
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public @ResponseBody UserEntityResource updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UserEntityResource userEntityResource, BindingResult result) {

        if (result.hasErrors()) {
            throw new BadRequestException("User information is illegal");
        }

        if(!getAuthenticatedUserId(userEntityService).equals(userId)) {
            throw new ForbiddenException("Id of the authenticated user is different from the userId in URL");
        }

        userEntityResource.setUserId(userId);
        UserEntity userEntity;
        try {
            userEntity = userEntityService.findById(userId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist.");
        }
        if (userEntityResource.getOldPassword() == null || userEntityResource.getOldPassword().equals("")) {

            UserEntity userEntityToUpdate = userEntityResource.toUserEntity();
            userEntityToUpdate.setPassword(userEntity.getPassword());
            userEntityService.updateUser(userEntityToUpdate);
            return userEntityResourceAsm.toResource(userEntityService.findById(userId));

        } else if(bCryptPasswordEncoder.matches(userEntityResource.getOldPassword(),userEntity.getPassword())) {

            UserEntity userEntityToUpdate = userEntityResource.toUserEntity();
            userEntityToUpdate.setPassword(bCryptPasswordEncoder.encode(userEntityToUpdate.getPassword()));
            userEntityService.updateUser(userEntityToUpdate);
            return userEntityResourceAsm.toResource(userEntityService.findById(userId));

        } else {

            throw new ForbiddenException("User with username: " + userEntity.getUsername() + " old password does not match.");
        }

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public @ResponseBody UserEntityResource deleteUser(@PathVariable("userId") Long userId) {

        if(!getAuthenticatedUserId(userEntityService).equals(userId)) {
            throw new ForbiddenException("Id of the authenticated user is different from the userId in URL");
        }

        try {
            UserEntity userEntity = userEntityService.findById(userId);
            userEntityService.deleteUser(userEntity);

            return userEntityResourceAsm.toResource(userEntity);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist.");
        }

    }


}
