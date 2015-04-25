package com.paultech.rest.controllers;

import com.paultech.core.SecurityUserService.MyBlogUserDetails;
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
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    @Autowired
    private String iconFileLocation;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public @ResponseBody UserEntityResource getCurrentLoggedInUserName() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntityResource userEntityResource;
        if(principle instanceof MyBlogUserDetails) {
            String username = ((MyBlogUserDetails)principle).getUsername();
            userEntityResource = userEntityResourceAsm.toResource(userEntityService.findByUsername(username));
        } else {
            userEntityResource = new UserEntityResource();
        }
        return userEntityResource;
    }

    @RequestMapping(value = "/{userId}/icon", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getUserIcon(@PathVariable("userId") Long userId) {
        InputStream inputStream = null;
        byte[] bytes = null;
        String extension = null;
        try {

            File iconFolder = new File(iconFileLocation);
            File[] icons = iconFolder.listFiles();

            String iconName = null;
            if(null != icons && icons.length != 0)
            for(File icon : icons) {
                Long userIdInIconFile = null;
                String[] splitFilename = icon.getName().split("\\.");
                if(splitFilename.length > 1 && !splitFilename[0].equals("")) {
                    userIdInIconFile = Long.parseLong(splitFilename[0]);
                }
                if(userIdInIconFile != null && userId.equals(userIdInIconFile)) {
                    iconName = icon.getName();
                    extension = splitFilename[splitFilename.length - 1];
                    break;
                }
            }

            if(null != iconName) {
                Resource icon = new FileSystemResource(iconFileLocation + iconName);inputStream = icon.getInputStream();
                bytes = IOUtils.toByteArray(inputStream);
            } else {
                Resource icon = new FileSystemResource(iconFileLocation + "default");
                inputStream = icon.getInputStream();
                bytes = IOUtils.toByteArray(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        if(null != extension) {
            if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if(extension.equalsIgnoreCase("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }
        }

        return new ResponseEntity<byte[]>(bytes,headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}/icon", method = RequestMethod.POST)
    public @ResponseBody String setUserIcon(@PathVariable("userId") Long userId, MultipartFile icon) {
        if (icon == null) {
            throw new BadRequestException("No image is uploaded");
        }

        if(!getAuthenticatedUserId(userEntityService).equals(userId)) {
            throw new ForbiddenException("Id of the authenticated user is different from the userId in URL");
        }

        try {

            String[] splitFilename = icon.getOriginalFilename().split("\\.");
            String extension = splitFilename[splitFilename.length - 1];
            if("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension)) {
                String[] extensions = {"jpg","jpeg","png"};
                File[] iconFiles = new File(iconFileLocation).listFiles();
                if(null != iconFiles) {
                    for(String str : extensions) {
                        for(File file : iconFiles) {
                            if(file.getName().equalsIgnoreCase(userId + "." + str)) file.delete();
                        }
                    }
                }
                File iconFile = new File(iconFileLocation + userId + "." + extension);
                icon.transferTo(iconFile);
            } else {
                throw new BadRequestException();
            }

        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody UserEntityResource createUser(MultipartFile icon,@Valid UserEntityResource userEntityResource, BindingResult result) {


        if (result.hasErrors()) {
            throw new BadRequestException("User information is illegal");
        }

        try {
            UserEntity userEntity = userEntityResource.toUserEntity();

            userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));

            userEntityService.createUser(userEntity);
            userEntity = userEntityService.findByUsername(userEntityResource.getUsername());

            if(icon != null) {
                String[] splitFilename = icon.getOriginalFilename().split("\\.");
                String extension = splitFilename[splitFilename.length - 1];
                if("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension)) {
                    String iconPath = iconFileLocation + userEntity.getUserId() + "." + extension;
                    icon.transferTo(new File(iconPath));
                } else {
                    throw new BadRequestException();
                }
            }


            return userEntityResourceAsm.toResource(userEntity);
        } catch (UserNameConflictException e) {
            throw new ConflictException("Username: \"" + userEntityResource.getUsername() + "\" already  exists.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException();
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
    public @ResponseBody UserEntityResource updateUser(@PathVariable("userId") Long userId,@RequestBody @Valid UserEntityResource userEntityResource, BindingResult result) {

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

            String[] extensions = {"jpg","jpeg","png"};
            File[] iconFiles = new File(iconFileLocation).listFiles();
            if(null != iconFiles) {
                for(String str : extensions) {
                    for(File file : iconFiles) {
                        if(file.getName().equalsIgnoreCase(userId + "." + str)) file.delete();
                    }
                }
            }

            return userEntityResourceAsm.toResource(userEntity);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("User with id: " + userId + " does not exist.");
        }

    }


}
