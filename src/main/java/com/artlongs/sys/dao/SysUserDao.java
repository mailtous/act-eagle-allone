package com.artlongs.sys.dao;

import act.Act;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.SQLReady;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysUserDao extends BeetlSqlDao<SysUser> {

    public SysUser checkLogin(String userName, String pwd) {
        String sql = " select * from sys_user where user_name=?";
        SysUser sysUser = getObj(sql, userName);
        return (sysUser != null && sysUser.verifyPassword(pwd))?sysUser:null;
    }


}
