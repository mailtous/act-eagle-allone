package com.artlongs.framework.utils;

import com.artlongs.framework.page.Page;
import com.artlongs.sys.model.SysDept;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.kit.BeanKit;
import org.osgl.util.C;
import org.osgl.util.S;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.artlongs.framework.utils.Qe.Opt.*;

/**
 * Function: 查询表达式
 *
 * @Autor: leeton
 * @Date : 2/27/18
 */
public class Qe<T> {

    protected Integer id = 0;
    protected SQLManager sqlManager;
    protected Class clz;
    protected String mainTableName = "";
    protected String select = " SELECT * ";
    protected String from = " FROM ";
    protected StringBuffer group = new StringBuffer();
    protected StringBuffer having = new StringBuffer();
    protected String sqlTemplate = " {select} {from} {where} {group} {having} {order}";
    protected StringBuffer sql = new StringBuffer();  // 不包含 group having order 语句
    protected StringBuffer order = new StringBuffer();



    public Qe() {
    }

    public Qe(Class<T> mainTableClass) {
        this.clz = mainTableClass;
        this.mainTableName = getTableName(mainTableClass);
        this.from = FROM.sql(mainTableName,mainTableName);
    }

    public Qe(Class<T> clz,SQLManager sqlManager) {
        this.clz = clz;
        this.sqlManager = sqlManager;
        String tableName = getTableName(clz);
        this.mainTableName = tableName;
        this.from = FROM.sql(mainTableName,mainTableName);
    }

    public Qe(String mainTableName) {
        this.mainTableName = mainTableName;
    }

    public Lq<T> lambda() {
        if (BeanKit.queryLambdasSupport) {
            Lq<T> lq = new Lq<T>();
            if (this.sql != null) {
                throw new UnsupportedOperationException("LamdbaQuery必须在调用其他AP前获取");
            }
            return lq;
        } else {
            throw new UnsupportedOperationException("需要使用Java8以上，并且依赖com.trigersoft:jaque,请查阅官网文档");
        }

    }

    public static String getTableName(Class clz) {
        return new UnderlinedNameConversion().getTableName(clz);
    }

    public String count(){
        if(this.sql.indexOf("SELECT") ==-1 && this.sql.indexOf("select") ==-1){
            this.sql.insert(0," SELECT count(*) ");
        }
        return sql();
    }

    public String build(){
        String sql = this.sqlTemplate.toString();
        if(this.sql().indexOf("DELETE FROM")==-1){
            sql = sql.replace("{select}", this.select)
                    .replace("{from}", this.from)
                    .replace("{where}", this.sql);

            if(0<this.group.length()){
                sql = sql.replace("{group}", this.group);
            }else {
                sql = sql.replace("{group}", "");
            }

            if(0<this.having.length()){
                sql = sql.replace("{having}", this.having);
            }else {
                sql = sql.replace("{having}", "");
            }

            if(0<this.order.length()){// ORDER 语句要排在最后
                sql = sql.replace("{order}", ORDER.of(this.order));
            }else {
                sql = sql.replace("{order}", "");
            }

        }else {
            sql = this.sql.toString();
        }

        return sql;
    }
    public String sql() {
        return this.sql.toString();
    }

    private Qe<T> append(String sql) {
        this.sql.append(sql);
        return this;
    }

    public Qe select(String... val) {
        if (val.length > 0) {
            this.select = SELECT.of(val);
        }
        return this;
    }

    public static Qe del(String table) {
        Qe qe = new Qe();
        qe.sql.append(DEL.of(table));
        return qe;
    }

    public Qe where(Qe qe) {
        return append(WHERE.of(qe.sql()));
    }

    public Qe leftJoin(String joinTableName, String joinTableKey, String mainTableKey) {
        if (S.blank(this.mainTableName)) throw new RuntimeException("主表不能为空。");
        this.sql.append(LEFTJOIN.of(joinTableName));
        return append(ON.on(this.mainTableName, mainTableKey, joinTableName, joinTableKey));
    }

    public Qe leftJoin(Class<T> clz, String joinTableKey, String mainTableKey) {
        return leftJoin(getTableName(clz), joinTableKey, mainTableKey);
    }

    public Qe eq(String column, Object val) {
        return append(EQ.sql(column, val));
    }

    public Qe ne(String column, Object val) {
        return append(NE.sql(column, val));
    }

    public Qe lt(String column, Object val) {
        return append(LT.sql(column, val));
    }

    public Qe gt(String column, Object val) {
        return append(GT.sql(column, val));
    }

    public Qe le(String column, Object val) {
        return append(LE.sql(column, val));
    }

    public Qe ge(String column, Object val) {
        return append(GE.sql(column, val));
    }

