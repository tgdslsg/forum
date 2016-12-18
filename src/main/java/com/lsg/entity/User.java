package com.lsg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by tgdsl on 2016/12/15.
 */
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer state;
    private String avatar;
    private Timestamp createTime;

    /**
     * 新用户默认头像key
     */
    public static final String DEFAULT_AVATAR_NAME = "default-avatar.jpg";
    /**
     * 用户状态:未激活
     */
    public static final Integer USERSTATE_UNACTIVE = 0;
    /**
     * 用户状态:已激活（正常）
     */
    public static final Integer USERSTATE_ACTIVE = 1;
    /**
     * 用户状态:禁用
     */
    public static final Integer USERSTATE_DISABLED = 2;


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", Email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
