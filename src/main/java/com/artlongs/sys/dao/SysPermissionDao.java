package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.sys.model.SysPermission;

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

        List<Long> roleIds = this.lq()
                .select(SysPermission::getRoleId)
                .andEq(SysPermission::getFuncId, funcid)
                .toList(Long.class);

        return roleIds;
    }
    public boolean realDelByFuncId(Long func_id) {
        return this.lq().andEq(funcId, func_id).toDel();
    }

    public List<SysPermission> getPermissionListByRoleid(Long roleid) {
//        String sql = " select * from sys_permission where role_id = ?";
        List<SysPermission> permissionList = this.lq().andEq(roleId, roleid).toList();
        return permissionList;
    }

    public SysPermission getPermissionOf(Long funcid, Long roleid) {
//        String sql = " select * from sys_permission where func_id=? and role_id = ?";
//        SysPermission sysPermission = getObj(sql, funcId, roleId);
        SysPermission sysPermission = this.lq().andEq(funcId,funcid).andEq(roleId,roleid).to();
        return sysPermission;
    }





}
