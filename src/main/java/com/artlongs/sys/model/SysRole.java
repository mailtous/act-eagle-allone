package com.artlongs.sys.model;

import act.inject.AutoBind;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;

import javax.inject.Singleton;
import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysRole extends BaseEntity {
    private String roleName;

    @Singleton
    @AutoBind
    public static abstract class Dao<T extends SysRole> extends BeetlSqlDao<SysRole>{
        public abstract List<SysRole> getAllOfList();
    }

    // ============================ girl && beast ============================

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
