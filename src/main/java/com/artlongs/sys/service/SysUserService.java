package com.artlongs.sys.service;

import act.util.ActContext;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.dao.SysUserDao;
import com.artlongs.sys.model.SysPermission;
import com.artlongs.sys.model.SysUser;
import org.osgl.http.H;
import org.osgl.http.Http;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.Date;
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
    ActContext actContext;

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
        BizRetVo vo = new BizRetVo();
        SysUser sysUser = sysUserDao.checkLogin(userName, pwd);
        vo.setItem(sysUser);
        return null != sysUser ? vo.setSuccess("登录成功。") : vo.setError("用户名或密码错误!");
    }



    /**
     * 创建新用户,密码加密过
     * @param sysUser
     * @return
     */
    public boolean createNewUser(SysUser sysUser) {
        sysUser.setPwdByEncode(sysUser.getPwd());
        sysUser.setCreateDate(new Date());
        sysUser.setModifyDate(new Date());
        int r = sysUserDao.save(sysUser);
        return r > 0;
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
