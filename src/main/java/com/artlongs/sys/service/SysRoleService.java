package com.artlongs.sys.service;

import com.artlongs.framework.page.Page;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.dao.SysRoleDao;
import com.artlongs.sys.model.SysPermission;
import com.artlongs.sys.model.SysRole;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.List;

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

    @Inject
    private SysPermissionService permissionService;

    public Page<SysRole> getAllOfPage(Page<SysRole> page){
        return sysRoleDao.getAllOfPage(page);
    }

    public List<SysRole> getAllOfList(){
        return sysRoleDao.getAllOfList();
    }

    public R realDel(Long id) {

        SysRole sysRole = get(id);
        if (null == sysRole) {
           return R.fail("找不到对应的角色!");
        }

        List<SysPermission> permissionList = permissionService.getPermissionList(id);
        if (C.notEmpty(permissionList)) {
            return R.fail("角色已经分配了权限,不允许删除。");
        }

        return R.tf(sysRoleDao.realDel(id));
    }
}
