package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.vo.RetVo;
import com.artlongs.sys.model.SysRole;
import com.artlongs.sys.service.SysRoleService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.Param;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;

/**
 * Function:后台--角色管理
 *
 * @Autor: leeton
 * @Date : 2/1/18
 */
@UrlContext("/sys/role")
@TemplateContext("/sys/role")
public class SysRoleController extends SysBaseController {

    @Inject
    private SysRoleService sysRoleService;

    @GetAction("list")
    public RenderAny list(Page page) {
        page = sysRoleService.getAllOfPage(page);
        ctx.renderArg("page", page);
        return render("list.html");
    }

    @GetAction("edit_box/{id}")
    public RenderAny edit(@Param(defLongVal = -1) Long id ) {
        SysRole sysRole = new SysRole();
        sysRole.setId(id); // -1:新增
        if (id > 0) {
            sysRole = sysRoleService.get(new Long(id));
        }
        return render("edit_box.html",sysRole);
    }

    @PostAction("edit/{id}")
    public RenderJSON editSave(SysRole sysRole) {
        RetVo vo = RetVo.success("系统角色编辑成功!");
        if(sysRoleService.updateAndTime(sysRole)>0){
            vo.setError("系统角色编辑失败!");
        }
        return json(vo);
    }

    @PostAction("add")
    public RenderJSON add(SysRole sysRole) {
        sysRoleService.add(sysRole);
        return json(new RetVo<>().setSuccess("系统角色添加成功!"));
    }


    @PostAction("del/{id}")
    public RenderJSON del(Long id) {
        SysRole sysFunc = sysRoleService.get(id);
        if (null != sysFunc) {
            sysRoleService.realDel(id);
        }
        return json(new RetVo<>().setSuccess("系统角色删除成功!"));
    }
}
