package com.artlongs.sys.dao;

import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.page.Page;
import com.artlongs.sys.model.SysRole;

import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysRoleDao extends BeetlSqlDao<SysRole>{

    public Page<SysRole> getAllOfPage(Page page){
        String sql = " select * from sys_role";
        return getPage(page, sql, null);
    }

    public List<SysRole> getAllOfList() {
        String sql = " select * from sys_role";
        return getList(sql);
    }

    public boolean realDel(Long funcId) {
        int num = delete(funcId);
        return num > 0;
    }


}
