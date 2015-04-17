package com.paultech.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by paulzhang on 18/03/15.
 */
@Entity
@Table(name = "user", schema = "", catalog = "myblog")
public class UserEntity implements Serializable {
    private Long userId;
    private String username;
    private String password;
    private Gender gender;
    private Date birthday;
    private String selfIntroduce;
    private String iconPath;
    private String role;


    private List<BlogEntity> blogEntityList = new ArrayList<BlogEntity>(0);


    private List<UserBlogComment> userBlogComments = new ArrayList<>(0);

    @Id
    @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(length = 32,unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 32)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name = "self_introduce",length = 300)
    public String getSelfIntroduce() {
        return selfIntroduce;
    }

    public void setSelfIntroduce(String selfIntroduce) {
        this.selfIntroduce = selfIntroduce;
    }

    @Column(name = "icon_path")
    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @JsonIgnore
    @Column(length = 20)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    @OneToMany(mappedBy = "owner",fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    public List<BlogEntity> getBlogEntityList() {
        return blogEntityList;
    }

    public void setBlogEntityList(List<BlogEntity> blogEntityList) {
        this.blogEntityList = blogEntityList;
    }

//    @OneToMany(mappedBy = "userBlogCommentPK.userEntity",fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "userBlogCommentPK.userEntity",orphanRemoval = true,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
//    Generate "on delete cascade" in the foreign key.
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @OneToMany(mappedBy = "userBlogCommentPK.userEntity")
    @JsonIgnore
    public List<UserBlogComment> getUserBlogComments() {
        return userBlogComments;
    }

    public void setUserBlogComments(List<UserBlogComment> userBlogComments) {
        this.userBlogComments = userBlogComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (gender != that.gender) return false;
        if (iconPath != null ? !iconPath.equals(that.iconPath) : that.iconPath != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (selfIntroduce != null ? !selfIntroduce.equals(that.selfIntroduce) : that.selfIntroduce != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (selfIntroduce != null ? selfIntroduce.hashCode() : 0);
        result = 31 * result + (iconPath != null ? iconPath.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
