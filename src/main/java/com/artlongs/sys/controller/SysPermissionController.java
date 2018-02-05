package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.service.SysFuncService;
import com.artlongs.sys.service.SysPermissionService;
import org.osgl.mvc.annotation.GetAction;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 2/1/18
 */
@UrlContext("/sys/perm")
@TemplateContext("/sys/perm")
public class SysPermissionController extends SysBaseController {

    @Inject
    private SysPermissionService sysPermissionService;

    @GetAction("list")
    public RenderAny list() {

        return render("list.html");
    }

}
