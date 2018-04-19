package com.artlongs.sys.service;

import act.db.sql.tx.Transactional;
import act.util.Stateless;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.model.SysPermission;
import com.artlongs.sys.model.SysUser;
import org.osgl.http.H;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Function: 菜单创建的服务
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
@Stateless
@Transactional
public class SysMenuService {

    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysFuncService sysFuncService;
    @Inject
    private SysPermissionService sysPermissionService;

    /** 简单的缓存菜单及权限列表 **/
    public static Map<Long, List<SysPermission>> permissionMap = new ConcurrentHashMap<>();
    public static Map<Long, List<SysFunc>> funcMap = new ConcurrentHashMap<>();
    public static Map<Long, SysFunc> myMenu = new ConcurrentHashMap<>();

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
    public Map<Long, List<SysPermission>> getPermissionMap(List<Long> roleIds) {
        if (C.isEmpty(roleIds)) return permissionMap;
        for (Long roleId : roleIds) {
            if (null == permissionMap.get(roleId)) {
                List<SysPermission> permissionList = sysPermissionService.getPermissionList(new Long(roleId));
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
    private Map<Long, List<SysFunc>> getMyAllFuncMap(Long sysUserId) {
        if(C.isEmpty(funcMap.get(sysUserId))){
            List<SysFunc> funcList = C.newList();
            List<Long> roleIds = sysUserService.getMyRoleList(sysUserId);
            if (C.notEmpty(roleIds)) {
                permissionMap = getPermissionMap(roleIds);
                if (C.notEmpty(permissionMap)) {
                    for (List<SysPermission> permissionList : permissionMap.values()) {
                        for (SysPermission sysPermission : permissionList) {
                            SysFunc func = sysFuncService.get(sysPermission.getFuncId());
                            if (null != func) {
                                if(!isExistInFuncList(funcList,func)){
                                    funcList.add(func);
                                }
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

    private boolean isExistInFuncList(List<SysFunc> funcList, SysFunc func) {
        for (SysFunc sysFunc : funcList) {
            if(sysFunc.getId().equals(func.getId())) return true;
        }
        return false;
    }

    /**
     * 按 sysUserId 获取自己拥有的菜单
     * @param sysUserId
     * @return
     */
    public SysFunc getMyMenu(Long sysUserId){
        SysFunc menu = myMenu.get(sysUserId);
        if (null == menu) {
            menu = SysFunc.blankTopMenu();
            List<SysFunc> myActionFuncs = getMyActionMenu(sysUserId);
            List<SysFunc> myTopMenus = getTopMenu(myActionFuncs);
            menu.setChilds(myTopMenus);
            buildTreeFunc(menu, myActionFuncs);
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
    private void buildTreeFunc(SysFunc sysFunc, List<SysFunc> myActionFuncs){
        for (SysFunc func : sysFunc.getChilds()) {
            List<SysFunc> childs = getChildsByParentId(myActionFuncs, func.getId());
            if(C.isEmpty(childs)) break;
            func.setChilds(childs);
            buildTreeFunc(func, myActionFuncs);
        }
    }



    /**
     * 取得我可用的功能url
     *
     * @param roleIds
     * @return
     */
    public Set<String> getMyActionUrls(Long sysUserId) {
        List<SysFunc> funcList = C.newList();
        Set<String> urls = C.newSet();
        if(C.isEmpty(funcMap.get(sysUserId))){
            getMyAllFuncMap(sysUserId);
        }
        funcList = funcMap.get(sysUserId);
        if (C.notEmpty(funcList)) {
            urls = funcList.stream()
                    .filter(f -> (SysFunc.ON == f.getAction()) && (null != f.getFuncUrl()))
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
    public List<SysFunc> getMyActionMenu(Long sysUserId) {
        List<SysFunc> funcList = C.newList();
        if(C.isEmpty(funcMap.get(sysUserId))){
            getMyAllFuncMap(sysUserId);
        }
        funcList = funcMap.get(sysUserId);
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
    public List<SysFunc> getMyActionFunc(Long sysUserId) {
        List<SysFunc> funcSet = C.newList();
        if(C.isEmpty(funcMap.get(sysUserId))){
            getMyAllFuncMap(sysUserId);
        }
        funcSet = funcMap.get(sysUserId);
        if (C.notEmpty(funcSet)) {
            funcSet.removeIf(f -> (SysFunc.OFF==f.getAction()));
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

    /**
     * 检查用户是否有当前路径的访问权限
     * @param sysUser
     * @param req
     */
    public boolean canAccessUrl(SysUser sysUser, H.Request req) {
        if(null == sysUser) return false;
        //超级管理员拥有所有权限
        if(sysUser.isSuperAdmin())  return true;

        Set<String> actionUrlList = getMyActionUrls(sysUser.getId());
        if(C.isEmpty(actionUrlList)) return false;

        String currentUrl = req.url().trim();
        for (String url : actionUrlList) {
            String regx = url.trim().replace(".json", "");
            if (regx.endsWith("**")) {//匹配所有子级目录及文件
                regx = "^"+regx +"(.*?)";
            }
            if (regx.endsWith("*")) { //只匹配当前目录下的文件
                regx = "^"+regx +"(\\w)*";
            }
            Pattern p = Pattern.compile(regx);
            Matcher m = p.matcher(currentUrl);
            if(m.find()) return true;
        }

        return false;
    }
}
