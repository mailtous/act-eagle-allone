package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.page.Page;
import com.artlongs.sys.model.SysRole;
import org.osgl.inject.annotation.Provided;

import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysRoleDao extends SysRole.Dao<SysRole>{

    public Page<SysRole> getAllOfPage(Page page){
        String sql = " select * from " + SysRole.Dao.table;
        return getPage(page, sql, null);
    }

    public List<SysRole> getAllOfList() {
        String sql = " select * from " + SysRole.Dao.table;
        return getList(sql);
    }

    public boolean realDel(Long funcId) {
        int num = delete(funcId);
        return num > 0;
    }


}
