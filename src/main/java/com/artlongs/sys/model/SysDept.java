package com.artlongs.sys.model;

import act.inject.AutoBind;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;

import javax.inject.Singleton;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysDept extends BaseEntity{

    private String deptName;


    @Singleton
    @AutoBind
    public static abstract class Dao<T extends SysDept> extends BeetlSqlDao<SysDept> {
        public static String table = "sys_dept";
        public static String id = "id";
        public static String createDate="create_date";
        public static String modifyDate="modify_date";
        public static String deptName = "dept_name";
    }


    // ============================ girl && beast ============================

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