    public Qe in(String column, Object val) {
        return append(IN.sql(column, val));
    }

    public Qe between(String column, Object val1, Object val2) {
        return append(BETWEEN.between(column, val1, val2));
    }

    public Qe isnull(String column) {
        return append(ISNULL.sql(column, ""));
    }

    public Qe notnull(String column) {
        append(NOTNULL.sql(column, ""));
        return this;
    }

    public Qe like(String column, Object val) {
        return append(LIKE.sql(column, val));
    }

    public Qe and(Qe qe) {
        return append(AND.of(qe.sql));
    }

    public Qe or(Qe qe) {
        append(OR.of(qe.sql));
        return this;
    }

    public Qe asc(Object... val) {
        if (0<order.length()) order.append(" ,");
        order.append(ASC.of(val));
        return this;
    }

    public Qe desc(Object... val) {
        if (0<order.length()) order.append(" ,");
        order.append(DESC.of(val));
        return this;
    }

    public Qe group(Object... val) {
        this.group.append(GROUP.of(val));
        return this;
    }

    public Qe having(String column,Opt opt,Object val) {
        this.having.append(" HAVING (");
        this.having.append(column);
        this.having.append(opt.key());
        this.having.append(val);
        this.having.append(")");
        return this;
    }

    public String limit(Object val1, Object val2) {
        return LIMIT.between(build(), val1, val2);
    }

    // ====== 集成查询方法 BEGIN ======
    public T to() {
        List<T> list = sqlManager.execute(new SQLReady(build()), this.clz);
        return C.notEmpty(list)?list.get(0):null;

    }
    public <CLZ> CLZ to(Class<CLZ> tClass) {
        List<CLZ> list = sqlManager.execute(new SQLReady(build()), tClass);
        return C.notEmpty(list)?list.get(0):null;
    }

    public <CLZ> List<CLZ> toList() {
        return toList(this.clz);
    }


    public <CLZ> List<CLZ> toList(Class<CLZ> tClass) {
        List<CLZ> list = sqlManager.execute(new SQLReady(build()), tClass);
        return C.notEmpty(list)?list:new ArrayList<>();
    }

    public <CLZ> Page<CLZ> toPage(Page page) {
        return toPage(this.clz,page);
    }

    /**
     * 返回 PAGE 查询结果集
     * 注意不要传入 limit 语句
     * @param clz  返回的class
     * @param page
     * @param <CLZ>
     * @return
     */
    public <CLZ> Page<CLZ> toPage(Class<CLZ> clz, Page page) {
        PageQuery<CLZ> pq = page.myPageToPageQuery(page,new PageQuery());
        pq = sqlManager.execute(new SQLReady(build()), clz, pq);
        return page.pageQueryToMyPage(pq,page);
    }
    // ====== 集成查询方法 END ======



    public enum Opt {
        EQ() {
            @Override
            public String key() {
                return "=";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s = '%s') ";
                return String.format(expr, k, val);
            }
        },

