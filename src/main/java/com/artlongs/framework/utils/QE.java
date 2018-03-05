package com.artlongs.framework.utils;

import act.Act;
import com.artlongs.sys.model.SysDept;
import com.artlongs.sys.model.SysUser;
import org.osgl.util.S;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.artlongs.framework.utils.QE.Opt.*;

/**
 * Function: 查询表达式
 *
 * @Autor: leeton
 * @Date : 2/27/18
 */
public class QE {

    public QE() {
    }

    public QE(String key) {
        k = key;
    }

    private static String k;
    private Integer id = 0;
    private StringBuffer sql = new StringBuffer();
    private StringBuffer order = new StringBuffer();

    public String k() {
        return k;
    }

    public String sql() {
        return this.sql.toString();
    }

    public QE select(String... v) {
        sql.append(SELECT.of(v));
        return this;
    }
    public static QE selectAll() {
        QE qe = new QE();
        qe.sql.append(SELECT.of("*"));
        return qe;
    }

    public static QE del(String table) {
        QE qe = new QE();
        qe.sql.append(DEL.of(table));
        return qe;
    }

    public QE from(String table) {
        sql.append(FROM.sql(table, table));
        return this;
    }


    public QE where(QE qe) {
        sql.append(WHERE.of(qe.sql()));
        return this;
    }

    public QE leftJoin(String table) {
        sql.append(LEFTJOIN.of(table));
        return this;
    }

    public QE on(String table1, String v1, Opt opt, String table2, String v2) {
        sql.append(ON.on(table1, v1, opt, table2, v2));
        return this;
    }

    public static QE k(String key) {
        return new QE(key);
    }

    public QE eq(Object v) {
        sql.append(EQ.sql(k, v));
        return this;
    }

    public QE ne(Object v) {
        sql.append(NE.sql(k, v));
        return this;
    }

    public QE lt(Object v) {
        sql.append(LT.sql(k, v));
        return this;
    }

    public QE gt(Object v) {
        sql.append(GT.sql(k, v));
        return this;
    }

    public QE le(Object v) {
        sql.append(LE.sql(k, v));
        return this;
    }

    public QE ge(Object v) {
        sql.append(GE.sql(k, v));
        return this;
    }

    public QE in(Object v) {
        sql.append(IN.sql(k, v));
        return this;
    }

    public QE between(Object v1, Object v2) {
        sql.append(BETWEEN.between(k, v1, v2));
        return this;
    }

    public QE isnull() {
        sql.append(ISNULL.sql(k, ""));
        return this;
    }

    public QE notnull() {
        sql.append(NOTNULL.sql(k, ""));
        return this;
    }

    public QE like(Object v) {
        sql.append(LIKE.sql(k, v));
        return this;
    }

    public QE likestart(Object v) {
        sql.append(LIKESTART.sql(k, v));
        return this;
    }

    public QE likeend(Object v) {
        sql.append(LIKEEND.sql(k, v));
        return this;
    }

    public QE likeall(Object v) {
        sql.append(LIKEAll.sql(k, v));
        return this;
    }

    public QE and(QE qe) {
        sql.append(AND.of(qe.sql));
        return this;
    }

    public QE or(QE qe) {
        sql.append(OR.of(qe.sql));
        return this;
    }

    public QE orderEnd() {
        sql.append(ORDER.of(this.order));
        return this;
    }

    public QE asc(Object... v) {
        if(S.noBlank(order.toString()))  order.append(" ,");
        order.append(ASC.of(v));
        return this;
    }
    public QE desc(Object... v) {
        if(S.noBlank(order.toString()))  order.append(" ,");
        order.append(DESC.of(v));
        return this;
    }

    public String limit(Object v1, Object v2) {
        return LIMIT.between(this.sql, v1, v2);
    }

