package com.artlongs.framework.query;

import org.beetl.sql.core.SQLManager;

import java.util.List;

/**
 * Func : lambda 风格的 sql 查询
 * 约定: 1. 数据库字段默认风格是全小写加下线分隔的 underline 风格
 *       2.使用 Attr.Property 入参代表是使用默认的主表,
 *       3.使用 new Attr<>(x::getMethod) 入参,则使用你指定的 x 做为从表.
 *       4.不指定 spell 默认是把字段 AS 为驼峰格式
 *       5.如果使用在子查询里则要指定拼写为 Spell.UNDERLINE
 *
 * @author: leeton on 2019/6/10.
 */
public class Lq<T> extends Qe<T> {
    public Lq() {
    }

    public Lq(Class<T> clz) {
        super(clz);
    }

    public Lq(Class<T> clz, SQLManager sqlManager) {
        super(clz, sqlManager);
    }
    public Lq(SQLManager sqlManager) {
        super(sqlManager);
    }

    private String getFunctionName(Attr.Property<T, ?> fun) {
        return new Attr<>(fun).getColumn();
    }

    private String[] getFunctionName(Attr.Property<T, ?>... funs) {
        String[] cols = new String[funs.length];
        int i = 0;
        for (Attr.Property<T, ?> fun : funs) {
            cols[i++] = this.getFunctionName(fun);
        }
        return cols;

    }

    public Lq<T> select(Attr.Property<T, ?>... funs) {
        select(Spell.CAMEL,funs);
        return this;
    }

    public Lq<T> select(Spell spell,Attr.Property<T, ?>... funs) {
        for (Attr.Property<T, ?> fun : funs) {
            select(spell,fun);
        }
        return this;
    }

    public Lq<T> selectAs(Attr.Property<T, ?> func, String as) {
        super.selectAs(getFunctionName(func), as);
        return this;
    }
    public Lq<T> select(Spell spell,Attr.Property<T, ?> func) {
        super.select(spell,func);
        return this;
    }

    public Lq<T> select(Attr.Property<T, ?> func,Spell spell) {
        super.select(spell,func);
        return this;
    }

    /**
     * 选择其它数据表的字段
     */
    public Lq<T> select(Attr... attrs) {
        if (null != attrs && attrs.length > 0) {
            String[] columns = new String[attrs.length];
            String[] alias = new String[attrs.length];
            for (int i = 0; i < attrs.length; i++) {
                String as = attrs[i].getColumn().equals(attrs[i].getName())?null:attrs[i].getName();
                super.selectAs(attrs[i].getTableName(), attrs[i].getColumn(), as);
            }
        }
        return this;
    }

    public Lq<T> selectAs(Attr attr, String as) {
        super.selectAs(attr.getClz(), attr.getColumn(), as);
        return this;
    }

    public Lq<T> select(Attr attr,Spell spell) {
        super.select(spell,attr);
        return this;
    }
    public Lq<T> select(Spell spell,Attr attr) {
        super.select(spell,attr);
        return this;
    }

    public Lq<T> select(Spell spell,Attr... attrs) {
        super.select(spell,attrs);
        return this;
    }

    public Lq<T> select(Qf... qfs) {
        for (Qf qf : qfs) {
            super.select(qf);
        }
        return this;
    }

    public Lq<T> fromSubSelect(Lq<T> subSelect) {
        super.fromSubSelect(subSelect);
        return this;
    }

    public Lq<T> fromSubSelect(Lq<T> subSelect, String as) {
        super.fromSubSelect(subSelect, as);
        return this;
    }

    public Lq<T> sum(Attr.Property<T, ?> fun) {
        super.sum(getFunctionName(fun));
        return this;
    }

    public Lq<T> sum(Attr<T> otherTableAttr) {
        super.sum(otherTableAttr.getColumn(), otherTableAttr.getClz());
        return this;
    }

    public Lq<T> sumAs(Attr.Property<T, ?> fun, String as) {
        super.sumAs(getFunctionName(fun), as);
        return this;
    }

    public Lq<T> sumAs(Attr<T> otherTableAttr, String as) {
        super.sumAs(otherTableAttr.getColumn(), as, otherTableAttr.getTableName());
        return this;
    }

    public Lq<T> sumCase(Attr.Property<T, ?> caseField, Object eqVal, Attr.Property<T, ?> sumField, String asFiled) {
        super.sumCase(getFunctionName(caseField), eqVal, getFunctionName(sumField), asFiled);
        return this;
    }

