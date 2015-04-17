package com.paultech.core.services;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserEntity;
import com.paultech.core.repo.UserEntityRepo;
import com.paultech.core.services.exceptions.EntityNotFoundException;
import com.paultech.core.services.exceptions.UserNameConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by paulzhang on 18/03/15.
 */
@Repository
public class UserEntityService {
    @Autowired
    private UserEntityRepo userEntityRepo;

    public UserEntity findById(Long id) {
        UserEntity userEntity = userEntityRepo.findOne(id);
        if(null == userEntity) {
            throw new EntityNotFoundException("UserEntity with the id: " + id + " does not exist.");
        } else {
            return userEntity;
        }
    }

    public List<UserEntity> findAll() {
        return userEntityRepo.findAll();
    }

    public Page<UserEntity> findAllByPage(Pageable pageable) {
        return userEntityRepo.findAll(pageable);
    }

    public UserEntity findByUsername(String username) {
        UserEntity userEntity = userEntityRepo.findByUsername(username);
        if(userEntity == null) {
            throw new EntityNotFoundException("Cannot find userEntity named: " + username);
        } else {
            return userEntity;
        }
    }


    public UserEntity createUser(UserEntity userEntity) {
        UserEntity existingUserEntity = userEntityRepo.findByUsername(userEntity.getUsername());
        if(existingUserEntity != null) {
            throw new UserNameConflictException("User with the name: " + userEntity.getUsername() + " already exists.");
        } else {
            userEntityRepo.save(userEntity);
            return userEntity;
        }
    }

    public UserEntity updateUser(UserEntity userEntity) {
        userEntityRepo.save(userEntity);
        return userEntity;
    }

    public void deleteUser(UserEntity userEntity) {
        userEntityRepo.delete(userEntity);
    }
}