    public Integer id(String sql){
        String s = "QE_" + sql;
        Integer id = hash(s.getBytes());
        return id;
    }
    public static int hash(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b : data)
            hash = (hash ^ b) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }


    public enum Opt {
        EQ() {
            @Override
            public String key() {
                return "=";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s = '%s' ";
                return String.format(expr, k, v);
            }
        },

        NE() {
            @Override
            public String key() {
                return "!=";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s != '%s' ";
                return String.format(expr, k, v);
            }
        },
        LT() {
            @Override
            public String key() {
                return "<";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s < '%s' ";
                return String.format(expr, k, v);
            }
        },
        GT() {
            @Override
            public String key() {
                return ">";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s > '%s' ";
                return String.format(expr, k, v);
            }
        },
        LE() {
            @Override
            public String key() {
                return "<=";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s <= '%s' ";
                return String.format(expr, k, v);
            }
        },
        GE() {
            @Override
            public String key() {
                return ">=";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s >= '%s' ";
                return String.format(expr, k, v);
            }
        },
        IN() {
            @Override
            public String key() {
                return "IN";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = " %s IN (%s) ";
                return String.format(expr, k, v);
            }
        },
        ISNULL() {
            @Override
            public String key() {
                return "IS NULL";
            }

            @Override
            public String sql(Object k, Object v) {
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
            public String sql(Object k, Object v) {
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
            public String between(Object k, Object v1, Object v2) {
                if (v1 instanceof Date) {
                    v1 = ((Date) v1).getTime();
                }
                if (v2 instanceof Date) {
                    v2 = ((Date) v2).getTime();
                }
                String expr = " (%s between '%s' and '%s') ";
                return String.format(expr, k, v1, v2);
            }
        },
        LIKE() {
            @Override
            public String key() {
                return "LIKE";
            }

            @Override
            public String sql(Object k, Object v) {
                String expr = "( %s like %s )";
                return String.format(expr, k, v);
            }
        },
        LIKESTART() {
            @Override
            public String sql(Object k, Object v) {
                String expr = "( %s like %% %s )";
                return String.format(expr, k, v);
            }
        },
        LIKEEND() {
            @Override
            public String sql(Object k, Object v) {
                String expr = "( %s like %s %% )";
                return String.format(expr, k, v);
            }
        },
        LIKEAll() {
            @Override
            public String sql(Object k, Object v) {
                String expr = "( %s like %% %s %%)";
                return String.format(expr, k, v);
            }
        },
        SELECT() {
            @Override
            public String of(Object... v) {
                StringBuffer framg = new StringBuffer(" SELECT ");
                int i = 0;
                while (i < v.length) {
                    framg.append("%s");
                    if (i < v.length - 1) framg.append(", ");
                    i++;
                }
                return String.format(framg.toString(), v);
            }
        },
        DEL() {
            @Override
            public String of(Object... v) {
                String expr = " DELETE FROM %s ";
                return String.format(expr, v);
            }
        },

        FROM() {
            @Override
            public String sql(Object v1, Object v2) {
                String expr = " FROM %s AS %s ";
                return String.format(expr, v1, v2);
            }
        },
        AS() {
            @Override
            public String sql(Object v1, Object v2) {
                String expr = " %s AS %s ";
                return String.format(expr, v1, v2);
            }
        },
        WHERE() {
            @Override
            public String of(Object... v1) {
                String expr = " WHERE (%s)";
                return String.format(expr, v1);
            }
        },
        LEFTJOIN() {
            @Override
            public String of(Object... v1) {
                String expr = " LEFT JOIN %s ";
                return String.format(expr, v1);
            }
        },
        ON() {
            @Override
            public String on(String table1, Object v1, Opt opt, String table2, Object v2) {
                String expr = " ON(%s.%s %s %s.%s)";
                return String.format(expr, table1, v1, opt.key(), table2, v2);
            }
        },
        ORDER() {
            @Override
            public String of(Object... v) {
                String expr = " ORDER BY %s ";
                return String.format(expr, v);
            }
        },
        ASC() {
            @Override
            public String of(Object... v) {
                StringBuffer framg = new StringBuffer();
                int i = 0;
                while (i < v.length) {
                    framg.append(" %s ASC");
                    if (i < v.length - 1) framg.append(", ");
                    i++;
                }
                return String.format(framg.toString(), v);
            }
        },
        DESC() {
            @Override
            public String of(Object... v) {
                StringBuffer framg = new StringBuffer();
                int i = 0;
                while (i < v.length) {
                    framg.append(" %s DESC");
                    if (i < v.length - 1) framg.append(", ");
                    i++;
                }
                return String.format(framg.toString(), v);
            }
        },
        LIMIT() {
            @Override
            public String between(Object k, Object v1, Object v2) {
                String expr = " %s LIMIT %s, %s";
                return String.format(expr, k, v1, v2);
            }
        },
        AND() {
            @Override
            public String of(Object...v) {
                String expr = "  AND (%s) ";
                return String.format(expr,v);
            }
        },
        OR() {
            @Override
            public String of(Object...v) {
                String expr = " OR (%s) ";
                return String.format(expr, v);
            }
        };

        public String key() {
            return "";
        }

        public String of(Object... k) {
            return "";
        }

        public String sql(Object k, Object v) {
            return "";
        }

        public String between(Object k, Object v1, Object v2) {
            return "";
        }

        public String on(String table1, Object v1, Opt opt, String table2, Object v2) {
            String expr = " ON(%1.%2 %3 %4.%5) ";
            return String.format(expr, table1, v1, opt.key(), table2, v2);
        }

    }


    public static void main(String[] args) throws Exception {
        String sql = new QE()
                .select(SysUser.Dao.userName, SysUser.Dao.deptId, SysDept.Dao.deptName)
                .from(SysUser.Dao.table)
                .leftJoin(SysDept.Dao.table)
                .on(SysUser.Dao.table, SysUser.Dao.deptId, Opt.EQ, SysDept.Dao.table, SysDept.Dao.id)
                .where(
                        QE.k(SysUser.Dao.deptId).eq(1).or(QE.k(SysUser.Dao.deptId).eq(2))
                )
                .asc(SysUser.Dao.deptId)
                .desc(SysUser.Dao.userName)
                .orderEnd()
                .limit(0, 1);

        System.out.println("sql=" + sql);

        String and_sql = QE.k(SysUser.Dao.userName).eq("linton").and(QE.k(SysUser.Dao.userName).eq("alice")).sql();
        System.out.println("and_sql=" + and_sql);


        String or = QE.k(SysUser.Dao.deptId).eq(1).or(QE.k(SysUser.Dao.deptId).eq(2)).sql();
        System.out.println("or=" + or);


        String between = QE.k(SysUser.Dao.createDate).between(new Date(), new Date()).sql();
        System.out.println("between=" + between);

        String del = QE.del(SysUser.Dao.table).where(QE.k(SysUser.Dao.deptId).eq(1)).sql();

        System.out.println("del= " + del);

    }

}
