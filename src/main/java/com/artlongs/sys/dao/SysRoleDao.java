package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.utils.QE;
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
        String sql = QE.selectAll().from(table).sql();
        return getPage(page, sql, null);
    }

    public List<SysRole> getAllOfList() {
        String sql = QE.selectAll().from(table).sql();
        return getList(sql);
    }

    public boolean realDel(Long funcId) {
        int num = delete(funcId);
        return num > 0;
    }


}
