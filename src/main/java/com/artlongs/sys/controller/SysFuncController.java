package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.model.RoleAssignVo;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.model.SysRole;
import com.artlongs.sys.service.SysFuncService;
import com.artlongs.sys.service.SysMenuService;
import com.artlongs.sys.service.SysPermissionService;
import com.artlongs.sys.service.SysRoleService;
import org.apache.commons.lang3.math.NumberUtils;
import org.osgl.Osgl;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.Param;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;
import org.osgl.util.N;

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
    @Inject
    private SysRoleService sysRoleService;
    @Inject
    private SysPermissionService sysPermissionService;
    @Inject
    private SysMenuService sysMenuService;

    @GetAction("list")
    public RenderAny list() {
        Map<Long, List<SysFunc>> funcTreeMap = sysFuncService.getModuleTreeMap();
        List<SysFunc> funcList = funcTreeMap.get(0L); //顶层菜单
        ctx.renderArg("funcList", funcList);
        return render("list.html");
    }

    @GetAction("childs/{parentId}")
    public RenderAny childs(@Param(defLongVal = 0) Long parentId) {
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

    /**
     * 编辑功能信息
     * @param sysFunc
     * @return
     */
    @PostAction("edit/{id}")
    public RenderJSON editSave(SysFunc sysFunc) {
        if (null != sysFunc.getId() && null != sysFuncService.get(sysFunc.getId())) {
            sysFunc.setFuncUrl(sysFunc.getFuncUrl().trim());
            sysFunc.setFuncName(sysFunc.getFuncName().trim());
            if(sysFuncService.saveOrUpdate(sysFunc)>0){
                sysFuncService.clearMap();
                return json(new R<>().setSuccess("系统功能编辑成功!"));
            }
        }

        return json(new R<>().setFail("系统功能编辑失败!"));
    }

    @PostAction("add")
    public RenderJSON add(SysFunc sysFunc) {
        if(sysFuncService.saveOrUpdate(sysFunc)>0){
            sysFuncService.clearMap();
        }
        return json(new R<>().setSuccess("系统功能添加成功!"));
    }


    @PostAction("del/{funcId}")
    public RenderJSON del(Long funcId) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFuncService.realDelFuncAndPermisson(funcId);
        }
        return json(new R<>().setSuccess("系统功能项删除成功!"));
    }

    @PostAction("on/{funcId}")
    public RenderJSON on(Long funcId) {
        return json(onOff(funcId,SysFunc.ON));
    }

    @PostAction("off/{funcId}")
    public RenderJSON off(Long funcId) {
        return json(onOff(funcId,SysFunc.OFF));
    }

    @PostAction("ismenu/{funcId}")
    public RenderJSON isMenu(Long funcId,int onOff) {
        return json(setOfMenu(funcId,onOff));
    }


    private R onOff(Long funcId, int onOff) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFunc.setAction(onOff);
            sysFuncService.update(sysFunc);
            clearMenu();
            return new R<>().setSuccess("已设置为："+(onOff==1?"开启":"关闭"));
        }
        return new R<>().setFail("找不到对应的功能或菜单!");
    }

    private R setOfMenu(Long funcId, int onOff) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFunc.setIsMenu(onOff);
            sysFuncService.update(sysFunc);
            clearMenu();
            return new R<>().setSuccess("已设置为："+(onOff==1?"菜单":"功能"));
        }
        return new R<>().setFail("找不到对应的功能或菜单!");
    }

    /**
     * 权限分配窗口页
     * @param id
     * @return
     */
    @GetAction("perm_box/{funcId}")
    public RenderAny perm_edit(Long funcId ) {
        SysFunc sysFunc = sysFuncService.get(new Long(funcId));
        List<SysRole> sysRoleList = sysRoleService.getAllOfList();
        List<Long> roleIds = sysPermissionService.getRoleIdsOfFuncId(funcId);
        Map<Long, Boolean> roleMap = sysPermissionService.roleMapOfpermission(roleIds);

        ctx.renderArg("sysRoleList", sysRoleList);
        ctx.renderArg("sysFunc", sysFunc);
        ctx.renderArg("roleMap", roleMap);
        return render("perm_box.html");
    }

    /**
     * 保存--权限分配
     * @param funcId
     * @param roleAssignVoList
     * @return
     */
    @PostAction("assign/perm/{funcId}")
    public RenderJSON assignPerm(Long funcId, List<RoleAssignVo> roleAssignVoList) {
        SysFunc sysFunc = sysFuncService.get(new Long(funcId));
        if(null == sysFunc) return json(R.fail("功能或菜单不存在。"));

        R r = sysPermissionService.savePermissionOfAssign(roleAssignVoList);

        return json(r);
    }

    /**
     * 清理菜单相关缓存
     */
    private void clearMenu(){
        sysMenuService.clear();
        sysFuncService.clearMap();
    }

}
