package com.artlongs.framework.service;

import com.artlongs.framework.dao.BaseDao;
import com.artlongs.framework.model.BaseEntity;
import com.artlongs.framework.page.Page;
import org.beetl.sql.core.DSTransactionManager;

import java.util.Date;
import java.util.List;

/**
 * Service基类,包含基本的CRUD
 * Created by leeton on 9/29/17.
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected BaseDao<T> baseDao;

    public BaseServiceImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    /**
     * Beelsql的事务管理类
     */
    public DSTransactionManager tx = new DSTransactionManager();


    public BaseServiceImpl() {
    }

    @Override
    public T get(Long id) {
        return baseDao.get(id);
    }

    @Override
    public int save(T t) {
        return baseDao.save(t);
    }

    @Override
    public int add(BaseEntity t) {
        t.setCreateDate(new Date());
        t.setModifyDate(new Date());
        return save((T)t);
    }

    @Override
    public int update(T t) {
        return baseDao.update(t);
    }

    @Override
    public int updateAndTime(BaseEntity t) {
        t.setModifyDate(new Date());
        return update((T)t);
    }

    @Override
    public int delete(T t) {
        return baseDao.delete(t);
    }

    @Override
    public int delete(Long id) {
        return baseDao.delete(id);
    }

    public T getObj(String sql,Object... args){
       return baseDao.getObj(sql, args);
    }

    public Long count(String sql,Object... args){
        return baseDao.count(sql, args);
    }

    @Override
    public List<T> getList(String frameSql, Object... args) {
        return baseDao.getList(frameSql, args);
    }

    @Override
    public List<T> getList(String sql) {
        return baseDao.getList(sql);
    }

    @Override
    public Page<T> getPage(Page page, String frameSql, Object[] args) {
        return baseDao.getPage(page, frameSql, args);
    }
}
