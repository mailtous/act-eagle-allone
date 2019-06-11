package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.utils.Qe;
import com.artlongs.sys.model.SysRole;

import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysRoleDao extends BeetlSqlDao<SysRole> {

    public Page<SysRole> getAllOfPage(Page page){
     /*   String sql = new Qe(SysRole.class).build();
        return getPage(page, sql, null);*/
        return this.lq().toPage(page);
    }

    public List<SysRole> getAllOfList() {
        String sql = new Qe(SysRole.class).build();
        return getList(sql);
    }

    public boolean realDel(Long funcId) {
        int num = delete(funcId);
        return num > 0;
    }


}
