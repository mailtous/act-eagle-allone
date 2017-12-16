package com.artlongs.sys.model;

import com.artlongs.framework.model.BaseEntity;

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
@Entity
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

    @Transient
    private List<SysFunc> childs; //子菜单
    @Transient
    public static SysFunc blankMenu(){
        SysFunc menu = new SysFunc();
        menu.setFuncName("MY MENU");
        menu.setNode(0);
        menu.setParentId(0L);
        menu.setId(1L);
        menu.setChilds(new ArrayList<>());
        return menu;
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

    public Integer getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public Integer getAction() {
        return action;
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
