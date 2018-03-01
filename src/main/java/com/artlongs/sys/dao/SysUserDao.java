package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.utils.QE;
import com.artlongs.sys.model.SysUser;


/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysUserDao extends SysUser.Dao<SysUser> {

    public SysUser checkLogin(String name, String pwd) {
        String sql = QE.selectAll().from(table).where(QE.k(userName).eq(name)).sql();
        SysUser sysUser = getObj(sql);
        return (sysUser != null && sysUser.verifyPassword(pwd))?sysUser:null;
    }


}
