package com.artlongs.sys.model;

import act.util.Stateless;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;

import javax.inject.Inject;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysDept extends BaseEntity{

    private String deptName;


    @Stateless
    public static abstract class Dao<T> extends BeetlSqlDao<SysDept> {
        public static String table = "sys_dept";
        public static String id = "id";
        public static String createDate="create_date";
        public static String modifyDate="modify_date";
        public static String deptName = "dept_name";
    }


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
