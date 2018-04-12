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
    private Long roleId;
    private Long funcId;
    private Integer onoff;

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

    public Integer getOnoff() {
        return onoff;
    }

    public void setOnoff(Integer onoff) {
        this.onoff = onoff;
    }
}
