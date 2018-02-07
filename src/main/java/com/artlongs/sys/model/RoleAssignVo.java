package com.artlongs.sys.model;

/**
 * Function: 权限分配VO
 *
 * @Autor: leeton
 * @Date : 2/6/18
 */
public class RoleAssignVo {
    public static final Integer off = 0;
    public static final Integer on = 1;
    private Integer roleId;
    private Integer funcId;
    private Integer onoff;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getFuncId() {
        return funcId;
    }

    public void setFuncId(Integer funcId) {
        this.funcId = funcId;
    }

    public Integer getOnoff() {
        return onoff;
    }

    public void setOnoff(Integer onoff) {
        this.onoff = onoff;
    }
}
