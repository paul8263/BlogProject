package com.paultech.core.services;

import com.paultech.core.entities.BlogEntity;
import com.paultech.core.entities.UserBlogComment;
import com.paultech.core.entities.UserBlogCommentPK;
import com.paultech.core.entities.UserEntity;
import com.paultech.core.repo.UserBlogCommentRepo;
import com.paultech.core.services.exceptions.DuplicatedCommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Created by paulzhang on 21/03/15.
 */
@Repository
public class UserBlogCommentService {

    @Autowired
    private UserBlogCommentRepo userBlogCommentRepo;


    public Page<UserBlogComment> findByUserEntity(UserEntity userEntity, Pageable pageable) {
        return userBlogCommentRepo.findByUserBlogCommentPK_UserEntity(userEntity,pageable);
    }

    public Page<UserBlogComment> findByBlogEntity(BlogEntity blogEntity, Pageable pageable) {
        return userBlogCommentRepo.findByUserBlogCommentPK_BlogEntity(blogEntity,pageable);
    }

    public UserBlogComment findById(UserBlogCommentPK userBlogCommentPK) {
        return userBlogCommentRepo.findOne(userBlogCommentPK);
    }

    public UserBlogComment save(UserBlogComment userBlogComment) {
        UserBlogComment existingUserBlogComment = userBlogCommentRepo.findOne(userBlogComment.getUserBlogCommentPK());
        if(existingUserBlogComment != null) {
            throw new DuplicatedCommentException("Comment with Username: " + userBlogComment.getUserBlogCommentPK().getUserEntity().getUsername()
            + " and " + userBlogComment.getUserBlogCommentPK().getBlogEntity().getOwner().getUsername() + "'s " + " Blog named: "
            + userBlogComment.getUserBlogCommentPK().getBlogEntity().getTitle() + " has already existed!");
        } else {
            userBlogCommentRepo.save(userBlogComment);
            return userBlogComment;
        }
    }

    public UserBlogComment update(UserBlogComment userBlogComment) {
        userBlogCommentRepo.save(userBlogComment);
        return userBlogComment;
    }

    public void delete(UserBlogComment userBlogComment) {
        userBlogCommentRepo.delete(userBlogComment);
    }
}
