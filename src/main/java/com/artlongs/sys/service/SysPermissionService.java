package com.artlongs.sys.service;

import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.sys.dao.SysPermissionDao;
import com.artlongs.sys.model.SysPermission;

import javax.inject.Inject;
import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
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

    public boolean realDelByFuncId(Long funcId) {
        return sysPermissionDao.realDelByFuncId(funcId);
    }


}
