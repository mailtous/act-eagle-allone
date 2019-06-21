package com.artlongs.sys.dao;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.page.Page;
import com.artlongs.sys.model.SysFunc;

import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Stateless
public class SysFuncDao extends BeetlSqlDao<SysFunc> {


    public Page<SysFunc> getAllOfPage(Page page){
        String sql = this.lq().build();
        return getPage(page, sql, null);
    }

    public List<SysFunc> getAll(){
        String sql = this.lq().build();
        return getList(sql);
    }

    public boolean realDel(Long funcId) {
        int num = delete(funcId);
        return num > 0;
    }

    public SysFunc getParent(Long parentId){
        String sql = this.lq().andEq(SysFunc::getParentId,parentId).build();
        return getObj(sql, parentId);
    }
}