        NE() {
            @Override
            public String key() {
                return "!=";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s != '%s') ";
                return String.format(expr, k, val);
            }
        },
        LT() {
            @Override
            public String key() {
                return "<";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s < '%s') ";
                return String.format(expr, k, val);
            }
        },
        GT() {
            @Override
            public String key() {
                return ">";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s > '%s') ";
                return String.format(expr, k, val);
            }
        },
        LE() {
            @Override
            public String key() {
                return "<=";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s <= '%s') ";
                return String.format(expr, k, val);
            }
        },
        GE() {
            @Override
            public String key() {
                return ">=";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s >= '%s') ";
                return String.format(expr, k, val);
            }
        },
        IN() {
            @Override
            public String key() {
                return "IN";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s IN (%s)) ";
                return String.format(expr, k, val);
            }
        },
        ISNULL() {
            @Override
            public String key() {
                return "IS NULL";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s IS NULL) ";
                return String.format(expr, k);
            }
        },
        NOTNULL() {
            @Override
            public String key() {
                return "IS NOT NULL";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (%s IS NOT NULL) ";
                return String.format(expr, k);
            }
        },
        BETWEEN() {
            @Override
            public String key() {
                return "BETWEEN";
            }

            @Override
            public String between(Object k, Object val1, Object val2) {
                if (val1 instanceof Date) {
                    val1 = ((Date) val1).getTime();
                }
                if (val2 instanceof Date) {
                    val2 = ((Date) val2).getTime();
                }
                String expr = " (%s between '%s' and '%s') ";
                return String.format(expr, k, val1, val2);
            }
        },
        LIKE() {
            @Override
            public String key() {
                return "LIKE";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " (LOCATE(%s,%s)) ";
                return String.format(expr, val, k);
            }
        },
        SELECT() {
            @Override
            public String of(Object... val) {
                StringBuffer framSql = new StringBuffer( " SELECT ");
                int i = 0;
                while (i < val.length) {
                    framSql.append(val[i]);
                    framSql.append(",");
                    i++;
                }
                return framSql.deleteCharAt(framSql.length() - 1).toString();
            }
        },
        DEL() {
            @Override
            public String of(Object... val) {
                String expr = " DELETE FROM %s ";
                return String.format(expr, val);
            }
        },

        FROM() {
            @Override
            public String sql(Object val1, Object val2) {
                String expr = " FROM %s AS %s ";
                return String.format(expr, val1, val2);
            }
        },
        AS() {
            @Override
            public String sql(Object val1, Object val2) {
                String expr = " %s AS %s ";
                return String.format(expr, val1, val2);
            }
        },
        WHERE() {
            @Override
            public String of(Object... val1) {
                String expr = " WHERE (%s)";
                return String.format(expr, val1);
            }
        },
        LEFTJOIN() {
            @Override
            public String of(Object... val1) {
                String expr = " LEFT JOIN %s ";
                return String.format(expr, val1);
            }
        },
        ON() {
            @Override
            public String on(String table1, Object val1,String table2, Object val2) {
                String expr = " ON(%s.%s = %s.%s)";
                return String.format(expr, table1, val1, table2, val2);
            }
        },
        ORDER() {
            @Override
            public String of(Object... val) {
                String expr = " ORDER BY %s ";
                return String.format(expr, val);
            }
        },
        GROUP() {
            @Override
            public String of(Object... val) {
                String expr = " GROUP BY %s ";
                return String.format(expr, val);
            }
        },
        HAVING() {
            @Override
            public String between(Object k, Object opt, Object val) {
                String expr = " (%s %s %s)";
                return String.format(expr, k, opt, val);
            }
        },
        ASC() {
            @Override
            public String of(Object... val) {
                StringBuffer framg = new StringBuffer();
                int i = 0;
                while (i < val.length) {
                    framg.append(" %s ASC");
                    if (i < val.length - 1) framg.append(", ");
                    i++;
                }
                return String.format(framg.toString(), val);
            }
        },
        DESC() {
            @Override
            public String of(Object... val) {
                StringBuffer framg = new StringBuffer();
                int i = 0;
                while (i < val.length) {
                    framg.append(" %s DESC");
                    if (i < val.length - 1) framg.append(", ");
                    i++;
                }
                return String.format(framg.toString(), val);
            }
        },
        LIMIT() {
            @Override
            public String between(Object k, Object val1, Object val2) {
                String expr = " %s LIMIT %s, %s";
                return String.format(expr, k, val1, val2);
            }
        },
        AND() {
            @Override
            public String of(Object... val) {
                String expr = "  AND (%s) ";
                return String.format(expr, val);
            }
        },
        OR() {
            @Override
            public String of(Object... val) {
                String expr = " OR (%s) ";
                return String.format(expr, val);
            }
        };

        public String key() {
            return "";
        }

        public String of(Object... k) {
            return "";
        }

        public String sql(Object k, Object val) {
            return "";
        }

        public String between(Object k, Object val1, Object val2) {
            return "";
        }

        public String on(String table1, Object val1, String table2, Object val2) {
            String expr = " ON(%1.%2 = %4.%5) ";
            return String.format(expr, table1, val1, table2, val2);
        }

    }


    public static void main(String[] args) throws Exception {
        String sql = new Qe(SysUser.class)
                .select(SysUser.Dao.userName, SysUser.Dao.deptId, SysDept.Dao.deptName)
                .leftJoin(SysDept.class, SysDept.Dao.id, SysUser.Dao.deptId)
                .where(new Qe().like(SysUser.Dao.deptId, 1))
                .asc(SysUser.Dao.deptId)
                .desc(SysUser.Dao.userName)
                .group(SysUser.Dao.deptId)
                .having(SysUser.Dao.deptId, Opt.GT, 0)
                .build();

        System.out.println("sql=" + sql);

        String and_sql = new Qe().eq(SysUser.Dao.userName, "linton").and(new Qe().eq(SysUser.Dao.userName, "alice")).sql();
        System.out.println("and_sql=" + and_sql);


        String or = new Qe().eq(SysUser.Dao.deptId, 1).or(new Qe().eq(SysUser.Dao.deptId, 2)).sql();
        System.out.println("or=" + or);


        String between = new Qe().between(SysUser.Dao.createDate, new Date(), new Date()).build();
        System.out.println("between=" + between);

        String del = Qe.del(SysUser.Dao.table).where(new Qe().eq(SysUser.Dao.deptId, 1)).build();

        System.out.println("del= " + del);

    }

}
