package com.artlongs.test;

import com.artlongs.framework.model.BaseEntity;

import javax.persistence.Entity;

/**
 * Created by leeton on 9/27/17.
 */
public class User extends BaseEntity {

    private String userName;
    private String pwd;
    private Integer roleId;


    public User(String userName, String pwd, Integer roleId) {
        super();
        this.userName = userName;
        this.pwd = pwd;
        this.roleId = roleId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}

