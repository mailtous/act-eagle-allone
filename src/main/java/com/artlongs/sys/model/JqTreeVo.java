package com.artlongs.sys.model;

import java.util.List;

/**
 * Function: JQuery fancytree 树对象
 *
 * @Autor: leeton
 * @Date : 1/19/18
 */
public class JqTreeVo {
    private String key;
    private String title;
    private boolean folder;
    private String tooltip;
    private String url;
    private boolean expanded;
    private List<JqTreeVo> children;
    // ============================ girl && beast ============================

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<JqTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<JqTreeVo> children) {
        this.children = children;
    }
}
