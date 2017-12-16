package com.artlongs.test;

import com.artlongs.framework.service.BaseServiceImpl;

import javax.inject.Inject;

/**
 * Created by leeton on 10/10/17.
 */
public class UserService extends BaseServiceImpl<User> {

    public UserService(){}
    private UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.baseDao = userDao;
        this.userDao = userDao;
    }
}
