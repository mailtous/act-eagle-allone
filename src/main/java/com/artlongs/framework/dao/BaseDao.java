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

    T getObj(String sql, Object... args);

    Long count(String sql, Object... args);

    List<T> getList(String sql);

    List<T> getList(String frameSql, Object... args);

    Page<T> getPage(Page page, String frameSql, Object[] args);

}
