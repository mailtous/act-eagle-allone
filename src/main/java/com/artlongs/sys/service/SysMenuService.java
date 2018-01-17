package com.artlongs.sys.service;

import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.model.SysPermission;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Function: 菜单创建的服务
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
public class SysMenuService {

    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysFuncService sysFuncService;
    @Inject
    private SysPermissionService sysPermissionService;

    /** 简单的缓存菜单及权限列表 **/
    public static Map<Integer, List<SysPermission>> permissionMap = new ConcurrentHashMap<>();
    public static Map<Integer, List<SysFunc>> funcMap = new ConcurrentHashMap<>();
    public static Map<Integer, SysFunc> myMenu = new ConcurrentHashMap<>();

    public void clear(){
        try{
            permissionMap.clear();
            funcMap.clear();
            myMenu.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按角色ids取得我的所有权限
     * @param roleIds
     * @return
     */
    public Map<Integer, List<SysPermission>> getPermissionMap(List<Integer> roleIds) {
        if (C.isEmpty(roleIds)) return permissionMap;
        for (Integer roleId : roleIds) {
            if (null == permissionMap.get(roleId)) {
                List<SysPermission> permissionList = sysPermissionService.getPermissionList(roleId);
                if (C.notEmpty(permissionList)) {
                    permissionMap.putIfAbsent(roleId, permissionList);
                }
            }
        }
        return permissionMap;
    }


    /**
     * 取得我的全部功能列表
     *
     * @param roleIds
     * @return
     */
    private Map<Integer, List<SysFunc>> getMyAllFuncMap(Integer sysUserId) {
        if(null == funcMap.get(sysUserId)){
            List<SysFunc> funcList = C.newList();
            List<Integer> roleIds = sysUserService.getMyRoleList(sysUserId);
            if (C.notEmpty(roleIds)) {
                permissionMap = getPermissionMap(roleIds);
                if (C.notEmpty(permissionMap)) {
                    for (List<SysPermission> permissionList : permissionMap.values()) {
                        for (SysPermission sysPermission : permissionList) {
                            SysFunc func = sysFuncService.get(sysPermission.getFuncId());
                            if (null != func) {
                                funcList.add(func);
                            }
                        }
                    }
                }
                // 按sequence从小到大排序
                Collections.sort(funcList, Comparator.comparing(SysFunc::getSequence));
                //add
                funcMap.put(sysUserId, funcList);
            }
        }

        return funcMap;
    }

    /**
     * 按 sysUserId 获取自己拥有的菜单
     * @param sysUserId
     * @return
     */
    public SysFunc getMyMenu(Integer sysUserId){
        SysFunc menu = myMenu.get(sysUserId);
        if (null == menu) {
            menu = SysFunc.blankTopMenu();
            List<SysFunc> myActionFuncs = getMyActionFunc(sysUserId);
            List<SysFunc> myTopMenus = getTopMenu(myActionFuncs);
            menu.setChilds(myTopMenus);
            getChildMenu(menu, myActionFuncs);
            myMenu.putIfAbsent(sysUserId, menu);
        }
        return menu;
    }

    /**
     * 构建树型子菜单
     * @param sysFunc
     * @param myActionFuncs
     * @return
     */
    private SysFunc getChildMenu(SysFunc sysFunc,List<SysFunc> myActionFuncs){
        for (SysFunc func : sysFunc.getChilds()) {
            List<SysFunc> childs = getChildsByParentId(myActionFuncs, func.getId());
            if(C.isEmpty(childs)) break;
            func.setChilds(childs);
            getChildMenu(func, myActionFuncs);
        }
        return sysFunc;
    }



    /**
     * 取得我可用的功能url
     *
     * @param roleIds
     * @return
     */
    public Set<String> getMyActionUrls(Integer sysUserID) {
        List<SysFunc> funcList = C.newList();
        Set<String> urls = C.newSet();
        getMyAllFuncMap(sysUserID);
        funcList = funcMap.get(sysUserID);
        if (C.notEmpty(funcList)) {
            urls = funcList.stream()
                    .filter(f -> (SysFunc.ON == f.getAction()))
                    .map(SysFunc::getFuncUrl)
                    .collect(Collectors.toSet());
        }
        return urls;
    }

    /**
     * 取得我可用的菜单(不包括普通的功能url)
     *
     * @param roleIds
     * @return
     */
    public List<SysFunc> getMyActionMenu(Integer sysUserID) {
        List<SysFunc> funcList = C.newList();
        getMyAllFuncMap(sysUserID);
        funcList = funcMap.get(sysUserID);
        if (C.notEmpty(funcList)) {
            funcList.removeIf(f -> (0 == f.getIsMenu() || SysFunc.OFF == f.getAction()));
        }
        return funcList;
    }

    /**
     * 取得我可用的功能列表(包含菜单)
     *
     * @param roleIds
     * @return
     */
    public List<SysFunc> getMyActionFunc(Integer sysUserID) {
        List<SysFunc> funcSet = C.newList();
        getMyAllFuncMap(sysUserID);
        funcSet = funcMap.get(sysUserID);
        if (C.notEmpty(funcSet)) {
            funcSet.removeIf(f -> (SysFunc.OFF==f.getAction().intValue()));
        }else {
            return C.newList();
        }
        return funcSet;
    }


    /**
     * 取得当前层次的菜单
     *
     * @param menuList 所有菜单
     * @param parentId 父节点ID
     * @param node     层次
     * @return
     */
    public List<SysFunc> getChildsByParentId(List<SysFunc> menuList, Long parentId) {
        List<SysFunc> sysFuncList = new ArrayList<>();
        for (SysFunc sysFunc : menuList) {
            if (parentId.equals(sysFunc.getParentId())) {
                sysFuncList.add(sysFunc);
            }
        }
        return sysFuncList;
    }

    /**
     * 只取顶层菜单
     *
     * @param menuList
     * @return
     */
    public List<SysFunc> getTopMenu(List<SysFunc> menuList) {
        List<SysFunc> funcList = C.newList();
        for (SysFunc sysFunc : menuList) {
            if (SysFunc.TOP_NODE==sysFunc.getNode()) {
                funcList.add(sysFunc);
            }
        }
        return funcList;
    }
}
