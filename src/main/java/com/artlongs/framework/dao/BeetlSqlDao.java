package com.artlongs.framework.dao;

import act.Act;
import act.db.beetlsql.BeetlSqlService;
import act.util.Stateless;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.utils.GenericsUtils;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.engine.PageQuery;
import org.osgl.util.C;
import org.osgl.util.N;
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
        List<T> resultList = sqlm.execute(new SQLReady(sql,args), this.persistentClass);
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
        List<T> resultList = sqlm.execute(new SQLReady(sql), this.persistentClass);
        return getList(this.persistentClass, sql);
    }


    @Override
    public List<T> getList(Class<T> clz, String sql) {
        List<T> resultList = sqlm.execute(new SQLReady(sql), clz);
        return C.isEmpty(resultList) ? new ArrayList<T>() : resultList;
    }

    @Override
    public List<T> getList(String frameSql, Object... args) {
        List<T> resultList = sqlm.execute(new SQLReady(frameSql, args), this.persistentClass);
        return getList(this.persistentClass, frameSql, args);
    }

    @Override
    public List<T> getList(Class<T> clz,String frameSql, Object... args) {
        List<T> resultList = sqlm.execute(new SQLReady(frameSql, args), clz);
        return C.isEmpty(resultList) ? new ArrayList<T>() : resultList;
    }

    @Override
    public Page<T> getPage( Page page,String frameSql, Object[] args) {
        PageQuery<T> pq = myPageToPageQuery(page,new PageQuery());
        SQLReady sqlReady = new SQLReady(frameSql, args);
        pq = sqlm.execute(sqlReady, this.persistentClass, pq);
        return pageQueryToMyPage(pq,page);
    }

    /**
     * 获得实体对应的数据表名
     * @return
     */
    public String getTableName() {
        return S.underscore(this.persistentClass.getSimpleName());
    }

    /**
     * BeeltPage转为我们的Page
     * @param pq
     * @param page
     * @return
     */
    public Page pageQueryToMyPage(PageQuery pq,Page page) {
        page.setItems(pq.getList());
        page.setPageSize(N.Num.valueOf(pq.getPageSize()).intValue());
        page.setTotal(pq.getTotalRow());
        page.setPageNumber(N.Num.valueOf(pq.getPageNumber()).intValue());
        page.setOrderBy(pq.getOrderBy());
        return page;
    }

    /**
     * 我们的Page转为BeeltPage
     * @param page
     * @param pq
     * @return
     */
    public PageQuery myPageToPageQuery(Page page,PageQuery pq) {
        pq.setList(page.getItems());
        pq.setPageNumber(page.getPageNumber());
        pq.setTotalRow(page.getTotal());
        pq.setPageSize(page.getPageSize());
        pq.setOrderBy(page.getOrderBy());
        return pq;
    }


}
