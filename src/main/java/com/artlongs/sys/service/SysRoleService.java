package com.artlongs.sys.service;

import com.artlongs.framework.page.Page;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.sys.dao.SysFuncDao;
import com.artlongs.sys.dao.SysRoleDao;
import com.artlongs.sys.model.SysRole;

import javax.inject.Inject;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
public class SysRoleService extends BaseServiceImpl<SysRole> {

    private SysRoleDao sysRoleDao;
    @Inject
    public SysRoleService(SysRoleDao sysRoleDao) {
        this.baseDao = sysRoleDao;
        this.sysRoleDao = sysRoleDao;
    }



    public Page<SysRole> getAllOfPage(Page<SysRole> page){
        return sysRoleDao.getAllOfPage(page);
    }

    public boolean realDel(Long id) {
        return sysRoleDao.realDel(id);
    }
}
