package com.artlongs.sys.model;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/21/17
 */
public class FuncVo{
    private Long id;
    private Long parentId;
    private String funcName;
    private String funcUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFuncUrl() {
        return funcUrl;
    }

    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl;
    }
}