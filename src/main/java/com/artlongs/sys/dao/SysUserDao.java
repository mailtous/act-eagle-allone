package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.sys.model.SysUser;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysUserDao extends SysUser.Dao<SysUser> {

    public SysUser checkLogin(String userName, String pwd) {
        String sql = " select * from %s where %s =?";
        sql = String.format(sql, SysUser.Dao.table, SysUser.Dao.userName);
        SysUser sysUser = getObj(sql, userName);
        return (sysUser != null && sysUser.verifyPassword(pwd))?sysUser:null;
    }


}
