package com.artlongs.sys.model;


import com.alibaba.fastjson.JSON;
import com.artlongs.framework.model.BaseEntity;
import org.osgl.util.C;
import org.osgl.util.S;

import javax.persistence.Entity;
import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
@Entity
public class SysUser extends BaseEntity {

    private String userName;
    private String pwd;
    private String roleIds;
    private Long deptId;
    private Integer delStatus;

    public List<Integer> getRoleIdList() {
        List<Integer> roleIdList = C.newList();
        if (S.noBlank(roleIds)) {
            roleIdList = JSON.parseObject(roleIds, List.class);
        }
        return roleIdList;
    }

    //===============

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

}
