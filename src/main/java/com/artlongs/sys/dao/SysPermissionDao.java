package com.artlongs.sys.dao;

import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.sys.model.SysPermission;
import org.beetl.sql.core.SQLReady;

import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysPermissionDao extends BeetlSqlDao<SysPermission> {

    public List<Long> getRoleIdsOfFuncId(Long funcId) {
        String sql = " select role_id from sys_permission where func_id = ?";
        List<Long> roleIds = sqlm.execute(new SQLReady(sql, new Object[]{funcId}), Long.class);
        return roleIds;
    }
    public boolean realDelByFuncId(Long funcId) {
        String sql = " delete from sys_permission where func_id = ?";
        super.sqlm.executeUpdate(new SQLReady(sql, new Object[]{funcId}));
        return true;
    }

    public List<SysPermission> getPermissionListByRoleid(Long roleId) {
        String sql = " select * from sys_permission where role_id = ?";
        List<SysPermission> permissionList = getList(sql, new Object[]{roleId});
        return permissionList;
    }

    public SysPermission getPermissionOf(Integer funcId, Integer roleId) {
        String sql = " select * from sys_permission where func_id=? and role_id = ?";
        SysPermission sysPermission = getObj(sql, funcId, roleId);
        return sysPermission;
    }



}
