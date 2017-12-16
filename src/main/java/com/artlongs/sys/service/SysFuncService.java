package com.artlongs.sys.service;

import com.artlongs.framework.page.Page;
import com.artlongs.framework.service.BaseServiceImpl;
import com.artlongs.sys.dao.SysFuncDao;
import com.artlongs.sys.model.SysFunc;

import javax.inject.Inject;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/29/17
 */
public class SysFuncService extends BaseServiceImpl<SysFunc> {


    private SysFuncDao sysFuncDao;

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

    public boolean realDelFuncAndPermisson(Long funcId) {
       if(sysFuncDao.realDelFunc(funcId)){
           return sysPermissionService.realDelByFuncId(funcId);
        }
        return false;
    }

}
