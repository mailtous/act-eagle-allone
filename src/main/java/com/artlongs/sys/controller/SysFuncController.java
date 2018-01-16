package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.service.SysFuncService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Function:功能模块列表
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
@UrlContext("/sys/func")
@TemplateContext("/sys/func")
public class SysFuncController extends SysBaseController {

    @Inject
    private SysFuncService sysFuncService;

    @GetAction("list")
    public RenderAny list() {
        Map<Long, List<SysFunc>> funcTreeMap = sysFuncService.getModuleTreeMap();
        List<SysFunc> funcList = funcTreeMap.get(0L); //顶层菜单
        ctx.renderArg("funcList", funcList);
        return render("list.html");
    }

    @GetAction("childs/{parentId}")
    public RenderAny childs(Long parentId) {
        Map<Long, List<SysFunc>> funcTreeMap = sysFuncService.getModuleTreeMap();
        List<SysFunc> funcList = funcTreeMap.get(parentId); //子菜单
        SysFunc parentFunc = sysFuncService.get(parentId);  //顶层菜单
        ctx.renderArg("funcList", funcList);
        ctx.renderArg("parentFunc", parentFunc);
        return render("list.html");
    }

    @PostAction("add")
    public RenderJSON add(SysFunc sysFunc) {
        sysFuncService.add(sysFunc);
        return json(new BizRetVo<>().setSuccess("系统功能添加成功!"));
    }

    @PostAction("edit/{id}")
    public RenderJSON edit(SysFunc sysFunc) {
        sysFuncService.update(sysFunc);
        return json(new BizRetVo<>().setSuccess("系统功能编辑成功!"));
    }

    @PostAction("del/{id}")
    public RenderJSON del(Long funcId) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFuncService.realDelFuncAndPermisson(funcId);
        }
        return json(new BizRetVo<>().setSuccess("系统功能项删除成功!"));
    }

    @PostAction("on/{id}")
    public RenderJSON on(Long funcId) {
        return json(onOff(funcId,SysFunc.ON));
    }

    @PostAction("off/{id}")
    public RenderJSON off(Long funcId) {
        return json(onOff(funcId,SysFunc.OFF));
    }

    private BizRetVo onOff(Long funcId, int onOff) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFunc.setAction(onOff);
            return new BizRetVo<>().setSuccess("功能项的状态已变更!");
        }
        return new BizRetVo<>().setError("找不到对应的功能或菜单!");
    }

}
