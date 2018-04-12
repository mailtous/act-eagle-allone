package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.model.BaseEntity;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.model.RoleAssignVo;
import com.artlongs.sys.model.SysRole;
import com.artlongs.sys.model.SysUser;
import com.artlongs.sys.service.SysRoleService;
import com.artlongs.sys.service.SysUserService;
import org.osgl.Osgl;
import org.osgl.http.H;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.Param;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import java.util.List;

/**
 * Function:后台--系统用户管理
 * @Autor: leeton
 * @Date : 11/22/17
 */
@UrlContext("/sys/user")
@TemplateContext("/sys/user")
public class SysUserController extends SysBaseController {

    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysRoleService sysRoleService;

    @GetAction("list")
    public RenderAny list(Integer pageNo) {
        Page<SysUser> page = new Page<>().setPageNumber(pageNo);
        page = sysUserService.getPage(page);
        return render("list.html",page);
    }

    @GetAction("list.json")
    public RenderJSON listJson(Integer pageNo) {
        Page<SysUser> page = new Page<>().setPageNumber(pageNo);
        page = sysUserService.getPage(page);
        return renderJson(page);
    }

    @GetAction("view/{id}")
    public RenderJSON view(@Param(defIntVal = 0) Integer id ) {
        SysUser sysUser = sysUserService.get(new Long(id));
        return json(sysUser);
    }

    @GetAction("edit_box/{id}")
    public RenderAny edit(@Param(defIntVal = 0) Integer id ,H.Request request) {
        SysUser currentUser = sysUserService.getCurrentLoginUser(ctx.session(), request);
        SysUser sysUser = new SysUser();
        if (id > 0) {
             sysUser = sysUserService.get(new Long(id));
        }
        SysUser.GradeStatus[] gradeStatusList = SysUser.GradeStatus.values();
        return render("edit_box.html",sysUser,gradeStatusList,currentUser);
    }

    @PostAction("edit/{id}")
    public RenderJSON editSave(SysUser sysUser) {
        R r = sysUserService.updateOf(sysUser);
        return json(r);
    }

    @PostAction("add")
    public RenderJSON add(SysUser sysUser) {
        R r = sysUserService.createNewUser(sysUser);
        return json(r);
    }

    @PostAction("del/{id}")
    public RenderJSON del(Long id) {
        SysUser sysUser = sysUserService.get(id);
        if (null != sysUser) {
            sysUser.setDelStatus(BaseEntity.DELETED);
            sysUserService.update(sysUser);
            return json(R.success("用户删除成功!"));
        }
        return json(R.fail("用户删除失败!"));
    }


    /**
     * 角色分配窗口页
     * @param id
     * @return
     */
    @GetAction("role_box/{userId}")
    public RenderAny role_edit(Long userId ) {
        SysUser sysUser = sysUserService.get(userId);
        List<SysRole> sysRoleList = sysRoleService.getAllOfList();

        ctx.renderArg("sysRoleList", sysRoleList);
        ctx.renderArg("sysUser", sysUser);
        ctx.renderArg("roleMap", sysUser.hasRoleMap());

        return render("role_box.html");
    }


    /**
     * 保存---分配角色
     * @param funcId
     * @param roleAssignVoList
     * @return
     */
    @PostAction("assign/role/{userId}")
    public RenderJSON assignPerm(Long userId, List<RoleAssignVo> roleAssignVoList) {
        SysUser sysUser = sysUserService.get(userId);
        if (null == sysUser) return json(R.fail("用户不存在。"));
        R r = sysUserService.assignRole(roleAssignVoList, sysUser);
        return json(r);
    }


}
