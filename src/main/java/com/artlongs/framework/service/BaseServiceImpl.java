package com.artlongs.framework.service;

import act.db.beetlsql.BeetlSqlTransactional;
import act.db.sql.tx.Transactional;
import com.artlongs.framework.dao.BaseDao;
import com.artlongs.framework.model.BaseEntity;
import com.artlongs.framework.page.Page;
import org.beetl.sql.core.DSTransactionManager;
import org.osgl.mvc.annotation.With;

import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

/**
 * Service基类,包含基本的CRUD
 * Created by leeton on 9/29/17.
 */
@Singleton
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    public BaseServiceImpl() {
    }

    protected BaseDao<T> baseDao;

    public BaseServiceImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
    /**
     * Beelsql的事务管理类
     */
    public DSTransactionManager tx = new DSTransactionManager();

    @Override
    public T get(Long id) {
        return baseDao.get(id);
    }

    @Override
    @Transactional
    public int save(T t) {
        int pk = baseDao.save(t);
        return pk;
    }

    @Override
    @Transactional
    public int add(BaseEntity t) {
        t.setCreateDate(new Date());
        t.setModifyDate(new Date());
        return save((T)t);
    }

    @Override
    @Transactional
    public int update(T t) {
        return baseDao.update(t);
    }

    @Override
    @Transactional
    public int updateAndTime(BaseEntity t) {
        t.setModifyDate(new Date());
        return update((T)t);
    }

    @Override
    @Transactional
    public int delete(T t) {
        return baseDao.delete(t);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        return baseDao.delete(id);
    }

    public T getObj(String frameSql,Object... args){
       return baseDao.getObj(frameSql, args);
    }

    public T getObj(Class<T>clz,String frameSql,Object... args){
       return baseDao.getObj(clz,frameSql, args);
    }

    public Long count(String frameSql,Object... args){
        return baseDao.count(frameSql, args);
    }

    @Override
    public List<T> getList(String frameSql, Object... args) {
        return baseDao.getList(frameSql, args);
    }

    @Override
    public List<T> getList(Class<T>clz,String sql) {
        return baseDao.getList(clz,sql);
    }

    @Override
    public List<T> getList(Class<T>clz,String frameSql, Object... args) {
        return baseDao.getList(clz,frameSql, args);
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
