package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.data.annotation.Data;
import act.view.RenderAny;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.service.SysFuncService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.Param;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
        Stack<SysFunc> parentStack = sysFuncService.getParentStackOfNodeId(parentId);//父菜单导航条
        ctx.renderArg("funcList", funcList);
        ctx.renderArg("parentStack", parentStack);
        return render("list.html");
    }

    @GetAction("tree")
    public String tree() {
        Map<Long, List<SysFunc>> moduleMap = sysFuncService.getModuleTreeMap();
        SysFunc topMenu = sysFuncService.buildFuncTree(moduleMap);
        String jsonStr = topMenu.childsToJqTreeStr();
        return jsonStr;
    }


    @GetAction("edit_box/{id}")
    public RenderAny edit(@Param(defLongVal = -1) Long id ) {
        SysFunc sysFunc = new SysFunc();
        sysFunc.setId(id); // -1:新增
        if (id > 0) {
            sysFunc = sysFuncService.get(new Long(id));
        }
        //树型菜单的数据
        Map<Long, List<SysFunc>> moduleMap = sysFuncService.getModuleTreeMap();
        SysFunc topMenu = sysFuncService.buildFuncTree(moduleMap);
        String treeJsonStr = topMenu.childsToJqTreeStr();
        ctx.renderArg("treeJsonStr", treeJsonStr);
        ctx.renderArg("sysFunc", sysFunc);

        return render("edit_box.html");
    }

    @PostAction("edit/{id}")
    public RenderJSON editSave(SysFunc sysFunc) {
        if(sysFuncService.updateAndTime(sysFunc)>0){
            sysFuncService.clearMap();
        }
        return json(new BizRetVo<>().setSuccess("系统功能编辑成功!"));
    }

    @PostAction("add")
    public RenderJSON add(SysFunc sysFunc) {
        if(sysFuncService.add(sysFunc)>0){
            sysFuncService.clearMap();
        }
        return json(new BizRetVo<>().setSuccess("系统功能添加成功!"));
    }


    @PostAction("del/{funcId}")
    public RenderJSON del(Long funcId) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFuncService.realDelFuncAndPermisson(funcId);
        }
        return json(new BizRetVo<>().setSuccess("系统功能项删除成功!"));
    }

    @PostAction("on/{funcId}")
    public RenderJSON on(Long funcId) {
        return json(onOff(funcId,SysFunc.ON));
    }

    @PostAction("off/{funcId}")
    public RenderJSON off(Long funcId) {
        return json(onOff(funcId,SysFunc.OFF));
    }

    private BizRetVo onOff(Long funcId, int onOff) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFunc.setAction(onOff);
            sysFuncService.update(sysFunc);
            sysFuncService.clearMap();
            return new BizRetVo<>().setSuccess("功能项的状态已变更!");
        }
        return new BizRetVo<>().setError("找不到对应的功能或菜单!");
    }

}
