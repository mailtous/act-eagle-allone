package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.utils.Lq;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.query.Query;

import java.util.List;


/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysUserDao extends SysUser.Dao<SysUser> {

    public SysUser checkLogin(String name, String pwd) {
         // beetlsql query
        SysUser  sysUser = sqlm.query(SysUser.class).lambda().andEq(SysUser::getUserName, name).single();
        return (sysUser != null && sysUser.verifyPassword(pwd)) ? sysUser : null;
    }


}
