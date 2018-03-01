package com.artlongs.sys.model;

import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysPermission extends BaseEntity {

    private Long roleId;
    private Long funcId;

    public static abstract class Dao<SysPermission> extends BeetlSqlDao<SysPermission> {
        public static String table = "sys_permission";
        public static String id = "id";
        public static String createDate="create_date";
        public static String modifyDate="modify_date";
        public static String roleId="role_id";
        public static String funcId="func_id";

    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getFuncId() {
        return funcId;
    }

    public void setFuncId(Long funcId) {
        this.funcId = funcId;
    }
}