    public Lq<T> caseAs(Attr.Property<T, ?> caseField, Object eqVal, Attr.Property<T, ?> targetField, String asFiled) {
        super.caseAs(getFunctionName(caseField), eqVal, getFunctionName(targetField), asFiled);
        return this;
    }

    public Lq<T> leftJoin(Class joinTableClass, Attr joinKey, Attr mainKey) {
        super.leftJoin(joinTableClass, joinKey.getColumn(), mainKey.getColumn());
        return this;
    }

    public Lq<T> leftJoin(Class joinTableClass, Attr mainKey) {
        super.leftJoin(joinTableClass, "id", mainKey.getColumn());
        return this;
    }

    // =============== and ==============================
    public Lq<T> and(Lq<T>... manyQe) {
        super.and(manyQe);
        return this;
    }

    public Lq<T> or(Lq<T>... manyQe) {
        super.or(manyQe);
        return this;
    }

    public Lq<T> andEq(Attr.Property<T, ?> column, Object val) {
        super.andEq(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andNe(Attr.Property<T, ?> column, Object val) {
        super.andNe(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andLt(Attr.Property<T, ?> column, Object val) {
        super.andLt(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andGt(Attr.Property<T, ?> column, Object val) {
        super.andGt(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andLe(Attr.Property<T, ?> column, Object val) {
        super.andLe(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andGe(Attr.Property<T, ?> column, Object val) {
        super.andGe(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andIn(Attr.Property<T, ?> column, Object val) {
        super.andIn(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andBetween(Attr.Property<T, ?> column, Object v1, Object v2) {
        super.andBetween(getFunctionName(column), v1, v2);
        return this;
    }

    public Lq<T> andLike(Attr.Property<T, ?> column, Object val) {
        super.andLike(getFunctionName(column), val);
        return this;
    }

    public Lq<T> andIsnull(Attr.Property<T, ?> column) {
        super.andIsnull(getFunctionName(column));
        return this;
    }

    public Lq<T> andNotnull(Attr.Property<T, ?> column) {
        super.andNotnull(getFunctionName(column));
        return this;
    }

    // =============== OR CONDITION ==============================
    public Lq<T> orEq(Attr.Property<T, ?> column, Object val) {
        super.orEq(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orNe(Attr.Property<T, ?> column, Object val) {
        super.orNe(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orLt(Attr.Property<T, ?> column, Object val) {
        super.orLt(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orGt(Attr.Property<T, ?> column, Object val) {
        super.orGt(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orLe(Attr.Property<T, ?> column, Object val) {
        super.orLe(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orGe(Attr.Property<T, ?> column, Object val) {
        super.orGe(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orIn(Attr.Property<T, ?> column, Object val) {
        super.orIn(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orBetween(Attr.Property<T, ?> column, Object v1, Object v2) {
        super.orBetween(getFunctionName(column), v1, v2);
        return this;
    }

    public Lq<T> orLike(Attr.Property<T, ?> column, Object val) {
        super.orLike(getFunctionName(column), val);
        return this;
    }

    public Lq<T> orIsnull(Attr.Property<T, ?> column) {
        super.orIsnull(getFunctionName(column));
        return this;
    }

    public Lq<T> orNotnull(Attr.Property<T, ?> column) {
        super.orNotnull(getFunctionName(column));
        return this;
    }

    public Lq<T> asc(Attr.Property<T, ?>... val) {
        super.asc(getFunctionName(val));
        return this;
    }

    public Lq<T> desc(Attr.Property<T, ?>... val) {
        super.desc(getFunctionName(val));
        return this;
    }

    public Lq<T> group(Attr.Property<T, ?>... val) {
        super.group(getFunctionName(val));
        return this;
    }

    public Lq<T> having(Attr.Property<T, ?> column, Opt opt, Object val) {
        super.having(getFunctionName(column), opt, val);
        return this;
    }

    public Lq<T> whereOfFilters(List<PropertyFilter> filterList) {
        super.whereOfFilters(filterList);
        return this;
    }

    public static void main(String[] args) throws Exception {
/*
        String sql = new Lq<>(SysUser.class)
                .select(SysUser::getDeptId)
                .select(new Attr<>(SysDept::getDeptName))
                .leftJoin(SysDept.class, new Attr<>(SysUser::getDeptId))
                .andLike(SysUser::getDeptId, 1)
                .asc(SysUser::getDeptId)
                .desc(SysUser::getUserName)
                .group(SysUser::getDeptId)
                .having(SysUser::getDeptId, Qe.Opt.GT, 0)
                .build();

        System.out.println("sql=" + sql);
*/


    }

}
