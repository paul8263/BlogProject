package com.paultech.core.repo;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserBlogComment;
import com.paultech.core.entities.UserBlogCommentPK;
import com.paultech.core.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by paulzhang on 18/03/15.
 */
public interface UserBlogCommentRepo extends JpaRepository<UserBlogComment,UserBlogCommentPK> {
    public Page<UserBlogComment> findByUserBlogCommentPK_UserEntity(UserEntity userEntity, Pageable pageable);
    public Page<UserBlogComment> findByUserBlogCommentPK_BlogEntity(BlogEntity blogEntity, Pageable pageable);

}
