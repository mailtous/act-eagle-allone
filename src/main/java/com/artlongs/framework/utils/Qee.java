package com.artlongs.framework.utils;

import com.artlongs.sys.model.SysDept;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.kit.BeanKit;
import org.osgl.util.S;

import java.util.Date;

import static com.artlongs.framework.utils.Qee.Opt.*;

/**
 * Function: 查询表达式
 *
 * @Autor: leeton
 * @Date : 2/27/18
 */
public class Qee<T> {

    protected Integer id = 0;
    protected Class clz;
    protected String mainTableName = "";
    protected String select = " SELECT * ";
    protected String from = " FROM ";
    protected String having = "";
    protected String sqlTemplate = " {select} {from} {where} {order} {having} ";
    protected StringBuffer sql = new StringBuffer();
    protected StringBuffer order = new StringBuffer();

    public Qee() {
    }

    public Qee(Class<T> mainTableClass) {
        this.clz = mainTableClass;
        this.mainTableName = getTableName(mainTableClass);
        this.from = FROM.sql(mainTableName,mainTableName);
    }

    public Qee(String mainTableName) {
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
                    .replace("{where}", this.sql)
                    .replace("{having}", this.having);

            if(0<this.order.length()){
                sql = sql.replace("{order}", ORDER.of(this.order));
            }else {
                sql = sql.replace("{order}", "");
            }
            if(0<this.having.length()){
                sql = sql.replace("{having}", ORDER.of(this.having));
            }else {
                sql = sql.replace("{having}", "");
            }
        }else {
            sql = this.sql.toString();
        }

        return sql;
    }
    public String sql() {
        return this.sql.toString();
    }

    private Qee<T> append(String sql) {
        this.sql.append(sql);
        return this;
    }

    public Qee select(String... val) {
        if (val.length > 0) {
            this.select = SELECT.of(val);
        }
        return this;
    }

    public static Qee del(String table) {
        Qee qe = new Qee();
        qe.sql.append(DEL.of(table));
        return qe;
    }

    public Qee where(Qee qe) {
        return append(WHERE.of(qe.sql()));
    }

    public Qee leftJoin(String joinTableName, String joinTableKey, String mainTableKey) {
        if (S.blank(this.mainTableName)) throw new RuntimeException("主表不能为空。");
        this.sql.append(LEFTJOIN.of(joinTableName));
        return append(ON.on(this.mainTableName, mainTableKey, joinTableName, joinTableKey));
    }

    public Qee leftJoin(Class<T> clz, String joinTableKey, String mainTableKey) {
        return leftJoin(getTableName(clz), joinTableKey, mainTableKey);
    }

    public Qee eq(String column, Object val) {
        return append(EQ.sql(column, val));
    }

    public Qee ne(String column, Object val) {
        return append(NE.sql(column, val));
    }

    public Qee lt(String column, Object val) {
        return append(LT.sql(column, val));
    }

    public Qee gt(String column, Object val) {
        return append(GT.sql(column, val));
    }

    public Qee le(String column, Object val) {
        return append(LE.sql(column, val));
    }

    public Qee ge(String column, Object val) {
        return append(GE.sql(column, val));
    }

    public Qee in(String column, Object val) {
        return append(IN.sql(column, val));
    }

    public Qee between(String column, Object val1, Object val2) {
        return append(BETWEEN.between(column, val1, val2));
    }

    public Qee isnull(String column) {
        return append(ISNULL.sql(column, ""));
    }

    public Qee notnull(String column) {
        sql.append(NOTNULL.sql(column, ""));
        return this;
    }

    public Qee like(String column, Object val) {
        return append(LIKE.sql(column, val));
    }

    public Qee likestart(String column, Object val) {
        return append(LIKESTART.sql(column, val));
    }

    public Qee likeend(String column, Object val) {
        return append(LIKEEND.sql(column, val));
    }

    public Qee likeall(String column, Object val) {
        return append(LIKEAll.sql(column, val));
    }

    public Qee and(Qee qe) {
        return append(AND.of(qe.sql));
    }

    public Qee or(Qee qe) {
        sql.append(OR.of(qe.sql));
        return this;
    }

    public Qee asc(Object... val) {
        if (0<order.length()) order.append(" ,");
        order.append(ASC.of(val));
        return this;
    }

    public Qee desc(Object... val) {
        if (0<order.length()) order.append(" ,");
        order.append(DESC.of(val));
        return this;
    }

    public String limit(Object val1, Object val2) {
        return LIMIT.between(build(), val1, val2);
    }


    public enum Opt {
        EQ() {
            @Override
            public String key() {
                return "=";
            }

            @Override
            public String sql(Object k, Object val) {
                String expr = " %s = '%s' ";
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
                String expr = " %s != '%s' ";
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
                String expr = " %s < '%s' ";
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
                String expr = " %s > '%s' ";
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
                String expr = " %s <= '%s' ";
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
                String expr = " %s >= '%s' ";
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
                String expr = " %s IN (%s) ";
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
                String expr = " %s IS NULL ";
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
                String expr = " %s IS NOT NULL ";
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
                String expr = "( %s like %s )";
                return String.format(expr, k, val);
            }
        },
        LIKESTART() {
            @Override
            public String sql(Object k, Object val) {
                String expr = "( %s like %% %s )";
                return String.format(expr, k, val);
            }
        },
        LIKEEND() {
            @Override
            public String sql(Object k, Object val) {
                String expr = "( %s like %s %% )";
                return String.format(expr, k, val);
            }
        },
        LIKEAll() {
            @Override
            public String sql(Object k, Object val) {
                String expr = "( %s like %% %s %%)";
                return String.format(expr, k, val);
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
        String sql = new Qee(SysUser.class)
                .select(SysUser.Dao.userName, SysUser.Dao.deptId, SysDept.Dao.deptName)
                .leftJoin(SysDept.class,SysDept.Dao.id,SysUser.Dao.deptId)
                .where(new Qee().eq(SysUser.Dao.deptId, 1))
                .asc(SysUser.Dao.deptId)
                .desc(SysUser.Dao.userName)
                .limit(0, 1);

        System.out.println("sql=" + sql);

        String and_sql = new Qee().eq(SysUser.Dao.userName, "linton").and(new Qee().eq(SysUser.Dao.userName, "alice")).sql();
        System.out.println("and_sql=" + and_sql);


        String or = new Qee().eq(SysUser.Dao.deptId, 1).or(new Qee().eq(SysUser.Dao.deptId, 2)).sql();
        System.out.println("or=" + or);


        String between = new Qee().between(SysUser.Dao.createDate, new Date(), new Date()).build();
        System.out.println("between=" + between);

        String del = Qee.del(SysUser.Dao.table).where(new Qee().eq(SysUser.Dao.deptId, 1)).build();

        System.out.println("del= " + del);

    }

}
