package com.artlongs.framework.utils;

import com.artlongs.sys.model.SysDept;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.SQLManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Func :
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
        super.select(getFunctionName(funs));
        return this;
    }
    public Lq<T> selectAs(Attr.Property<T, ?> func,String as) {
        super.selectAs(getFunctionName(func), as);
        return this;
    }

    /**
     * 选择其它数据表的字段
     */
    public Lq<T> select(Attr... funs) {
        if (null != funs && funs.length > 0) {
            List<String> fields = new ArrayList<String>();
            for (Attr fun : funs) {
                fields.add(fun.getColumn());
            }
            super.select(funs[0].getClz(), fields.toArray(new String[0]));
        }
        return this;
    }

    public Lq<T> selectAs(Attr fun,String as) {
        super.selectAs(fun.getClz(),fun.getColumn(), as);
        return this;
    }


    public Lq<T> sum(Attr.Property<T, ?> fun) {
        super.sum(getFunctionName(fun), clz);
        return this;
    }

    public Lq<T> sum(Attr<T> otherTableAttr) {
        super.sum(otherTableAttr.getColumn(), otherTableAttr.getClz());
        return this;
    }

    public Lq<T> sumAs(Attr.Property<T, ?> fun, String as) {
        super.sumAs(getFunctionName(fun), as, this.clz);
        return this;
    }

    public Lq<T> sumAs(Attr<T> otherTableAttr, String as) {
        super.sumAs(otherTableAttr.getColumn(), as, otherTableAttr.getClz());
        return this;
    }

    public Lq<T> sumCase(Attr.Property<T, ?> caseField, Object eqVal, Attr.Property<T, ?> sumField, String asFiled) {
        super.sumCase(getFunctionName(caseField), eqVal, getFunctionName(sumField), asFiled, null);
        return this;
    }

    public Lq<T> caseAs(Attr.Property<T, ?> caseField, Object eqVal, Attr.Property<T, ?> targetField, String asFiled) {
        super.caseAs(getFunctionName(caseField), eqVal, getFunctionName(targetField), asFiled, null);
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

    public Lq<T> and(Lq<T>... manyQe) {
        super.and(manyQe);
        return this;
    }

    public Lq<T> or(Lq<T>... manyQe) {
        super.or(manyQe);
        return this;
    }

    // =============== and ==============================
    public Lq<T> andEq(Attr.Property<T, ?> column, Object val) {
        super.andEq(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andNe(Attr.Property<T, ?> column, Object val) {
        super.andNe(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andLt(Attr.Property<T, ?> column, Object val) {
        super.andLt(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andGt(Attr.Property<T, ?> column, Object val) {
        super.andGt(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andLe(Attr.Property<T, ?> column, Object val) {
        super.andLe(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andGe(Attr.Property<T, ?> column, Object val) {
        super.andGe(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andIn(Attr.Property<T, ?> column, Object val) {
        super.andIn(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andBetween(Attr.Property<T, ?> column, Object v1, Object v2) {
        super.andBetween(getFunctionName(column), v1, v2, null);
        return this;
    }

    public Lq<T> andLike(Attr.Property<T, ?> column, Object val) {
        super.andLike(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> andIsnull(Attr.Property<T, ?> column) {
        super.andIsnull(getFunctionName(column), null);
        return this;
    }

    public Lq<T> andNotnull(Attr.Property<T, ?> column) {
        super.andNotnull(getFunctionName(column), null);
        return this;
    }

    // =============== OR CONDITION ==============================
    public Lq<T> orEq(Attr.Property<T, ?> column, Object val) {
        super.orEq(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orNe(Attr.Property<T, ?> column, Object val) {
        super.orNe(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orLt(Attr.Property<T, ?> column, Object val) {
        super.orLt(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orGt(Attr.Property<T, ?> column, Object val) {
        super.orGt(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orLe(Attr.Property<T, ?> column, Object val) {
        super.orLe(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orGe(Attr.Property<T, ?> column, Object val) {
        super.orGe(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orIn(Attr.Property<T, ?> column, Object val) {
        super.orIn(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orBetween(Attr.Property<T, ?> column, Object v1, Object v2) {
        super.orBetween(getFunctionName(column), v1, v2, null);
        return this;
    }

    public Lq<T> orLike(Attr.Property<T, ?> column, Object val) {
        super.orLike(getFunctionName(column), val, null);
        return this;
    }

    public Lq<T> orIsnull(Attr.Property<T, ?> column) {
        super.orIsnull(getFunctionName(column), null);
        return this;
    }

    public Lq<T> orNotnull(Attr.Property<T, ?> column) {
        super.orNotnull(getFunctionName(column), null);
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

    public static void main(String[] args) throws Exception {
        String sql = new Lq<>(SysUser.class)
                .select(SysUser::getDeptId)
                .select(new Attr<>(SysDept::getDeptName))
                .leftJoin(SysDept.class, new Attr<>(SysUser::getDeptId))
                .andLike(SysUser::getDeptId, 1)
                .andIn(SysUser::getDeptId, new Integer[]{1, 2, 3})
                .andBetween(SysUser::getCreateDate, new Date(), new Date())
                .asc(SysUser::getDeptId)
                .desc(SysUser::getUserName)
                .group(SysUser::getDeptId)
                .having(SysUser::getDeptId, Qe.Opt.GT, 0)
                .build();

        System.out.println("sql=" + sql);


    }

}
