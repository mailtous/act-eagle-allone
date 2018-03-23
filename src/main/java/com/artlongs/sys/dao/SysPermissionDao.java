package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.utils.Qee;
import com.artlongs.sys.model.SysPermission;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.query.Query;

import java.util.List;

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
        String sql = new Qee(SysPermission.class).select(roleId).where(new Qee().eq(funcId,funcid)).build();
        List<Long> roleIds = sqlm.execute(new SQLReady(sql), Long.class);
        Query q = sqlm.query(SysPermission.class);
        return roleIds;
    }
    public boolean realDelByFuncId(Long func_id) {
//        String sql = " delete from sys_permission where func_id = ?";
        String sql = Qee.del(table).where(new Qee().eq(funcId,func_id)).build();
        super.sqlm.executeUpdate(new SQLReady(sql, new Object[]{funcId}));
        return true;
    }

    public List<SysPermission> getPermissionListByRoleid(Long roleid) {
//        String sql = " select * from sys_permission where role_id = ?";
        String sql = new Qee(SysPermission.class).where(new Qee().eq(roleId,roleid)).build();
        List<SysPermission> permissionList = getList(sql);
        return permissionList;
    }

    public SysPermission getPermissionOf(Integer funcid, Integer roleid) {
//        String sql = " select * from sys_permission where func_id=? and role_id = ?";
//        SysPermission sysPermission = getObj(sql, funcId, roleId);
        String sql = new Qee(SysPermission.class).select().where(new Qee().eq(funcId,funcid).and(new Qee().eq(roleId,roleid))).build();
        SysPermission sysPermission = getObj(sql);
        return sysPermission;
    }



}
