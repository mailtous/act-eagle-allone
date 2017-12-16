package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.service.SysUserService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/20/17
 */
@UrlContext("/sys/login")
@TemplateContext("/sys/login")
public class SysLoginController extends SysBaseController {

    @Inject
    private SysUserService sysUserService;

    @GetAction("")
    public RenderAny login() {
        return render("login.html");
    }

    @PostAction("post")
    public RenderJSON loginPost(String userName ,String pwd) {
        BizRetVo bizRetVo = sysUserService.checkLogin(userName, pwd);
        return json(bizRetVo);
    }

}
