package com.artlongs.sys.model;


import act.Act;
import com.alibaba.fastjson.JSON;
import com.artlongs.framework.model.BaseEntity;
import org.osgl.http.H;
import org.osgl.inject.annotation.Configuration;
import org.osgl.util.C;
import org.osgl.util.S;

import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysUser extends BaseEntity {

    @Configuration("sysuser.cookies.name")
    public static String cookies_name;

    private String userName;
    private String pwd;
    private String roleIds;
    private Long deptId;
    private Integer delStatus;


    public List<Integer> roleIdList() {
        List<Integer> roleIdList = C.newList();
        if (S.noBlank(roleIds)) {
            roleIdList = JSON.parseArray(roleIds, Integer.class);
        }
        return roleIdList;
    }

    public  Map<Integer, Boolean> roleMap() {
        List<Integer> roleIdList = roleIdList();
        Map<Integer, Boolean> roleMap = C.newMap();
        for (Integer roleId : roleIdList) {
            roleMap.put(roleId, true);
        }
        return roleMap;
    }

    /**
     * 对前端输入的明文密码,进行加密
     * @param pwd
     * @return
     */
    @Transient
    public SysUser setEncodePwd(String pwd) {
        if(null != this && S.noBlank(pwd)){
            this.pwd = Act.crypto().passwordHash(pwd);
        }
        return this;
    }

    /**
     * 核验密码
     * @param pwdTxt 明文密码
     * @return
     */
    @Transient
    public boolean verifyPassword(String pwdTxt) {
        if(null == this) return false;
        return Act.crypto().verifyPassword(pwdTxt,this.getPwd());
    }

    public void saveToSession(H.Session session) {
        session.cacheFor30Min(this.getId().toString(), this);
    }

    public SysUser getBySession(H.Session session) {
       return session.cached(this.getId().toString());
    }

    public H.Cookie buildMyCookie(String val){
        H.Cookie cookie = new H.Cookie(cookies_name, val).path("/").maxAge(-1);
        return cookie;
    }

    public static H.Cookie getMyCookie(H.Request request){
        H.Cookie cookie = request.cookie(cookies_name);
        return cookie;
    }

    //=============== geter && setter ================================================

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
