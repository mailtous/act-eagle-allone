package com.artlongs.sys.service;

import com.artlongs.framework.page.Page;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.sys.dao.SysFuncDao;
import com.artlongs.sys.model.SysFunc;
import org.osgl.util.C;
import org.osgl.util.S;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: 模块(功能)
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
public class SysFuncService extends BaseServiceImpl<SysFunc> {


    private SysFuncDao sysFuncDao;

    private static Map<Long, List<SysFunc>> moduleMap = new ConcurrentHashMap<>();

    @Inject
    public SysFuncService(SysFuncDao sysFuncDao) {
        this.baseDao = sysFuncDao;
        this.sysFuncDao = sysFuncDao;
    }

    @Inject
    private SysPermissionService sysPermissionService;

    public Page<SysFunc> getAllOfPage(Page page){
        return sysFuncDao.getAllOfPage(page);

    }

    public List<SysFunc> getAll(){
        String sql = " select * from sys_func ";
        return sysFuncDao.getList(sql);

    }

    public boolean realDelFuncAndPermisson(Long funcId) {
       if(sysFuncDao.realDelFunc(funcId)){
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
     * 将模块(功能)列表转化为MAP
     *
     * @param sysFuncList
     * @return
     */
    public Map<Long, List<SysFunc>> parseListToTreeMap(List<SysFunc> sysFuncList) {
        Map<Long, List<SysFunc>> map = new HashMap<Long, List<SysFunc>>();
        if (C.notEmpty(sysFuncList)) {
            for (SysFunc func : sysFuncList) {
                List<SysFunc> list = map.containsKey(func.getParentId()) ? map.get(func.getParentId()) : new ArrayList<SysFunc>();
                list.add(func);
                map.put(func.getParentId(), list);
            }
        }
        return map;
    }

}
