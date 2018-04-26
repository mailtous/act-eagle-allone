package com.artlongs.framework.utils;


import com.artlongs.sys.model.SysDept;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.SQLManager;

/**
 * FUNC: Lambda 风格的 Query Expression.
 * User: leeton
 * Date: 3/14/18
 * Time: 2:33 PM
 */
public class Lq<T> extends Qe<T> {

    public Lq(){}
    public Lq(Class<T> clz) {
       super(clz);
    }

    public Lq(Class<T> clz,SQLManager sqlManager) {
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

    public Lq<T> select(Attr.Property<T, ?>... fun) {
        super.select(getFunctionName(fun));
        return this;
    }

    public Lq<T> select(Attr... methods) {
        super.select(Attr.getColumns(methods));
        return this;
    }

    public static Lq del(Class clz) {
        Lq qe = new Lq();
        qe.sql.append(Opt.DEL.of(getTableName(clz)));
        return qe;
    }


    public Lq<T> where(Lq<T> qe) {
        super.where(qe);
        return this;
    }

    public Lq<T> leftJoin(Class joinTableClass, Attr.Property<T, ?>joinKey, Attr.Property<T, ?> mainKey) {

        return  (Lq<T>) super.leftJoin(joinTableClass, getFunctionName(joinKey),getFunctionName(mainKey));
    }

    public Lq<T> leftJoin(Class joinTableClass, Attr joinKey, Attr mainKey) {

        return (Lq<T>)super.leftJoin(joinTableClass, joinKey.getColumn(),mainKey.getColumn());
    }

    public Lq<T> eq(Attr.Property<T, ?>fun, Object val) {
        super.eq(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> ne(Attr.Property<T, ?>fun, Object val) {
        super.ne(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> lt(Attr.Property<T, ?>fun, Object val) {
        super.lt(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> gt(Attr.Property<T, ?>fun, Object val) {
        super.gt(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> le(Attr.Property<T, ?>fun, Object val) {
        super.le(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> ge(Attr.Property<T, ?>fun, Object val) {
        super.ge(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> in(Attr.Property<T, ?>fun, Object val) {
        super.in(getFunctionName(fun),val);
        return this;
    }

    public Lq<T> between(Attr.Property<T, ?>fun, Object left , Object right) {
        super.between(getFunctionName(fun),left,right);
        return this;
    }

    public Lq<T> isnull(Attr.Property<T, ?>fun) {
        super.isnull(getFunctionName(fun));
        return this;
    }

    public Lq<T> notnull(Attr.Property<T, ?>fun) {
        super.notnull(getFunctionName(fun));
        return this;
    }

    public Lq<T> like(Attr.Property<T, ?>fun, Object v1 ) {
        super.like(getFunctionName(fun),v1);
        return this;
    }

    public Lq<T> and(Lq<T> qe) {
        super.and(qe);
        return this;
    }

    public Lq<T> or(Lq<T> qe) {
        super.or(qe);
        return this;
    }

    public Lq<T> asc(Attr.Property<T, ?>fun) {
        super.asc(getFunctionName(fun));
        return this;
    }
    public Lq<T> desc(Attr.Property<T, ?>fun) {
        super.desc(getFunctionName(fun));
        return this;
    }

    public String limit(Integer l, Integer r) {
        return super.limit(l, r);
    }

    public Lq<T> group(Attr.Property<T, ?>... funs) {
        super.group(getFunctionName(funs));
        return this;
    }

    public Lq<T> having(Attr.Property<T, ?>fun, Opt opt, Object val) {
        super.having(getFunctionName(fun), opt, val);
        return this;
    }



    public static void main(String[] args) throws Exception {
        Attr<SysDept> dept = new Attr<SysDept>();
        Attr<SysUser> user = new Attr<SysUser>();
        String sql = new Lq<SysUser>(SysUser.class)
                .select(SysUser::getDeptId)
                .leftJoin(SysDept.class, dept.parse(SysDept::getId),user.parse(SysUser::getDeptId))
                .where(new Lq<SysUser>().like(SysUser::getDeptId, 1))
                .asc(SysUser::getDeptId)
                .desc(SysUser::getUserName)
                .group(SysUser::getDeptId)
                .having(SysUser::getDeptId, Opt.GT, 0)
                .build();

        System.out.println("sql=" + sql);



    }

}
