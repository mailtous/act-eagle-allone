package com.artlongs.framework.utils;

import com.artlongs.framework.page.Page;
import com.artlongs.sys.model.SysDept;
import com.artlongs.sys.model.SysUser;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.engine.PageQuery;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.util.C;
import org.osgl.util.S;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.artlongs.framework.utils.Qe.Opt.*;

/**
 * Function: 查询表达式
 *
 * @Autor: leeton
 * @Date : 2/27/18
 */
public class Qe<T> {
    private Logger logger = L.get(this.getClass());
    protected Integer id = 0;
    protected SQLManager sqlManager;
    protected Class clz;
    protected String mainTableName = "";
    protected String template = "{del}{select}{sum}{max}{min}{casethen}{sumcasethen}{count}{from}{where}{subselect}{group}{having}{order}";
    protected StringBuffer del = new StringBuffer(32);
    protected StringBuffer count = new StringBuffer(32);
    protected StringBuffer sum = new StringBuffer(32);
    protected StringBuffer max = new StringBuffer(32);
    protected StringBuffer min = new StringBuffer(32);
    protected StringBuffer casethen = new StringBuffer(32);
    protected StringBuffer sumcasethen = new StringBuffer(32);
    protected StringBuffer select = new StringBuffer(32);
    protected StringBuffer subselect = new StringBuffer(128);
    protected StringBuffer from = new StringBuffer(32);
    protected StringBuffer where = new StringBuffer(196);
    protected StringBuffer group = new StringBuffer(32);
    protected StringBuffer having = new StringBuffer(64);
    protected StringBuffer order = new StringBuffer(32);
    protected StringBuffer limit = new StringBuffer(32);
    protected String symbolsql = "";  // 还没有设值的 sql
    protected Map<String, Object> params = new HashMap<>(9);
    protected boolean checkSqlHack = true;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String link;

    public Qe() {
    }

    public Qe(Class<T> clz) {
        init(clz, getTableName(clz), null);
    }

    public Qe(String mainTableName) {
        init(null, mainTableName, null);
    }

    public Qe(Class<T> clz, SQLManager sqlManager) {
        init(clz, getTableName(clz), sqlManager);
    }

    protected Qe<T> init(Class<T> clz, String mainTableName, SQLManager sqlManager) {
        this.id = hashCode();
        this.clz = clz;
        this.mainTableName = mainTableName;
        this.sqlManager = sqlManager;
        this.from.append(FROM.symbol).append(mainTableName).append(" AS ").append(mainTableName).append(" ");
        return this;
    }

    public static String getTableName(Class clz) {
        return new UnderlinedNameConversion().getTableName(clz);
    }

    public String count() {
        this.count.append("SELECT COUNT(1)");
        return build();
    }

    public Qe<T> select(String... vals) {
        if (vals.length > 0) {
            for (String v : vals) {
                selectAs(mainTableName, v, null);
            }
        }
        return this;
    }

    public Qe<T> select(Class otherClz, String... vals) {
        if (vals.length > 0) {
            for (String v : vals) {
                selectAs(getTableName(otherClz), v, null);
            }
        }
        return this;
    }

    public Qe<T> selectAs(Class otherClz, String v, String as) {
        selectAs(getTableName(otherClz), v, as);
        return this;
    }

    public Qe<T> selectAs(String column, String as) {
        return selectAs(mainTableName, column, as);
    }

    private Qe<T> selectAs(String table, String column, String as) {
        if (this.select.length() == 0) {
            this.select.append(SELECT.symbol);
        }
        this.select.append(table).append(".").append(column);
        if (null != as && "" != as) {
            this.select.append(AS.symbol).append(as);
        }
        this.select.append(",");
        return this;
    }

    public static class SubSelect<T> extends Qe {
        private Class clz;
        private String mainTableName = "";

        public SubSelect(Class clz) {
            this.clz = clz;
            this.mainTableName = getTableName(clz);
            super.init(clz, mainTableName, null);
        }
    }

    public Qe<T> sum(String column, Class... otherClz) {
        if (this.select.length() == 0 && this.sum.length() == 0) {
            this.sum.append("SELECT ");
        }
        if(select.length()>0 && sum.length() ==0){
            sum.append(" ,");
        }
        this.sum.append(" SUM(");
        this.sum.append(getTablePrev(otherClz)).append(".").append(column).append(")").append(",");
        return this;
    }

