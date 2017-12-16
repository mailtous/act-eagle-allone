package com.artlongs.sys.service;

import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.dao.SysUserDao;
import com.artlongs.sys.model.SysPermission;
import com.artlongs.sys.model.SysUser;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysUserService extends BaseServiceImpl<SysUser> {

    public SysUserService(){}

    private SysUserDao sysUserDao;

    @Inject
    public SysUserService(SysUserDao sysUserDao) {
        this.baseDao = sysUserDao;
        this.sysUserDao = sysUserDao;
    }

    @Inject
    private SysPermissionService sysPermissionService;
    @Inject
    private SysFuncService sysFuncService;

    public BizRetVo checkLogin(String userName, String pwd) {
        boolean pass = sysUserDao.checkLogin(userName, pwd);
        return pass ? new BizRetVo().setSuccess("登录成功。") : new BizRetVo().setError("用户名或密码错误!");
    }

    /**
     * 取得权限列表
     * @param roleId
     * @return
     */
    public List<SysPermission> getPermissionList(Integer roleId){
      return sysPermissionService.getPermissionList(roleId);
    }

    public List<Integer> getMyRoleList(Integer sysUserId){
        List<Integer> roleIdList = C.newList();
        SysUser sysUser = sysUserDao.get(new Long(sysUserId));
        if (null != sysUser) {
            roleIdList = sysUser.getRoleIdList();
        }
        return roleIdList;
    }




}
