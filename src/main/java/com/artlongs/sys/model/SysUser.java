package com.artlongs.sys.model;


import act.Act;
import act.inject.AutoBind;
import com.alibaba.fastjson.JSON;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;
import com.artlongs.framework.utils.BeanUtils;
import com.artlongs.sys.service.SysRoleService;
import org.osgl.http.H;
import org.osgl.inject.annotation.Configuration;
import org.osgl.util.C;
import org.osgl.util.S;

import javax.inject.Inject;
import javax.inject.Singleton;
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
    private Integer grade; 			//系统用户权限等级 GradeStatus
    private Integer action; 		//激活状态

    @Transient
    public List<Long> roleIdList() {
        List<Long> roleIdList = C.newList();
        if (S.noBlank(roleIds)) {
            roleIdList = JSON.parseArray(roleIds, Long.class);
        }
        return roleIdList;
    }

    /**
     * 返回用户拥有的所有权限
     * @return
     */
    @Transient
    public List<SysRole> roleList() {
        List<SysRole> sysRoleList = C.newList();
        List<Long> roleIdList = roleIdList();
        if (C.notEmpty(roleIdList)) {
            for (Long roleId : roleIdList) {
                SysRole role = new SysRole();
                role.setId(roleId);
                role.setRoleName(SysRoleService.allRoleMap.get(roleId));
                sysRoleList.add(role);
            }
        }
        return sysRoleList;
    }

    @Transient
    public Map<Long, Boolean> hasRoleMap() {
        List<Long> roleIdList = roleIdList();
        Map<Long, Boolean> roleMap = C.newMap();
        for (Long roleId : roleIdList) {
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

    public static SysUser getCurrentLoginUser(H.Session session, H.Request request){
        H.Cookie cookie = request.cookie(cookies_name);
        SysUser sysUser = session.cached(cookie.value());
        return sysUser;
    }

    public static boolean logout(H.Session session, H.Request request){
        H.Cookie cookie = request.cookie(cookies_name);
        session.remove(cookie.value());
        return true;
    }

    @Transient
    public boolean isSuperAdmin() {
        if (action()) {
            return GradeStatus.SUPERADMIN.getKey() == this.grade;
        }
        return false;
    }
    @Transient
    public boolean action() {
        if(null == this.action) return false;
        return  (ON == this.action && DELETED != this.delStatus);
    }

    /**
     * 系统用户权限等级
     */
    public enum GradeStatus{
        DEFAULT(0, "未分配等级"),
        SUPERADMIN(1, "超级管理员"),
        ADMIN(2, "普通管理员"),
        MAJORDOMO(3, "总监"),
        MANAGER(4, "经理"),
        OFFICER(5, "主管"),
        ASSISTANT(6, "专员"),
        SALES(7, "销售员"),
        OTHER(8, "一般人员");

        private Integer key;
        private String cname;

        GradeStatus(Integer key, String cname) {
            this.key = key;
            this.cname = cname;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public static String getCnameByKey(int key) {
            for (GradeStatus type : GradeStatus.values()) {
                if (type.getKey() == key) return type.getCname();
            }
            return "";
        }
        public static GradeStatus getStatusByKey(int key) {
            for (GradeStatus type : GradeStatus.values()) {
                if (type.getKey() == key) return type;
            }
            return DEFAULT;
        }
    }

    public SysUser copyTo(SysUser sysUser) {
        return BeanUtils.copyTo(this, sysUser );
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public static int getON() {
        return ON;
    }

    public static int getOFF() {
        return OFF;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
