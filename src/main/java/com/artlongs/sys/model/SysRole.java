package com.artlongs.sys.model;

import com.artlongs.framework.model.BaseEntity;

import javax.persistence.Entity;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysRole extends BaseEntity {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
