package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.model.BaseEntity;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.vo.RetVo;
import com.artlongs.sys.model.SysUser;
import com.artlongs.sys.service.SysUserService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.Param;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;

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

    @GetAction("list")
    public RenderAny list(Integer pageNo) {
        Page<SysUser> page = new Page<>().setPageNo(pageNo);
        page = sysUserService.getPage(page);
        return render("list.html",page);
    }

    @GetAction("list.json")
    public RenderJSON listJson(Integer pageNo) {
        Page<SysUser> page = new Page<>().setPageNo(pageNo);
        page = sysUserService.getPage(page);
        return renderJson(page);
    }

    @GetAction("view/{id}")
    public RenderJSON view(@Param(defIntVal = 0) Integer id ) {
        SysUser sysUser = sysUserService.get(new Long(id));
        return json(sysUser);
    }

    @GetAction("edit_box/{id}")
    public RenderAny edit(@Param(defIntVal = 0) Integer id ) {
        SysUser sysUser = new SysUser();
        if (id > 0) {
             sysUser = sysUserService.get(new Long(id));
        }
        return render("edit_box.html",sysUser);
    }

    @PostAction("edit/{id}")
    public RenderJSON editSave(SysUser sysUser) {
        sysUserService.update(sysUser);
        return json(RetVo.success("用户资料编辑成功!"));
    }

    @PostAction("add")
    public RenderJSON add(SysUser sysUser) {
        RetVo b = sysUserService.createNewUser(sysUser);
        return json(b);
    }

    @PostAction("del/{id}")
    public RenderJSON del(Long id) {
        SysUser sysUser = sysUserService.get(id);
        if (null != sysUser) {
            sysUser.setDelStatus(BaseEntity.DELETED);
            sysUserService.update(sysUser);
            return json(RetVo.success("用户删除成功!"));
        }
        return json(RetVo.error("用户删除失败!"));
    }


}