    public Qe<T> sumAs(String column, String as, Class... otherClz) {
        if (this.select.length() == 0 && this.sum.length() ==0) {
            this.sum.append("SELECT ");
        }
        if(select.length()>0 && sum.length() ==0){
            sum.append(" ,");
        }
        this.sum.append(" SUM(");
        this.sum.append(getTablePrev(otherClz)).append(".").append(column).append(") AS ").append(as).append(",");
        return this;
    }

    public Qe<T> sumCase(String caseField, Object eqVal, String sumField, String asFiled, Class... otherClz) {
        if (select.length() == 0 && sumcasethen.length() == 0) {
            sumcasethen.append("SELECT ");
        }
        if(select.length()>0 && sumcasethen.length() ==0){
            sumcasethen.append(" ,");
        }
        sumcasethen.append(" SUM(IFNULL(");
        String table = getTablePrev(otherClz);
        sumcasethen.append("CASE ")
                .append(table).append(".").append(caseField)
                .append(" WHEN ").append(eqVal)
                .append(" THEN ")
                .append(table).append(".").append(sumField)
                .append(" END, 0)) AS ").append(asFiled)
                .append(",");
        return this;
    }

    public Qe<T> caseAs(String caseField, Object eqVal, String targetField, String asFiled, Class... otherClz) {
        if (select.length() == 0 && casethen.length() == 0) {
            casethen.append("SELECT ");
        }
        if(select.length()>0 && casethen.length() ==0){
            casethen.append(" ,");
        }
        casethen.append(" (CASE ");
        String table = getTablePrev(otherClz);
        casethen.append(table).append(".").append(caseField)
                .append(" WHEN ").append(eqVal)
                .append(" THEN ")
                .append(table).append(".").append(targetField)
                .append(" END) AS ").append(asFiled)
                .append(",");
        return this;
    }

    private Qe del() {
        this.del.append(DEL.symbol).append(FROM.symbol).append(mainTableName);
        return this;
    }

    public Qe<T> leftJoin(String joinTableName, String joinTableKey, String mainTableKey) {
        if (S.blank(this.mainTableName)) throw new RuntimeException("主表不能为空。");
        this.from
                .append(LEFTJOIN.symbol)
                .append(joinTableName)
                .append(ON.symbol)
                .append(this.mainTableName)
                .append(".")
                .append(mainTableKey)
                .append(" = ")
                .append(joinTableName)
                .append(".")
                .append(joinTableKey);
        return this;
    }

    public Qe<T> leftJoin(Class<T> clz, String joinTableKey, String mainTableKey) {
        return leftJoin(getTableName(clz), joinTableKey, mainTableKey);
    }

    public Qe<T> leftJoin(Class<T> clz,String mainTableKey) {
        return leftJoin(getTableName(clz), "id", mainTableKey);
    }

    public Qe<T> and(Qe<T>... manyQe){
        addManyCondition(Opt.AND.symbol,manyQe);
        return this;
    }

