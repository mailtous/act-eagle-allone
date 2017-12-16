package com.artlongs.sys.model;

import com.artlongs.framework.model.BaseEntity;

import javax.persistence.Entity;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Entity
public class SysDept extends BaseEntity{

    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
