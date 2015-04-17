package com.paultech.core.repo;

import com.paultech.core.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by paulzhang on 18/03/15.
 */
public interface UserEntityRepo extends JpaRepository<UserEntity,Long> {

    public UserEntity findByUsername(String username);

}
