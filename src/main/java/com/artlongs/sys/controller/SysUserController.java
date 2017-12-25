package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.data.annotation.Data;
import act.db.DB;
import act.db.DbBind;
import act.view.RenderAny;
import com.artlongs.framework.model.BaseEntity;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.model.SysUser;
import com.artlongs.sys.service.SysUserService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.Param;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import java.util.Date;

/**
 * Function:
 *
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
        String sql = " select * from sys_user";
        page = sysUserService.getPage(page, sql, null);
        return render("list.html",page);
    }

    @GetAction("list.json")
    public RenderJSON listJson(Integer pageNo) {
        Page<SysUser> page = new Page<>().setPageNo(pageNo);
        String sql = " select * from sys_user";
        page = sysUserService.getPage(page, sql, null);
        return renderJson(page);
    }



    @GetAction("edit/{id}")
    public RenderAny edit(@Param(defIntVal = 0) Integer id ) {
        SysUser sysUser = new SysUser();
        if (id > 0) {
             sysUser = sysUserService.get(new Long(id));
        }
        return render("edit.html",sysUser);
    }

    @PostAction("edit/{id}")
    public RenderJSON editBy(SysUser sysUser) {
        sysUserService.update(sysUser);
        return json(new BizRetVo<>().setSuccess("用户资料编辑成功!"));
    }

    @PostAction("add")
    public RenderJSON add(SysUser sysUser) {
        sysUser.setCreateDate(new Date());
        sysUserService.save(sysUser);
        return json(new BizRetVo<>().setSuccess("系统用户添加成功!"));
    }

    @PostAction("del/{id}")
    public RenderJSON del(Long sysUserId) {
        SysUser sysUser = sysUserService.get(sysUserId);
        if (null != sysUser) {
            sysUser.setDelStatus(BaseEntity.DELETED);
        }
        return json(new BizRetVo<>().setSuccess("用户删除成功!"));
    }


}
