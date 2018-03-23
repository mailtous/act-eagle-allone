package com.artlongs.framework.dao;

import com.artlongs.framework.page.Page;

import java.util.List;

/**
 * Created by leeton on 9/30/17.
 */
public interface BaseDao<T> {

    T get(Long id);

    int save(T t);

    int update(T t);

    int delete(T t);

    int delete(Long id);

    /**
     * T 为默认的实体类
     *
     * @param frameSql
     * @param args
     * @return
     */
    T getObj(String frameSql, Object... args);

    /**
     * 按指定的 class 返回
     *
     * @param clz
     * @param frameSql
     * @param args
     * @return
     */
    T getObj(Class<T> clz, String frameSql, Object... args);

    Long count(String frameSql, Object... args);

    List<T> getList(String sql);

    List<T> getList(Class<T> clz, String sql);

    List<T> getList(String frameSql, Object... args);

    List<T> getList(Class<T> clz, String frameSql, Object... args);

    Page<T> getPage(Page page, String frameSql, Object[] args);
    Page<T> getPage(Class<T> clz, Page page, String frameSql, Object[] args);

}
