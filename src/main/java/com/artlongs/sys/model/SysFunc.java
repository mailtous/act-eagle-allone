package com.artlongs.sys.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.NameFilter;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;
import org.osgl.util.C;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class SysFunc extends BaseEntity {

    public static final int ON =1;
    public static final int OFF =0;
    public static final int TOP_NODE =0;  //顶层节点

    private Long parentId;
    private String funcName;
    private Integer node;   // 节点层级
    private String funcUrl;
    private Integer isMenu;
    private Integer action;
    private Integer sequence;

    public boolean hasChilds = false;  //是否有子节点

    @Transient
    private List<SysFunc> childs; //子菜单

    public static abstract class Dao<SysFunc> extends BeetlSqlDao<SysFunc> {
        public static String table = "sys_func";
        public static String id = "id";
        public static String createDate="create_date";
        public static String modifyDate="modify_date";
        public static String parentId="parent_id";
        public static String funcName ="func_name";
        public static String node="node";
        public static String funcUrl="func_url";
        public static String isMenu="is_menu";
        public static String action="action";
        public static String sequence="sequence";

    }


    /** 顶层菜单 **/
    @Transient
    public static SysFunc blankTopMenu(){
        SysFunc menu = new SysFunc();
        menu.setFuncName("MY MENU");
        menu.hasChilds = true;
        menu.setNode(0);
        menu.setParentId(0L);
        menu.setId(0L);
        menu.setChilds(new ArrayList<>());
        return menu;
    }

    /**
     * 转为JQuery fancytree 树对象
     * @param childs
     * @return
     */
    @Transient
    public String childsToJqTreeStr() {
        String tree = JSON.toJSONString(new JqTreeVo());
        if (C.notEmpty(this.childs)) {
            tree = JSON.toJSONString(this.childs, nameFilter);
        }
        return tree;
    }

    /**
     * Fastjson 转属性,好用呀
     */
    @Transient
    public transient NameFilter nameFilter = new NameFilter() {
        @Override
        public String process(Object o, String propertyName, Object propertyValue) {
            if (propertyName.equalsIgnoreCase("id")) {
                return "key";
            }
            if (propertyName.equalsIgnoreCase("funcName")) {
                return "title";
            }
            if (propertyName.equalsIgnoreCase("childs")) {
                return "children";
            }
            if (propertyName.equalsIgnoreCase("funcUrl")) {
                return "url";
            }
            if (propertyName.equalsIgnoreCase("hasChilds")) {
                return "folder";
            }
            return propertyName;
        }
    };
    public Integer getIsMenu() {

        return isMenu==null?0:isMenu;
    }

    public Integer getAction() {
        return null==action?0:action;
    }

    //=================

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public String getFuncUrl() {
        return funcUrl;
    }

    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<SysFunc> getChilds() {
        return childs;
    }

    public void setChilds(List<SysFunc> childs) {
        this.childs = childs;
    }

}
