package com.artlongs.sys.service;

import act.util.Stateless;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.dao.SysPermissionDao;
import com.artlongs.sys.model.RoleAssignVo;
import com.artlongs.sys.model.SysPermission;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
@Stateless
public class SysPermissionService extends BaseServiceImpl<SysPermission> {

    private SysPermissionDao sysPermissionDao;

    @Inject
    public SysPermissionService(SysPermissionDao sysPermissionDao) {
        baseDao = sysPermissionDao;
        this.sysPermissionDao = sysPermissionDao;
    }

    public List<SysPermission> getPermissionList(Long roleId) {
        List<SysPermission> permissionList = sysPermissionDao.getPermissionListByRoleid(roleId);
        return permissionList;
    }

    public SysPermission getPermissionOf(Long funcId, Long roleId) {
        return sysPermissionDao.getPermissionOf(funcId, roleId);
    }
    public List<Long> getRoleIdsOfFuncId(Long funcId) {
        return sysPermissionDao.getRoleIdsOfFuncId(funcId);
    }

    public Map<Long, Boolean> roleMapOfpermission(List<Long> roleIds) {
        Map<Long, Boolean> roleMap = C.newMap();
        for (Long roleId : roleIds) {
            roleMap.put(roleId, true);
        }
        return roleMap;
    }

    public boolean realDelByFuncId(Long funcId) {
        return sysPermissionDao.realDelByFuncId(funcId);
    }

    public R savePermissionOfAssign(List<RoleAssignVo> roleAssignVoList){
        if(C.empty(roleAssignVoList)) return R.fail("权限分配没有指定角色。");

        for (RoleAssignVo roleAssignVo : roleAssignVoList) {
            SysPermission sysPermissionOfDB = getPermissionOf(roleAssignVo.getFuncId(), roleAssignVo.getRoleId());
            if (RoleAssignVo.on == roleAssignVo.getOnoff()) { // 添加角色的权限
                if (null == sysPermissionOfDB) {
                    SysPermission sysPermission = new SysPermission();
                    sysPermission.setRoleId(new Long(roleAssignVo.getRoleId()));
                    sysPermission.setFuncId(new Long(roleAssignVo.getFuncId()));
                    sysPermission.setCreateDate(new Date());
                    sysPermission.setModifyDate(new Date());
                    sysPermissionDao.save(sysPermission);
                }
            }

            if (RoleAssignVo.off == roleAssignVo.getOnoff()) {// 删除角色的权限
                if (null != sysPermissionOfDB) {
                    sysPermissionDao.delete(sysPermissionOfDB);
                }
            }

        }
        SysRoleService.clearRoleCache();
        return R.success("角色的权限设置成功。");
    }






}
