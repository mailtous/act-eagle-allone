package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.utils.QE;
import com.artlongs.sys.model.SysUser;

import static act.mail.Mailer.Util.from;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysUserDao extends SysUser.Dao<SysUser> {

    public SysUser checkLogin(String userName, String pwd) {
        String sql = QE.selectAll().from(SysUser.Dao.table).where(QE.k(SysUser.Dao.userName).eq(userName)).sql();
        SysUser sysUser = getObj(sql);
        return (sysUser != null && sysUser.verifyPassword(pwd))?sysUser:null;
    }


}
