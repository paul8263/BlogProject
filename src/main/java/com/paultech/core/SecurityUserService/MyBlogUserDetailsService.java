package com.paultech.core.SecurityUserService;

import com.paultech.core.entities.UserEntity;
import com.paultech.core.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by paulzhang on 17/04/15.
 */
@Service
public class MyBlogUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityService userEntityService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserEntity userEntity = userEntityService.findByUsername(s);
        return new MyBlogUserDetails(userEntity);

    }
}
