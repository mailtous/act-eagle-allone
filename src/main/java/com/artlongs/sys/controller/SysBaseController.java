package com.artlongs.sys.controller;

import com.artlongs.framework.controller.BaseController;
import com.artlongs.sys.model.SysUser;
import com.artlongs.sys.service.SysMenuService;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.annotation.Before;
import org.osgl.util.C;
import org.osgl.util.S;

import javax.inject.Inject;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
public abstract class SysBaseController extends BaseController {
    Logger logger = L.get(getClass());

    @Inject
    private SysMenuService sysMenuService;

    /**
     * 后台用户是否已经登录的检查
     * @param session
     * @param req
     */
    @Before(except = "/sys/login")
    public void checkLogin(H.Session session,H.Request req) {
        //1.登录检查
        SysUser sysUser = userIsLogin(session, req);
        //2. 权限检查
        if(!sysMenuService.canAccessUrl(sysUser, req)){
            String errMsg = String.format("您没有访问：URL:(%s)的权限.",req.url());
            logger.error(errMsg);
            forbidden(errMsg);
        }

    }

    /**
     * 登录检查
     * @param session
     * @param req
     */
    private SysUser userIsLogin(H.Session session,H.Request req) {

        H.Cookie cookie = SysUser.getMyCookie(req);
        if (null == cookie) {
            to("/sys/login");
        }
        SysUser sysUser = session.cached(cookie.value());
        if (null == sysUser) {
            to("/sys/login");
        }
        return sysUser;
    }




}
