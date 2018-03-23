package com.artlongs.sys.service;

import com.alibaba.fastjson.JSON;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.dao.SysUserDao;
import com.artlongs.sys.model.RoleAssignVo;
import com.artlongs.sys.model.SysPermission;
import com.artlongs.sys.model.SysUser;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private SysMenuService sysMenuService;

    public R checkLogin(String userName, String pwd) {
        R r = new R();
        SysUser sysUser = sysUserDao.checkLogin(userName, pwd);
        r.setItem(sysUser);
        return null != sysUser ? r.setSuccess("登录成功。") : r.setFail("用户名或密码错误!");
    }

    public Page<SysUser> getPage(Page page) {
        String sql = " select * from sys_user";
        page = sysUserDao.getPage(page, sql ,null);
        return page;
    }

    /**
     * 创建新用户,密码加密过
     * @param sysUser
     * @return
     */
    public R createNewUser(SysUser sysUser) {
        R vo = new R<>();
        SysUser dbUser = getByName(sysUser.getUserName());
        if (null == dbUser) {
            sysUser.setEncodePwd(sysUser.getPwd()); //对前端输入的明文密码,进行加密
            sysUser.setCreateDate(new Date());
            sysUser.setModifyDate(new Date());
            sysUser.setDelStatus(SysUser.UN_DEL);
            int r = sysUserDao.save(sysUser);
            vo = r > 0 ? vo.setSuccess("用户创建成功!") : vo.setFail("用户创建失败!");
        }else {
            vo.setFail("用户已经存在!");
        }
        return vo;
    }

    public SysUser getByName(String userName) {
        String sql = " select * from sys_user where user_name = ?";
        SysUser sysUser = sysUserDao.getObj(sql, new Object[]{userName});
        return sysUser;
    }

    public int update(SysUser sysUser) {
        sysUser.setModifyDate(new Date());
        int rows = sysUserDao.update(sysUser);
        return rows;
    }

    public int del(SysUser sysUser) {
        sysUser.setModifyDate(new Date());
        sysUser.setDelStatus(SysUser.DELETED);
        sysUser.setModifyDate(new Date());
        int rows = sysUserDao.update(sysUser);
        return rows;
    }

    /**
     * 取得权限列表
     * @param roleId
     * @return
     */
    public List<SysPermission> getPermissionList(Long roleId){
      return sysPermissionService.getPermissionList(roleId);
    }

    public List<Integer> getMyRoleList(Integer sysUserId){
        List<Integer> roleIdList = C.newList();
        SysUser sysUser = sysUserDao.get(new Long(sysUserId));
        if (null != sysUser) {
            roleIdList = sysUser.roleIdList();
        }
        return roleIdList;
    }


    public R assignRole(List<RoleAssignVo> roleAssignVoList,SysUser sysUser){
        if (C.notEmpty(roleAssignVoList)) {
            Set<Integer> roleIds = C.newSet(sysUser.roleIdList());
            for (RoleAssignVo roleAssignVo : roleAssignVoList) {
                if (RoleAssignVo.on == roleAssignVo.getOnoff()) {
                    roleIds.add(roleAssignVo.getRoleId());
                } else {
                    roleIds.remove(roleAssignVo.getRoleId());
                }

            }
            sysUser.setRoleIds(JSON.toJSONString(roleIds));
        }
        int rows = updateAndTime(sysUser);
        return rows>0?R.success("用户设置角色成功!"):R.fail("用户设置角色失败!");
    }



}
