package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.controller.BaseController;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.model.SysUser;
import com.artlongs.sys.service.SysMenuService;
import org.osgl.http.H;
import org.osgl.inject.annotation.Configuration;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;

/**
 * Function:后台首页
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
@UrlContext("/sys")
@TemplateContext("/sys")
public class SysHomeController extends SysBaseController {

    @Inject
    private SysMenuService sysMenuService;

    @GetAction({"home",""})
    public RenderAny home(H.Session session,H.Request req) {
        SysUser currentUser = SysUser.getCurrentLoginUser(session, req);
        ctx.renderArg("currentUser", currentUser);
        ctx.renderArg("menuDataUrl", "/sys/home/my/menu.json?userId="+currentUser.getId());

        return render("home.html");
    }

    @GetAction("home/menu")
    public RenderAny menu() {
        String menuDataUrl = "/sys/home/my/menu.json?userId=1";
        return render("menu.html", menuDataUrl);
    }

    @GetAction("home/mz")
    public RenderAny mz() {
        Integer userId = 1; //TODO GET USER SESSION
        String menuDataUrl = "/sys/home/my/menu.json?userId=" + userId;
        return render("home_maizu.html", menuDataUrl);
    }

    @GetAction("home/my/menu.json")
    public RenderJSON myMenu(Long userId) {
        SysFunc sysFunc = sysMenuService.getMyMenu(userId);

        return json(sysFunc);
    }

}