package com.artlongs.sys.controller;

import act.view.RenderAny;
import com.artlongs.framework.controller.BaseController;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.model.SysUser;
import org.osgl.http.H;
import org.osgl.mvc.annotation.Before;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
public abstract class SysBaseController extends BaseController {

    /**
     * 后台用户是否已经登录的检查
     * @param session
     * @param req
     */
    @Before(except = "/sys/login")
    public void checkLogin(H.Session session,H.Request req) {
        H.Cookie cookie = SysUser.getMyCookie(req);
        if (null == cookie) {
            to("/sys/login");
        }
        SysUser sysUser = session.cached(cookie.value());
        if (null == sysUser) {
            to("/sys/login");
        }
    }

}
