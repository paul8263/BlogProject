package com.paultech.rest.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paultech.core.entities.Gender;
import com.paultech.core.entities.UserEntity;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;


/**
 * Created by paulzhang on 4/04/15.
 */
public class UserEntityResource extends ResourceSupport {
    private Long userId;
    @NotNull
    @Size(min = 6, max = 16)
    private String username;
    private String oldPassword;
    @NotNull
    @Size(min = 6, max = 16)
    private String password;
    @NotNull
    private Gender gender;
    @Past
    private Date birthday;
    @Size(max = 300)
    private String selfIntroduce;
    private String iconPath;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getOldPassword() {
        return oldPassword;
    }

    @JsonProperty
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSelfIntroduce() {
        return selfIntroduce;
    }

    public void setSelfIntroduce(String selfIntroduce) {
        this.selfIntroduce = selfIntroduce;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public UserEntity toUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(this.userId);
        userEntity.setUsername(this.username);
        userEntity.setPassword(this.password);
        userEntity.setGender(this.gender);
        userEntity.setBirthday(this.birthday);
        userEntity.setIconPath(this.iconPath);
        userEntity.setSelfIntroduce(this.selfIntroduce);
        userEntity.setRole("ROLE_USER");

        return userEntity;
    }
}