    public Qe<T> or(Qe<T>... manyQe){
        addManyCondition(Opt.OR.symbol,manyQe);
        return this;
    }
    // =============== AND CONDITION==============================
    public Qe<T> andEq(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.EQ, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andNe(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.NE, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andLt(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.LT, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andGt(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.GT, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andLe(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.LE, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andGe(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.GE, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andIn(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.IN, column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andBetween(String column, Object v1, Object v2, Class... otherClz) {
        addBetween(column, v1, v2, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andLike(String column, Object val, Class... otherClz) {
        addLike(column, val, AND.name(), otherClz);
        return this;
    }

    public Qe<T> andIsnull(String column, Class... otherClz) {
        this.where.append(AND.symbol).append(column).append(ISNULL.symbol);
        return this;
    }

    public Qe<T> andNotnull(String column, Class... otherClz) {
        this.where.append(AND.symbol).append(column).append(NOTNULL.symbol);
        return this;
    }

    // =============== OR CONDITION ==============================
    public Qe<T> orEq(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.EQ, column, val, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orNe(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.NE, column, val, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orLt(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.LT, column, val, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orGt(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.GT, column, val, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orLe(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.LE, column, val, OR.name());
        return this;
    }

    public Qe<T> orGe(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.GE, column, val, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orIn(String column, Object val, Class... otherClz) {
        addWhereSql(Opt.IN, column, val, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orBetween(String column, Object v1, Object v2, Class... otherClz) {
        addBetween(column, v1, v2, OR.name(), otherClz);
        return this;
    }

    public Qe<T> orLike(String column, Object val, Class... otherClz) {
        addLike(column, val, OR.symbol, otherClz);
        return this;
    }

    public Qe<T> orIsnull(String column, Class... otherClz) {
        String table = getTablePrev(otherClz);
        this.where.append(OR.symbol).append(table).append(".").append(column).append(ISNULL.symbol);
        return this;
    }

    public Qe<T> orNotnull(String column, Class... otherClz) {
        String table = getTablePrev(otherClz);
        this.where.append(OR.symbol).append(table).append(".").append(column).append(NOTNULL.symbol);
        return this;
    }

    public Qe<T> asc(Object... val) {
        addOrderBy(ASC.symbol, val);
        return this;
    }

    public Qe<T> desc(Object... val) {
        addOrderBy(DESC.symbol, val);
        return this;
    }

    private void addOrderBy(String opt, Object... val) {
        if (order.length() == 0) {
            order.append(ORDER.symbol);
        }
        for (Object v : val) {
            order.append(v).append(opt).append(",");
        }
    }

    public Qe<T> group(Object... val) {
        this.group.append(GROUP.symbol);
        for (Object v : val) {
            group.append(v).append(",");
        }
        group.deleteCharAt(group.length() - 1);
        return this;
    }

    public Qe<T> having(String column, Opt opt, Object val) {
        if (this.group.length() > 0) {
            this.having.append(HAVING.symbol)
                    .append("(")
                    .append(column)
                    .append(opt.symbol)
                    .append(val)
                    .append(")");
        }
        return this;
    }

    public String limit(int val1, int val2) {
        this.limit.append(LIMIT).append(val1).append(",").append(val2);
        return build();
    }

    // ====== 集成查询方法 BEGIN ======
    public T to() {
        List<T> list = sqlManager.execute(new SQLReady(build()), this.clz);
        return C.notEmpty(list) ? list.get(0) : null;

    }

    public <CLZ> CLZ to(Class<CLZ> tClass) {
        List<CLZ> list = sqlManager.execute(new SQLReady(build()), tClass);
        return C.notEmpty(list) ? list.get(0) : null;
    }

    public <CLZ> List<CLZ> toList() {
        return toList(this.clz);
    }


    public <CLZ> List<CLZ> toList(Class<CLZ> tClass) {
        List<CLZ> list = sqlManager.execute(new SQLReady(build()), tClass);
        return C.notEmpty(list) ? list : new ArrayList<>();
    }

    public <CLZ> Page<CLZ> toPage(Page page) {
        return toPage(this.clz, page);
    }

    public boolean toDel() {
       return sqlManager.executeUpdate(new SQLReady(del().build()))>0;
    }

    /**
     * 返回 PAGE 查询结果集
     * 注意不要传入 limit 语句
     *
     * @param clz   返回的class
     * @param page
     * @param <CLZ>
     * @return
     */
    public <CLZ> Page<CLZ> toPage(Class<CLZ> clz, Page page) {
        PageQuery<CLZ> pq = page.myPageToPageQuery(page, new PageQuery());
        pq = sqlManager.execute(new SQLReady(build()), clz, pq);
        return page.pageQueryToMyPage(pq, page);
    }
    // ====== 集成查询方法 END ======

    private void addWhereSql(Opt opt, String k, Object val, String link, Class... otherClz) {
        this.link = link;
        if (null == val || "".equals(val)) return;
        if (this.where.length() > 0) {
            this.where.append(link);
        }
        StringBuffer key = new StringBuffer().append(":").append(link).append(opt.name()).append("_").append(k);
        String table = getTablePrev(otherClz);
        this.where.append("(")
                .append(table)
                .append(".")
                .append(k)
                .append(opt.symbol)
                .append(key)
                .append(") ");
        //
        val = buildSubSelect(val);
        //
        addParams(key.toString(), val);
    }

    private void addManyCondition(String link,Qe... manyQe ) {
        this.where.append(link).append("(");
        if (manyQe.length > 0) {
            for (int i = 0; i < manyQe.length; i++) {
                Qe qe = manyQe[i];
                String sql = settingParams(qe.where.toString(), qe.params);
                if (i == 0) {
                    sql = sql.replace("AND(", "(").replace("OR(", "(");
                }
                if (i > 0) {
                    if (sql.indexOf("AND(") == -1 && sql.indexOf("OR(") == -1) {
                        this.where.append(qe.link).append(sql);
                        continue;
                    }
                }
                this.where.append(sql);
            }
        }
        this.where.append(") ");
    }

    public Qe<T> condition(){
        Qe q = new Qe(this.clz);
        q.clear();
        q.where = new StringBuffer(" ");
        return q;
    }
    public Qe<T> c(){
        return condition();
    }

    private Object buildSubSelect(Object val) {
        if (val instanceof SubSelect) {
            this.checkSqlHack = false;
            return "(" + ((SubSelect) val).build() + ")";
        }
        return val;
    }

    private String getTablePrev(Class[] otherClz) {
        String table = mainTableName;
        if (null != otherClz && otherClz.length > 0) {
            table = getTableName(otherClz[0]);
        }
        return table;
    }

    private void addParams(String key, Object v1) {
        this.params.put(key, v1);
    }

    private void addBetween(String column, Object v1, Object v2, String link, Class... otherClz) {
        this.link = link;
        if (null == v1 || "".equals(v1)) return;
        if (null == v2 || "".equals(v2)) return;
        StringBuffer key1 = new StringBuffer().append(":").append(link).append("_between_").append(column).append("_v1");
        StringBuffer key2 = new StringBuffer().append(":").append(link).append("_between_").append(column).append("_v2");
        String table = getTablePrev(otherClz);
        if (this.where.length() > 0) {
            this.where.append(link);
        }
        this.where.append("(")
                .append(table).append(".").append(column)
                .append(BETWEEN.symbol)
                .append(key1).append(" AND ").append(key2);
        this.where.append(")");
        addParams(key1.toString(), v1);
        addParams(key2.toString(), v2);
        key1 = null;
        key2 = null;
    }

    private void addLike(String column, Object val, String link, Class... otherClz) {
        this.link = link;
        if (null == val || "".equals(val)) return;
        if (this.where.length() > 0) {
            this.where.append(link);
        }
        String table = getTablePrev(otherClz);
        StringBuffer key = new StringBuffer().append(":").append(link).append("_like_").append(column);
        this.where.append("(")
                .append(LIKE.symbol).append("(")
                .append(table).append(".").append(column)
                .append(",")
                .append(key)
                .append(")>0) ");
        addParams(key.toString(), val);
        key = null;
    }

    public enum Opt {
        EQ("="),
        NE("!="),
        LT("<"),
        GT(">"),
        LE("<="),
        GE(">="),
        IN(" IN "),
        ISNULL(" IS NULL"),
        NOTNULL(" IS NOT NULL"),
        BETWEEN(" BETWEEN "),
        LIKE("LOCATE "),
        SELECT("SELECT "),
        FROM(" FROM "),
        LEFTJOIN(" LEFT JOIN "),
        ON(" ON "),
        WHERE(" WHERE "),
        DEL("DELETE "),
        AS(" AS "),
        ORDER(" ORDER BY "),
        GROUP(" GROUP BY "),
        HAVING(" HAVING "),
        ASC(" ASC"),
        DESC(" DESC"),
        LIMIT(" LIMIT "),
        AND(" AND "),
        OR(" OR ");

        public String symbol;

        Opt(String symbol) {
            this.symbol = symbol;
        }
    }

    private String changeValOfType(Object val) {
        if (null == val) return "";
        Collection list = new ArrayList();
        StringBuffer v = new StringBuffer();
        if (val.getClass().isArray()) {
            Object[] vv = (Object[]) val;
            v.append("(");
            for (Object o : vv) {
                v.append("'").append(o).append("'");
                v.append(",");
            }
            v.deleteCharAt(v.length() - 1);
            v.append(")");
        }
        if (val instanceof Collection) {
            list = (Collection) val;
            v.append("(");
            for (Object o : list) {
                v.append("'").append(o).append("'");
                v.append(",");
            }
            v.deleteCharAt(v.length() - 1);
            v.append(")");
        }

        if (val instanceof Date) {
            v.append("'").append(sdf.format(val)).append("'");
        }

        if (v.length() == 0) {
            v.append(val);
        }
        return v.toString();
    }

    private boolean isSQLHack(String v) {
        if (checkSqlHack) {
            if (v.toLowerCase().contains("select")) return true;
            if (v.toLowerCase().contains("from")) return true;
            if (v.toLowerCase().contains("where")) return true;
        }
        return false;
    }

    private StringBuffer buildWhereSql() {
        if (where.length() > 0) {
            where.insert(0, "(");
            where.insert(0, WHERE.symbol);
            where.append(")");
        }
        return where;
    }

    public String build() {
        String sql = this.template;
        sql = replaceTag(sql, "{del}", del);
        sql = replaceTag(sql, "{count}", count);
        sql = replaceTag(sql, "{select}", buildSelect());
        sql = replaceTag(sql, "{from}", from);
        sql = replaceTag(sql, "{where}", buildWhereSql());
        sql = replaceTag(sql, "{subselect}", subselect);
        sql = replaceTag(sql, "{sum}", sum);
        sql = replaceTag(sql, "{max}", max);
        sql = replaceTag(sql, "{min}", min);
        sql = replaceTag(sql, "{casethen}", casethen);
        sql = replaceTag(sql, "{sumcasethen}", sumcasethen);
        sql = replaceTag(sql, "{group}", group);
        sql = replaceTag(sql, "{having}", having);
        sql = replaceTag(sql, "{order}", order);
        sql = replaceTag(sql, "{limit}", limit);

        this.symbolsql = sql;
        sql = settingParams(sql);
        clear();
        return sql;
    }

    private StringBuffer buildSelect(){
        if (this.select.length() == 0) {
            this.select.append(" SELECT * ");
        }
        return this.select;
    }

    private String replaceTag(String sql, String tag, StringBuffer content) {
        if (content.length() > 0) {
            if (String.valueOf(content.charAt(content.length() - 1)).equals(",")) {
                content.deleteCharAt(content.length() - 1);
            }
            sql = sql.replace(tag, content);
        } else {
            sql = sql.replace(tag, "");
        }
        return sql;
    }

    private String settingParams(String symbolsql) {
       return settingParams(symbolsql, this.params);
    }

    private String settingParams(String symbolsql,Map<String,Object> params) {
        for (String key : params.keySet()) {
            String v = changeValOfType(params.get(key));
            if (isSQLHack(v)) {
                logger.error("Warn find SQL HACK :" + v);
                continue;
            }
            symbolsql = symbolsql.replace(key, v);
        }
        return symbolsql;
    }

    private void clear() {
        del = new StringBuffer();
        count = new StringBuffer();
        sum = new StringBuffer();
        max = new StringBuffer();
        min = new StringBuffer();
        casethen = new StringBuffer();
        sumcasethen = new StringBuffer();
        select = new StringBuffer();
        subselect = new StringBuffer();
        from = new StringBuffer();
        where = new StringBuffer();
        group = new StringBuffer();
        having = new StringBuffer();
        order = new StringBuffer();
        limit = new StringBuffer();
        link = "";
    }
    public static void main(String[] args) throws Exception {
        Qe qe = new Qe(SysUser.class);
        String sql = qe
                .select("user_uame")
//                .andIn("dept_id", new SubSelect(SysDept.class).select("id").andGt("id", 0))
//                .sum("id", SysDept.class)
//                .sumCase(SysDept.Dao.id, 1, SysDept.Dao.id, "did")
//                .caseAs(SysDept.Dao.id, 1, SysDept.Dao.id, "depid")
                .leftJoin(SysDept.class,"dept_id")
//                .andLike(SysUser.Dao.deptId, 1)
//                .andIn(SysUser.Dao.deptId, new Integer[]{1, 2, 3})
//                .andBetween(SysUser.Dao.createDate, new Date(), new Date())
                .andEq("dept_id",8)
                .or(qe.condition().andIn("dept_id", new Integer[]{1, 2, 3}),new Qe(SysDept.class).orEq("dept_id",5))

                .group("dept_id")
                .having("dept_id", Opt.GT, 0)
                .asc("dept_id")
                .desc("user_uame")
                .build();

        System.out.println("sql=" + sql);

     /*   String and_sql = new Qe().eq(SysUser.Dao.userName, "linton").and(new Qe<SysUser>().eq(SysUser.Dao.userName, "alice")).sql();
        System.out.println("and_sql=" + and_sql);


        String or = new Qe().eq(SysUser.Dao.deptId, 1).or(new Qe().eq(SysUser.Dao.deptId, 2)).sql();
        System.out.println("or=" + or);
*/

/*
        String between = new Qe().andBetween(SysUser.Dao.createDate, new Date(), new Date()).build();
        System.out.println("between=" + between);
*/

        String del = new Qe(SysUser.class).del().andEq("dept_id", 1).build();
        System.out.println("del= " + del);

        String count = new Qe(SysUser.class).andEq("dept_id", 1).count();
        System.out.println("count= " + count);

    }

}
