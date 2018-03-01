package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.utils.QE;
import com.artlongs.sys.model.SysPermission;
import org.beetl.sql.core.SQLReady;

import java.util.List;

import static com.artlongs.sys.model.SysUser.Dao.roleIds;
import static com.artlongs.sys.model.SysUser.Dao.table;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysPermissionDao extends SysPermission.Dao<SysPermission> {

    public List<Long> getRoleIdsOfFuncId(Long funcid) {
//        String sql = " select role_id from sys_permission where func_id = ?";
        String sql = new QE().select(roleId).from(table).where(QE.k(funcId).eq(funcid)).sql();
        List<Long> roleIds = sqlm.execute(new SQLReady(sql), Long.class);
        return roleIds;
    }
    public boolean realDelByFuncId(Long func_id) {
//        String sql = " delete from sys_permission where func_id = ?";
        String sql = QE.del(table).where(QE.k(funcId).eq(func_id)).sql();
        super.sqlm.executeUpdate(new SQLReady(sql, new Object[]{funcId}));
        return true;
    }

    public List<SysPermission> getPermissionListByRoleid(Long roleid) {
//        String sql = " select * from sys_permission where role_id = ?";
        String sql = QE.selectAll().from(table).where(QE.k(roleId).eq(roleid)).sql();
        List<SysPermission> permissionList = getList(sql);
        return permissionList;
    }

    public SysPermission getPermissionOf(Integer funcid, Integer roleid) {
//        String sql = " select * from sys_permission where func_id=? and role_id = ?";
//        SysPermission sysPermission = getObj(sql, funcId, roleId);
        String sql = QE.selectAll().from(table).where(QE.k(funcId).eq(funcid).and(QE.k(roleId).eq(roleid))).sql();
        SysPermission sysPermission = getObj(sql);
        return sysPermission;
    }



}
