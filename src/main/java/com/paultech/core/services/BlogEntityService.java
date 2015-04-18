package com.paultech.core.services;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserBlogComment;
import com.paultech.core.entities.UserEntity;
import com.paultech.core.repo.BlogEntityRepo;
import com.paultech.core.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by paulzhang on 18/03/15.
 */
@Repository
public class BlogEntityService {

    @Autowired
    private BlogEntityRepo blogEntityRepo;

    public Page<BlogEntity> findAll(Pageable pageable) {
        return blogEntityRepo.findAll(pageable);
    }


    public Page<BlogEntity> findAll(UserEntity userEntity, Pageable pageable) {
        return blogEntityRepo.findByOwner(userEntity,pageable);
    }


    public BlogEntity findById(Long id) {
        BlogEntity blogEntity = blogEntityRepo.findOne(id);
        if(null == blogEntity) {
            throw new EntityNotFoundException("BlogEntity with the id: " + id + " does not exist.");
        } else {
            return blogEntity;
        }
    }

    @PreAuthorize("isAuthenticated()")
    public BlogEntity save(BlogEntity blogEntity) {
        blogEntityRepo.save(blogEntity);
        return blogEntity;
    }

    @PreAuthorize("isAuthenticated()")
    public void delete(BlogEntity blogEntity) {
        blogEntityRepo.delete(blogEntity);
    }

}
