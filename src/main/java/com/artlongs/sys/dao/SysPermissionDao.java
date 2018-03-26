package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.utils.Lq;
import com.artlongs.framework.utils.Qe;
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

        String sql = new Lq<SysPermission>(SysPermission.class)
                .select(SysPermission::getRoleId)
                .where(new Lq<SysPermission>().eq(SysPermission::getFuncId,funcid))
                .build();

        List<Long> roleIds = sqlm.execute(new SQLReady(sql), Long.class);
        return roleIds;
    }
    public boolean realDelByFuncId(Long func_id) {
//        String sql = " delete from sys_permission where func_id = ?";
        String sql = Qe.del(table).where(new Qe().eq(funcId,func_id)).build();
        super.sqlm.executeUpdate(new SQLReady(sql, new Object[]{funcId}));
        return true;
    }

    public List<SysPermission> getPermissionListByRoleid(Long roleid) {
//        String sql = " select * from sys_permission where role_id = ?";
        String sql = new Qe(SysPermission.class).where(new Qe().eq(roleId,roleid)).build();
        List<SysPermission> permissionList = getList(sql);
        return permissionList;
    }

    public SysPermission getPermissionOf(Integer funcid, Integer roleid) {
//        String sql = " select * from sys_permission where func_id=? and role_id = ?";
//        SysPermission sysPermission = getObj(sql, funcId, roleId);
        String sql = new Qe(SysPermission.class).where(new Qe().eq(funcId,funcid).and(new Qe().eq(roleId,roleid))).build();
        SysPermission sysPermission = getObj(sql);
        return sysPermission;
    }



}
