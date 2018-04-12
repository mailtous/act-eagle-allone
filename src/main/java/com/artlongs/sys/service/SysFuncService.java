package com.artlongs.sys.service;

import act.data.MapUtil;
import act.util.Stateless;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.sys.dao.SysFuncDao;
import com.artlongs.sys.model.SysFunc;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import org.osgl.util.C;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Function: 模块(功能)
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
@Stateless
public class SysFuncService extends BaseServiceImpl<SysFunc> {

    private SysFuncDao sysFuncDao;
    @Inject
    public SysFuncService(SysFuncDao sysFuncDao) {
        this.baseDao = sysFuncDao;
        this.sysFuncDao = sysFuncDao;
    }

    @Inject
    private SysPermissionService sysPermissionService;

    public int saveOrUpdate(SysFunc func) {
        func.setParentId(null == func.getParentId()?SysFunc.TOP_NODE:func.getParentId());
        func.setNode(null == func.getNode()?SysFunc.TOP_NODE:func.getNode());
        func.setFuncUrl(func.getFuncUrl().trim());
        func.setFuncName(func.getFuncName().trim());
        if(null == func.getId() || func.getId() <=0) {
            return add(func);
        }else {
            return updateAndTime(func);
        }
    }


    /**
     * MAP<父亲节点ID,子节点列表>
     */
    private static Map<Long, List<SysFunc>> moduleMap = new LinkedHashMap<>();


    public Page<SysFunc> getAllOfPage(Page page){
        return sysFuncDao.getAllOfPage(page);

    }

    public List<SysFunc> getAll(){
        return sysFuncDao.getAll();
    }

    public boolean realDelFuncAndPermisson(Long funcId) {
       if(sysFuncDao.realDel(funcId)){
           return sysPermissionService.realDelByFuncId(funcId);
        }
        return false;
    }

    public Map<Long, List<SysFunc>> getModuleTreeMap(){
        if (C.isEmpty(moduleMap)) {
            moduleMap = parseListToTreeMap(this.getAll());
        }
        return moduleMap;
    }

    /**
     * 标记是否有子节点
     */
    private void markHasChild(Map<Long, List<SysFunc>> moduleMap){
        List<SysFunc> allFuncs = getAllFuncOfMap(moduleMap);
        if (C.notEmpty(allFuncs)) {
            for (SysFunc func : allFuncs) {
                for (Long key : moduleMap.keySet()) {
                    if (key.equals(func.getId())) {
                        func.hasChilds = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 构造所有菜单的子菜单
     * @param moduleMap
     * @return
     */
    public SysFunc buildFuncTree(Map<Long, List<SysFunc>> moduleMap){
        SysFunc topMenu = SysFunc.blankTopMenu();
        List<SysFunc> allFuncList = getAllFuncOfMap(moduleMap);
        if (C.notEmpty(allFuncList)) {
            for (SysFunc sysFunc : allFuncList) { //构建每层子菜单
                List<SysFunc> childs = moduleMap.get(sysFunc.getId());
                if (C.notEmpty(childs)) {
                    sysFunc.setChilds(childs);
                    sysFunc.hasChilds = true;
                }
            }
            //设置顶层的子菜单
            List<SysFunc> topMenuChilds = moduleMap.get(topMenu.getId());
            topMenu.setChilds(topMenuChilds);
        }
        return topMenu;
    }



    public List<SysFunc> getAllFuncOfMap(Map<Long, List<SysFunc>> moduleMap) {
        List<SysFunc> allFuncs = Lists.newArrayList();
        if (C.notEmpty(moduleMap)) {
            for (List<SysFunc> funcs : moduleMap.values()) {
                for (SysFunc func : funcs) {
                    allFuncs.add(func);
                }
            }
        }
        return allFuncs;
    }

    /**
     * 将模块(功能)列表转化为MAP
     *
     * @param sysFuncList
     * @return
     */
    public Map<Long, List<SysFunc>> parseListToTreeMap(List<SysFunc> sysFuncList) {
        Map<Long, List<SysFunc>> map = new HashMap<Long, List<SysFunc>>();
        if (C.notEmpty(sysFuncList)) {
            for (SysFunc func : sysFuncList) {
                Long parentId = null == func.getParentId()?0:func.getParentId();
                List<SysFunc> list = map.containsKey(parentId) ? map.get(parentId) : new ArrayList<SysFunc>();
                list.add(func);
                map.put(parentId, list);
            }
        }
        markHasChild(map); //标记父节点
        sortChilds(map);//排序
        return map;
    }

    public SysFunc getNodeByTreeMap(Long id){
        if (C.notEmpty(moduleMap)) {
            for (List<SysFunc> funcList : moduleMap.values()) {
                for (SysFunc sysFunc : funcList) {
                    if (id.equals(sysFunc.getId())) {
                        return sysFunc;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 按当前节点找出所有父节点,保存到stack
     * @param parentId
     * @return
     */
    public Stack<SysFunc> getParentStackOfNodeId(Long nodeId) {
        Stack<SysFunc> funcStacks = new Stack<>();
        while (nodeId.intValue() >0) {
            SysFunc node = getNodeByTreeMap(nodeId);
            if (node != null) {
                funcStacks.push(node);
                nodeId = node.getParentId();
            }else {
                break;
            }
        }
        return funcStacks;
    }

    /**
     * 把各级菜单按指定的序号排序
     * @param map
     */
    private void sortChilds(Map<Long, List<SysFunc>> map) {
        for (Long key : map.keySet()) {
            List<SysFunc> funcs = map.get(key);
            funcs = funcs.stream().sorted(Comparator.comparing(SysFunc::getSequence)).collect(Collectors.toList());
            map.put(key, funcs);
        }

    }


   public void clearMap(){
       moduleMap = new LinkedHashMap<>();
   }


}
