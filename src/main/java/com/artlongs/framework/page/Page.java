package com.artlongs.framework.page;

import org.beetl.sql.core.engine.PageQuery;
import org.osgl.util.N;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leeton on 9/25/17.
 */
public class Page<T> implements Serializable {
    // -- 公共变量 --//
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final int DEFAULT_PAGE_SIZE = 10;
    // -- 分页参数 --//
    protected Integer pageNumber = 1;
    protected Integer pageSize = DEFAULT_PAGE_SIZE;
    protected String orderBy = "";      //排序表达式(id desc,name asc)
    protected boolean autoCount = true;
    protected String[] groupBy ;    //分组条件

    protected List<T> items = new ArrayList<T>();  //返回数据列表
    protected long total = 0;  //总条数
    protected String url = ""; //页面的URL

    //======
    public Page() {
    }

    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public Page setPageNumber(Integer pageNumber) {
        if (null == pageNumber || pageNumber < 1) {
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
        return this;
    }

    /**
     * 获得每页的记录数量,默认为1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量,低于1时自动调整为1.
     */
    public Page setPageSize(Integer pageSize) {
        if (null == pageSize || pageSize < 1) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
     */
    public int getFirst() {
        return (pageNumber - 1) * pageSize;
    }

    /**
     * 显示当前页之后的剩余页数
     *
     * @return
     */
    public int getShowSize() {
        if (null == items) {
            return getFirst();
        }
        return getFirst() + items.size();
    }


    /**
     * 查询对象时是否自动另外执行count查询获取总记录数, 默认为true.
     */
    public boolean isAutoCount() {
        return autoCount;
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数.
     */
    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    /**
     * 取得页内的记录列表.
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * 设置页内的记录列表.
     */
    public Page setItems(final List<T> items) {
        this.items = items;
        return this;
    }

    /**
     * 取得总记录数, 默认值为0.
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数.
     */
    public void setTotal(final long total) {
        this.total = total;
    }

    /**
     * 根据pageSize与total计算总页数, 默认值为0.
     */
    public long getTotalPages() {
        if (total < 0) {
            return 0;
        }

        long count = total / pageSize;
        if (total % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNumber + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNumber + 1;
        } else {
            return pageNumber;
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageNumber - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNumber - 1;
        } else {
            return pageNumber;
        }
    }

    /**
     * BeeltPage转为我们的Page
     * @param pq
     * @param page
     * @return
     */
    public Page pageQueryToMyPage(PageQuery pq, Page page) {
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

    public String[] getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String[] groupBy) {
        this.groupBy = groupBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
