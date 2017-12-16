package com.artlongs.sys.dao;

import act.Act;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.sys.model.SysUser;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysUserDao extends BeetlSqlDao<SysUser> {

    public boolean checkLogin(String userName, String pwd) {
        String pwdMd5 = Act.crypto().encrypt(pwd);
        String sql = " select count(1) from sys_user where user_name=? and pwd = ?";
        Long num = count(sql, new Object[]{userName, pwdMd5});
        return num > 0;
    }


}
