package com.paultech.rest.controllers;

import com.paultech.core.SecurityUserService.MyBlogUserDetails;
import com.paultech.core.services.UserEntityService;
import com.paultech.rest.controllers.exceptions.ForbiddenException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by paulzhang on 18/04/15.
 */
public class ParentController {

    protected Long getAuthenticatedUserId(UserEntityService userEntityService) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId;
        if(principle instanceof MyBlogUserDetails) {
            String username = ((MyBlogUserDetails)principle).getUsername();
            userId = userEntityService.findByUsername(username).getUserId();
        } else {
            throw new ForbiddenException("User has not logged in");
        }
        return userId;
    }

}
