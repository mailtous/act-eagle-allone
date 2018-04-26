package com.artlongs.framework.dao;

import act.Act;
import act.db.beetlsql.BeetlSqlService;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.utils.GenericsUtils;
import com.artlongs.framework.utils.Lq;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.engine.PageQuery;
import org.osgl.util.C;
import org.osgl.util.S;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本的 CRUD, DAO基类
 * Created by leeton on 9/30/17.
 */
@Singleton
public class BeetlSqlDao<T> implements BaseDao<T> {

    protected SQLManager sqlm;             // Beelsql 的实际操作类
    private BeetlSqlService dbService;   //取得数据源的连接
    private Class<T> persistentClass;    //实体类,子类调用才有效果,可恶的JAVA泛型擦除。

    public BeetlSqlDao() {
        dbService = Act.app().dbServiceManager().dbService("default");
        sqlm = dbService.beetlSql();
        getPersistentClass();
    }

    public Class<T> getPersistentClass() {
        if (null == persistentClass) {
            this.persistentClass = GenericsUtils.getSuperClassGenricType(this.getClass());
        }
        return persistentClass;
    }

    /**
     * Lambda 查询
     * @param clz 实体类
     * @return
     */
    public Lq<T> lq(Class clz,SQLManager sqlManager){
        return new Lq<T>(clz,sqlManager);
    }

    public Lq<T> lq(){
        return lq(this.persistentClass,sqlm);
    }


    @Override
    public T get(Long id) {
        return sqlm.single(this.persistentClass, id);
    }

    @Override
    public int save(T t) {
        return sqlm.insert(t);
    }

    @Override
    public int update(T t) {
        return sqlm.updateById(t);
    }

    @Override
    public int delete(T t) {
        return sqlm.deleteObject(t);
    }

    @Override
    public int delete(Long id) {
        return sqlm.deleteById(this.persistentClass, id);
    }

    public T getObj(String sql,Object... args){
        return getObj(this.persistentClass,sql,args);
    }

    public T getObj(Class<T> clz,String sql,Object... args){
        List<T> resultList = sqlm.execute(new SQLReady(sql,args), clz);
        return C.isEmpty(resultList) ? null : resultList.get(0);
    }

    @Override
    public Long count(String sql,Object... args) {
        List<Long> countList = sqlm.execute(new SQLReady(sql,args),Long.class);
        Long count = C.isEmpty(countList)?new Long(0):countList.get(0);
        return count;
    }

    public List<T> getList(String sql) {
        return getList(this.persistentClass, sql);
    }


    @Override
    public List<T> getList(Class<T> clz, String sql) {
        return  getList(clz, sql, null);
    }

    @Override
    public List<T> getList(String frameSql, Object... args) {
        return getList(this.persistentClass, frameSql, args);
    }

    @Override
    public List<T> getList(Class<T> clz,String frameSql, Object... args) {
        List<T> resultList = sqlm.execute(new SQLReady(frameSql, args), clz);
        return C.isEmpty(resultList) ? new ArrayList<T>() : resultList;
    }

    @Override
    public Page<T> getPage(Page page,String frameSql, Object[] args) {
        return getPage(this.persistentClass, page, frameSql, args);
    }

    @Override
    public Page<T> getPage(Class<T> clz, Page page,String frameSql, Object[] args) {
        PageQuery<T> pq = page.myPageToPageQuery(page,new PageQuery());
        SQLReady sqlReady = new SQLReady(frameSql, args);
        pq = sqlm.execute(sqlReady, clz, pq);
        return page.pageQueryToMyPage(pq,page);
    }

    /**
     * 获得实体对应的数据表名
     * @return
     */
    public String getTableName() {
        return S.underscore(this.persistentClass.getSimpleName());
    }




}
