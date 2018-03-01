package com.artlongs.sys.model;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;
import org.osgl.inject.annotation.Provided;

import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysRole extends BaseEntity {
    private String roleName;

    @Stateless
    @act.inject.AutoBind
    public static abstract class Dao<SysRole> extends BeetlSqlDao<SysRole>{
        public static String table = "sys_role";
        public static String id = "id";
        public static String createDate="create_date";
        public static String modifyDate="modify_date";
        public static String roleName = "role_name";

        public abstract List<SysRole> getAllOfList();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
