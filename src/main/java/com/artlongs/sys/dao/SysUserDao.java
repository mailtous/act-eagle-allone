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
public class SysUserDao extends BeetlSqlDao<SysUser> {

    public SysUser checkLogin(String name, String pwd) {
         // beetlsql query
        SysUser  sysUser = sqlm.query(SysUser.class).lambda().andEq(SysUser::getUserName, name).single();
        return (sysUser != null && sysUser.verifyPassword(pwd)) ? sysUser : null;

    }


}
