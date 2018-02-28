package com.artlongs.framework.utils;

/**
 * Function: 查询表达式
 *
 * @Autor: leeton
 * @Date : 2/27/18
 */
public class QE {
    private String k;
    private String v;
    private String opt; //操作符

    //以下为各种操作(= != like ...)
    private String eq;
    private String ne;


    public String k() {
        return k;
    }

    public QE k(String key) {
        this.k = key;
        return this;
    }

    public String v() {
        return v;
    }
    public String opt() {
        return opt;
    }

    public QE eq(String v) {
        this.v = v;
        this.opt = "=";
        return this;
    }
    public QE ne(String v) {
        this.v = v;
        this.opt = "!=";
        return this;
    }


}
