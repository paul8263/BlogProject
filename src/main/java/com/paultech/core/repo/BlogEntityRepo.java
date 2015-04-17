package com.paultech.core.repo;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserBlogComment;
import com.paultech.core.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by paulzhang on 18/03/15.
 */
public interface BlogEntityRepo extends JpaRepository<BlogEntity,Long> {

    public Page<BlogEntity> findByOwner(UserEntity userEntity, Pageable pageable);

}
