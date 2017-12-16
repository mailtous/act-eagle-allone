package com.artlongs.sys.service;

import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.sys.dao.SysRoleDao;
import com.artlongs.sys.model.SysRole;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
public class SysRoleService extends BaseServiceImpl<SysRole> {

    public SysRoleService(){}

    private SysRoleDao sysRoleDao;

    public SysRoleService(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
        this.baseDao = sysRoleDao;
    }
}
