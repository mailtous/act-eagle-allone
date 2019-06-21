package com.artlongs.framework.query;

import com.artlongs.framework.page.Page;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.engine.PageQuery;
import org.osgl.util.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.artlongs.framework.query.Qe.Opt.*;

/**
 * Function: 查询表达式
 *
 * @Autor: leeton
 * @Date : 2/27/18
 */
public class Qe<T> {
    protected static final Logger logger = LoggerFactory.getLogger(Qe.class);
    protected Integer id = 0;
    protected Class<T> clz;
    protected String mainTableName = "";
    protected String template = "{insert}{update}{del}{select}{sum}{casethen}{sumcasethen}{count}{from}{join}{where}{subselect}{group}{having}{order}{limit}";
    protected StringBuffer del = new StringBuffer(32);
    protected StringBuffer count = new StringBuffer(32);
    protected StringBuffer sum = new StringBuffer(32);
    protected StringBuffer casethen = new StringBuffer(32);
    protected StringBuffer sumcasethen = new StringBuffer(32);
    protected StringBuffer select = new StringBuffer(32);
    protected StringBuffer subselect = new StringBuffer(128);
    protected StringBuffer from = new StringBuffer(32);
    protected StringBuffer join = new StringBuffer(196);
    protected StringBuffer where = new StringBuffer(196);
    protected StringBuffer group = new StringBuffer(32);
    protected StringBuffer having = new StringBuffer(64);
    protected StringBuffer order = new StringBuffer(32);
    protected StringBuffer limit = new StringBuffer(32);
    protected StringBuffer update = new StringBuffer(196);
    protected StringBuffer insert = new StringBuffer(196);
    protected String symbolsql = "";  // 还没有设值的 sql
    protected Map<String, Object> params = new HashMap<>(7);
    protected boolean checkSqlHack = true;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private String link;
    //
    private SQLManager sqlManager;

    public Qe() {
    }

    public Qe(Class<T> tClass) {
        init(tClass, getTableName(tClass), null);
    }

    public Qe(String mainTableName) {
        init(null, mainTableName, null);
    }


    public Qe(Class<T> tClass, SQLManager sqlManager) {
        init(tClass, getTableName(tClass), sqlManager);
    }

    public Qe(SQLManager sqlManager) {
        init(null, null, sqlManager);
    }

    protected Qe<T> init(Class<T> tClass, String mainTableName, SQLManager sqlManager) {
        this.id = hashCode();
        this.clz = tClass;
        this.mainTableName = mainTableName;
        this.sqlManager = sqlManager;
        return this;
    }

    public String count() {// 生成原生的 count 语句,不同toCount(),会清除select,casethen
        this.select = new StringBuffer();
        this.casethen = new StringBuffer();
        this.count.append("SELECT COUNT(1) ");
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

    public Qe<T> selectAs(Class otherT, String v, String as) {
        selectAs(getTableName(otherT), v, as);
        return this;
    }

    public Qe<T> selectAs(String column, String as) {
        return selectAs(mainTableName, column, as);
    }

    public Qe<T> select(Attr.Property<T, ?> func, Spell spell) {
        return select(spell, func);
    }

    public Qe<T> select(Spell spell, Attr.Property<T, ?> func) {
        Attr f = new Attr(func);
        String underline_column = f.getColumn();
        String camel_column = f.getName();
        if (underline_column.equals(camel_column)) {
            return selectAs(mainTableName, underline_column, null);
        }
        String as = Spell.getAs(null, spell, underline_column, f.getName());
        selectAs(mainTableName, underline_column, as);
        return this;
    }

    public Qe<T> select(Attr attr, Spell spell) {
        return select(spell, attr);
    }

    public Qe<T> select(Spell spell, Attr attr) {
        String column = attr.getColumn();
        String as = Spell.getAs(null, spell, column, attr.getName());
        selectAs(attr.getTableName(), column, as);
        return this;
    }

    public Qe<T> select(Spell spell, Attr... attrs) {
        for (Attr attr : attrs) {
            String column = attr.getColumn();
            String as = Spell.getAs(null, spell, column, attr.getName());
            selectAs(attr.getTableName(), column, as);
        }
        return this;
    }

    public Qe<T> selectAs(String table, String column, String as) {
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

    public Qe<T> select(Qf... qfs) {
        if (this.select.length() == 0) {
            this.select.append(SELECT.symbol);
        }
        for (Qf qf : qfs) {
            this.select.append(qf.sql).append(",");
        }
        return this;
    }

    public Qe<T> fromSubSelect(Qe<T> subSelect) {
        fromSubSelect(subSelect, subSelect.mainTableName);
        return this;
    }

    public Qe<T> fromSubSelect(Qe<T> subSelect, String as) {
        this.from.append(FROM.symbol).append("(");
        this.from.append(subSelect.build());
        this.from.append(")");
        if (StringKit.isNotBlank(as)) {
            this.from.append(AS.symbol).append(as);
            this.mainTableName = as;
        }
        return this;
    }

    public Qe<T> sum(String column, Class... otherT) {
        return sumAs(column, column, getTablePrev(otherT));
    }

    public Qe<T> sumAs(String column, String as, String... table) {
        if (this.select.length() == 0 && this.sum.length() == 0) {
            this.sum.append("SELECT ");
        }
        if (select.length() > 0 && sum.length() == 0) {
            sum.append(" ,");
        }
        this.sum.append(" SUM(");
        if (null != table && table.length > 0) {
            this.sum.append(table[0]).append(".");
        }
        this.sum.append(column).append(") AS ").append(as).append(",");
        return this;
    }

    public Qe<T> sumCase(String caseField, Object eqVal, String sumField, String asFiled, Class... otherT) {
        if (select.length() == 0 && sumcasethen.length() == 0) {
            sumcasethen.append("SELECT ");
        }
        if (select.length() > 0 && sumcasethen.length() == 0) {
            sumcasethen.append(" ,");
        }
        sumcasethen.append(" SUM(IFNULL(");
        String table = getTablePrev(otherT);
        sumcasethen.append("CASE ")
            .append(table).append(".").append(caseField)
            .append(" WHEN ").append(eqVal)
            .append(" THEN ")
            .append(table).append(".").append(sumField)
            .append(" END, 0)) AS ").append(asFiled)
            .append(",");
        return this;
    }

    public Qe<T> caseAs(String caseField, Object eqVal, String targetField, String asFiled, Class... otherT) {
        if (select.length() == 0 && casethen.length() == 0) {
            casethen.append("SELECT ");
        }
        if (select.length() > 0 && casethen.length() == 0) {
            casethen.append(" ,");
        }
        casethen.append(" (CASE ");
        String table = getTablePrev(otherT);
        casethen.append(table).append(".").append(caseField)
            .append(" WHEN ").append(eqVal)
            .append(" THEN ")
            .append(table).append(".").append(targetField)
            .append(" END) AS ").append(asFiled)
            .append(",");
        return this;
    }

    private Qe delete() {
        this.del.append(DEL.symbol).append(FROM.symbol).append(mainTableName);
        return this;
    }

    public Qe<T> leftJoin(String joinTableName, String joinTableKey, String mainTableKey) {
        if (null == mainTableName || "".equals(mainTableName)) throw new RuntimeException("主表不能为空。");
        this.join
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

    public Qe<T> leftJoin(Class<T> tClass, String joinTableKey, String mainTableKey) {
        return leftJoin(getTableName(tClass), joinTableKey, mainTableKey);
    }

    public Qe<T> leftJoin(Class<T> tClass, String mainTableKey) {
        return leftJoin(getTableName(tClass), "id", mainTableKey);
    }

    // =============== and ==============================

    /**
     * 多重子条件时使用
     */
    public Qe<T> condition() {
        Qe q = new Qe(this.clz);
        q.clear();
        q.where = new StringBuffer(" ");
        return q;
    }

    public Qe<T> c() {
        return condition();
    }

    public Qe<T> and(Qe<T>... manyQe) {
        addManyCondition(Opt.AND.symbol, manyQe);
        return this;
    }

    public Qe<T> or(Qe<T>... manyQe) {
        addManyCondition(Opt.OR.symbol, manyQe);
        return this;
    }

    public Qe<T> andEq(String column, Object val, Class... otherT) {
        addWhereSql(Opt.EQ, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andNe(String column, Object val, Class... otherT) {
        addWhereSql(Opt.NE, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andLt(String column, Object val, Class... otherT) {
        addWhereSql(Opt.LT, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andGt(String column, Object val, Class... otherT) {
        addWhereSql(Opt.GT, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andLe(String column, Object val, Class... otherT) {
        addWhereSql(Opt.LE, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andGe(String column, Object val, Class... otherT) {
        addWhereSql(Opt.GE, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andIn(String column, Object val, Class... otherT) {
        addWhereSql(Opt.IN, column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andBetween(String column, Object v1, Object v2, Class... otherT) {
        addBetween(column, v1, v2, AND.name(), otherT);
        return this;
    }

    public Qe<T> andLike(String column, Object val, Class... otherT) {
        addLike(column, val, AND.name(), otherT);
        return this;
    }

    public Qe<T> andIsnull(String column, Class... otherT) {
        this.where.append(AND.symbol).append(column).append(ISNULL.symbol);
        return this;
    }

    public Qe<T> andNotnull(String column, Class... otherT) {
        this.where.append(AND.symbol).append(column).append(NOTNULL.symbol);
        return this;
    }

    // =============== OR CONDITION ==============================

    public Qe<T> orEq(String column, Object val, Class... otherT) {
        addWhereSql(Opt.EQ, column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orNe(String column, Object val, Class... otherT) {
        addWhereSql(Opt.NE, column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orLt(String column, Object val, Class... otherT) {
        addWhereSql(Opt.LT, column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orGt(String column, Object val, Class... otherT) {
        addWhereSql(Opt.GT, column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orLe(String column, Object val, Class... otherT) {
        addWhereSql(Opt.LE, column, val, OR.name());
        return this;
    }

    public Qe<T> orGe(String column, Object val, Class... otherT) {
        addWhereSql(Opt.GE, column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orIn(String column, Object val, Class... otherT) {
        addWhereSql(Opt.IN, column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orBetween(String column, Object v1, Object v2, Class... otherT) {
        addBetween(column, v1, v2, OR.name(), otherT);
        return this;
    }

    public Qe<T> orLike(String column, Object val, Class... otherT) {
        addLike(column, val, OR.name(), otherT);
        return this;
    }

    public Qe<T> orIsnull(String column, Class... otherT) {
        String table = getTablePrev(otherT);
        this.where.append(OR.symbol).append(table).append(".").append(column).append(ISNULL.symbol);
        return this;
    }

    public Qe<T> orNotnull(String column, Class... otherT) {
        String table = getTablePrev(otherT);
        this.where.append(OR.symbol).append(table).append(".").append(column).append(NOTNULL.symbol);
        return this;
    }

    public Qe<T> asc(String... val) {
        addOrderBy(ASC.symbol, val);
        return this;
    }

    public Qe<T> desc(String... val) {
        addOrderBy(DESC.symbol, val);
        return this;
    }

    private void addOrderBy(String opt, String... val) {
        if (order.length() == 0) {
            order.append(ORDER.symbol);
        }
        for (String v : val) {
            order.append(mainTableName).append(".").append(v).append(opt).append(",");
        }
    }

    public Qe<T> group(String... columns) {
        this.group.append(GROUP.symbol);
        for (String v : columns) {
            group.append(mainTableName).append(".").append(v).append(",");
        }
        group.deleteCharAt(group.length() - 1);
        return this;
    }

    public Qe<T> having(String column, Opt opt, Object val) {
        if (this.group.length() > 0) {
            this.having.append(HAVING.symbol)
                .append("(")
                .append(mainTableName)
                .append(".")
                .append(column)
                .append(opt.symbol)
                .append(val)
                .append(")");
        }
        return this;
    }

    public Qe<T> limit(int val1, int val2) {
        this.limit.append(LIMIT.symbol).append(val1).append(",").append(val2);
        return this;
    }

    public Qe<T> update(Object entity) {
        if (where.length() == 0) throw new RuntimeException("UPDATE 之前必须有 WHERE 条件以避免大范围变更数据.");
        this.update.append(UPDATE.symbol).append("`").append(getTableName(this.clz)).append("`").append(" SET ");
        this.update.append(getKeyValCondition(entity, ":_up_"));
        return this;
    }

    public Qe<T> save(Object entity) {
        this.update.append(INSERT.symbol).append("`").append(getTableName(this.clz)).append("`").append(" SET ");
        this.update.append(getKeyValCondition(entity, ":_add_"));
        return this;
    }

    private StringBuffer getKeyValCondition(Object entity, String keyPrev) {
        StringBuffer sql = new StringBuffer(128);
        Map<String, Object> fieldMap = new HashMap<String, Object>(20);
        BeanMapUtils.copy(entity, fieldMap);
        for (String k : fieldMap.keySet()) {
            String _k = StringKit.enCodeUnderlined(k);
            sql.append(_k).append("=").append(keyPrev).append(_k).append(", ");
            addParams(keyPrev + _k, fieldMap.get(k));
        }
        sql.deleteCharAt(sql.length() - 2);
        return sql;
    }

    protected String getTableName(Class T) {
        return Attr.getRealTableName(T);
    }

    // ====== 集成查询方法 BEGIN ====================================================================================================
    public T to() {
        List<T> list = sqlManager.execute(new SQLReady(build()), this.clz);
        return C.notEmpty(list) ? list.get(0) : null;

    }

    public <T> T to(Class<T> tClass) {
        List<T> list = sqlManager.execute(new SQLReady(build()), tClass);
        return C.notEmpty(list) ? list.get(0) : null;
    }

    public <T> List<T> toList() {
        return toList(this.clz);
    }


    public <T> List<T> toList(Class tClass) {
        List<T> list = sqlManager.execute(new SQLReady(build()), tClass);
        return C.notEmpty(list) ? list : new ArrayList<>();
    }

    public <T> Page<T> toPage(Page page) {
        return toPage(this.clz, page);
    }

    public boolean toDel() {
        return sqlManager.executeUpdate(new SQLReady(delete().build())) > 0;
    }

    public int toUpdate(Object obj) {
        return sqlManager.executeUpdate(new SQLReady(update(obj).build()));
    }
    public int toSave(Object obj) {
        return sqlManager.executeUpdate(new SQLReady(save(obj).build()));
    }

    /**
     * 返回 PAGE 查询结果集
     * 注意不要传入 limit 语句
     *
     * @param T   返回的class
     * @param page
     * @param <T>
     * @return
     */
    public <T> Page<T> toPage(Class tClass, Page page) {
        PageQuery<T> pq = page.myPageToPageQuery(page, new PageQuery());
        pq = sqlManager.execute(new SQLReady(build()), tClass, pq);
        return page.pageQueryToMyPage(pq, page);
    }
    // ====== 集成查询方法 END ==========================================================================================


    private String getMysqlLimit(String sql, long offset, long pageSize) {
        offset = PageKit.mysqlOffset(false, offset);
        StringBuilder builder = new StringBuilder(sql);
        builder.append(" limit ").append(offset).append(" , ").append(pageSize);
        return builder.toString();
    }

    private void clearMap(Map params) {
        if (null != params) {
            params.clear();
            params = null;
        }
    }

    private void checkJdbc(SQLManager sqlManager) {
        if (null == sqlManager) throw new RuntimeException("SQLManager 不能为 NULL,请先传入.");
    }

    /**
     * PropertyFilter 转为 Qe
     **/
    public Qe<T> whereOfFilters(List<PropertyFilter> filterList) {
        for (PropertyFilter filter : filterList) {
            if (filter.isMulti() && filter.getFilters().size()>1) {
                builWhereOfBothFilter(this, filter);
            } else {
                builWhereOfFilter(this, filter);
            }
        }
        return this;
    }

    private Qe<T> builWhereOfBothFilter(Qe<T> qe, PropertyFilter filter) {
        PropertyFilter left = filter.getFilters().get(0);
        PropertyFilter rigth = filter.getFilters().get(1);
        if (filter.isRoundAnd()) {
            qe.and(builWhereOfFilter(this.c(), left), builWhereOfFilter(this.c(), rigth));
        }
        if (filter.isRoundOr()) {
            qe.or(builWhereOfFilter(this.c(), left), builWhereOfFilter(this.c(), rigth));
        }
        return qe;
    }

    private Qe builWhereOfFilter(Qe qe, PropertyFilter filter) {
        if(filter == null) return qe;
        String filed = StringKit.enCodeUnderlined(filter.getFieldName());
        PropertyFilter.MatchType matchType = filter.getMatchType();
        Object[] vv = filter.getValues();
        Object v1 = null;
        Object v2 = null;
        if(vv.length == 1){
            if(null == vv[0] && matchType != PropertyFilter.MatchType.ISNULL && matchType != PropertyFilter.MatchType.NOTNULL) return qe;
            v1 = StringKit.enCodeUnderlined(String.valueOf(vv[0]));
        }
        if(vv.length ==2){
            v2 = StringKit.enCodeUnderlined(String.valueOf(vv[1]));
        }
        switch (matchType) {
            case EQ:
                return qe = (filter.isAnd()) ? andEq(filed, v1) : orEq(filed, v1);
            case NE:
                return qe = (filter.isAnd()) ? andNe(filed, v1) : orNe(filed, v1);
            case LE:
                return qe = (filter.isAnd()) ? andLe(filed, v1) : orLe(filed, v1);
            case GE:
                return qe = (filter.isAnd()) ? andGe(filed, v1) : orGe(filed, v1);
            case GT:
                return qe = (filter.isAnd()) ? andGt(filed, v1) : orGt(filed, v1);
            case LT:
                return qe = (filter.isAnd()) ? andLt(filed, v1) : orLt(filed, v1);
            case ISNULL:
                return qe = (filter.isAnd()) ? andIsnull(filed) : orIsnull(filed);
            case NOTNULL:
                return qe = (filter.isAnd()) ? andNotnull(filed) : orNotnull(filed);
            case IN:
                return qe = (filter.isAnd()) ? andIn(filed, v1) : orIn(filed, v1);
            case BETWEEN:
                return qe = (filter.isAnd()) ? andBetween(filed, v1, v2) : orBetween(filed, v1, v2);
            case LIKE:
                return qe = (filter.isAnd()) ? andLike(filed, v1) : orLike(filed, v1);
            case LIKESTART:
                return qe = (filter.isAnd()) ? andLike(filed, v1) : orLike(filed, v1);
            case LIKEANYWHERE:
                return qe = (filter.isAnd()) ? andLike(filed, v1) : orLike(filed, v1);
            case LIKEISTART:
                return qe = (filter.isAnd()) ? andLike(filed, v1) : orLike(filed, v1);
            case LIKEIANYWHERE:
                return qe = (filter.isAnd()) ? andLike(filed, v1) : orLike(filed, v1);
        }
        return qe;
    }

    // ====== 集成查询方法 END ====================================================================================================

    private void addWhereSql(Opt opt, String k, Object val, String link, Class... otherT) {
        this.link = link;
        if (null == val || "".equals(val)) return;
        if (this.where.length() > 0) {
            this.where.append(link);
        }
        StringBuffer key = new StringBuffer().append(":").append(link.toLowerCase()).append(opt.name().toLowerCase()).append("_").append(k);
        String table = getTablePrev(otherT);
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

    private Object buildSubSelect(Object val) {
        if (val instanceof Qe) {
            this.checkSqlHack = false;
            return "(" + ((Qe) val).build() + ")";
        }
        return val;
    }

    private String getTablePrev(Class[] otherT) {
        String table = mainTableName;
        if (null != otherT && otherT.length > 0) {
            table = getTableName(otherT[0]);
        }
        return table;
    }

    private void addParams(String key, Object v1) {
        this.params.put(key, v1);
    }

    public Map<String, Object> toJdbdParams(Map<String, Object> params) {
        Map<String, Object> jMap = new HashMap<>(params.size());
        for (String k : params.keySet()) {
            jMap.put(k.replace(":", ""), params.get(k));
        }
        return jMap;
    }

    private void addBetween(String column, Object v1, Object v2, String link, Class... otherT) {
        this.link = link;
        if (null == v1 || "".equals(v1)) return;
        if (null == v2 || "".equals(v2)) return;
        StringBuffer key1 = new StringBuffer().append(":").append(link.toLowerCase()).append("_between_").append(column).append("_v1");
        StringBuffer key2 = new StringBuffer().append(":").append(link.toLowerCase()).append("_between_").append(column).append("_v2");
        String table = getTablePrev(otherT);
        if (this.where.length() > 0) {
            this.where.append(link);
        }
        this.where.append("(")
            .append(table).append(".").append(column)
            .append(Opt.BETWEEN.symbol)
            .append(key1).append(" AND ").append(key2);
        this.where.append(")");
        addParams(key1.toString(), v1);
        addParams(key2.toString(), v2);
        key1 = null;
        key2 = null;
    }

    private void addLike(String column, Object val, String link, Class... otherT) {
        this.link = link;
        if (null == val || "".equals(val)) return;
        if (this.where.length() > 0) {
            this.where.append(link);
        }
        String table = getTablePrev(otherT);
        StringBuffer key = new StringBuffer().append(":").append(link.toLowerCase()).append("_like_").append(column);
        this.where.append("(")
            .append(Opt.LIKE.symbol).append("(")
            .append(table).append(".").append(column)
            .append(",")
            .append(key)
            .append(")>0) ");
        addParams(key.toString(), val);
        key = null;
    }

    private void addManyCondition(String link, Qe... manyQe) {
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
        OR(" OR "),
        UPDATE(" UPDATE "),
        INSERT(" INSERT INTO ");

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
            return v.toString();
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
            return v.toString();
        }

        if (val instanceof Date) {
            v.append("'").append(sdf.format(val)).append("'");
            return v.toString();
        }
        if (val instanceof String && isNotSubSelect(val)) {
            v.append("'").append(val).append("'");
            return v.toString();
        }
        if (val instanceof BigDecimal) {
            v.append("'").append(((BigDecimal) val).doubleValue()).append("'");
            return v.toString();
        }

        if (v.length() == 0) {
            v.append(val);
        }
        return v.toString();
    }

    private boolean isNotSubSelect(Object v) {
        return !(v.toString().startsWith("(SELECT"));
    }

    private static final String reg = "/(\\\\%27)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|union|update|and|or|delete|insert|trancate|" +
        "into|substr|ascii|declare|exec|execute|count|master|into|drop|information_schema.columns|table_schema)\\b)";

    private static final Pattern SQL_PATTERN = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    private boolean isSQLHack(String v) {
        if (checkSqlHack) {
            return SQL_PATTERN.matcher(v).find();
        }
        return false;
    }

    private StringBuffer buildWhereSql() {
        if (where.length() > 0 && where.indexOf("WHERE") == -1) {
            where.insert(0, "(");
            where.insert(0, WHERE.symbol);
            where.append(")");
        }
        return where;
    }

    public String buildSymbolsql() {
        String sql = this.template;
        if (isReadSql()) {
            sql = replaceTag(sql, "{select}", buildSelect());
            sql = replaceTag(sql, "{from}", buildFrom());
        } else {
            sql = sql.replace("{select}", "").replace("{from}", "");
        }
        sql = replaceTag(sql, "{del}", del);
        sql = replaceTag(sql, "{count}", count);
        sql = replaceTag(sql, "{join}", join);
        sql = replaceTag(sql, "{where}", buildWhereSql());
        sql = replaceTag(sql, "{subselect}", subselect);
        sql = replaceTag(sql, "{sum}", sum);
        sql = replaceTag(sql, "{casethen}", casethen);
        sql = replaceTag(sql, "{sumcasethen}", sumcasethen);
        sql = replaceTag(sql, "{group}", group);
        sql = replaceTag(sql, "{having}", having);
        sql = replaceTag(sql, "{order}", order);
        sql = replaceTag(sql, "{limit}", limit);
        //
        sql = replaceTag(sql, "{update}", update);
        sql = replaceTag(sql, "{insert}", insert);
        this.symbolsql = sql;
        return this.symbolsql;
    }

    public String build() {
        String sql = settingParams(buildSymbolsql());
        clear();
        return sql;
    }

    private boolean isReadSql() {
        return (this.update.length() == 0 && this.insert.length() == 0);
    }

    private StringBuffer buildSelect() {
        if (this.select.length() == 0 && this.count.length() == 0) {
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

    private String settingParams(String symbolsql, Map<String, Object> params) {
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

    private StringBuffer buildFrom() {
        if (this.from.length() == 0) {
            this.from.append(FROM.symbol);
            if (null != mainTableName && !"".equals(mainTableName)) {
                this.from.append(mainTableName).append(" AS ").append(mainTableName).append(" ");
            }
        }
        return this.from;
    }

    public void clear() {
        del = new StringBuffer();
        count = new StringBuffer();
        sum = new StringBuffer();
        casethen = new StringBuffer();
        sumcasethen = new StringBuffer();
        select = new StringBuffer();
        subselect = new StringBuffer();
        from = new StringBuffer();
        join = new StringBuffer();
        where = new StringBuffer();
        group = new StringBuffer();
        having = new StringBuffer();
        order = new StringBuffer();
        limit = new StringBuffer();
        link = null;
        clearMap(params);
    }


    public static void main(String[] args) throws Exception {
        String sql = new Qe(User.class)
            .select("user_uame")
            .andIn("dept_id", new Qe(Dept.class).select("id").andGt("id", 0))
//                .sum("id", Dept.class)
//                .sumCase("id", 1, "money", "money")
//                .leftJoin(Dept.class,"dept_id")
//                .andLike("name", "alice")
//                .andIn("id", new Integer[]{1, 2, 3})
//                .andBetween("createDate", new Date(), new Date())
//                .group("dept_id")
//                .having("dept_id", Opt.GT, 0)
//                .asc("dept_id")
//                .desc("user_uame")
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

/*        String del = new Qe(SysUser.class).del().andEq("dept_id", 1).build();
        System.out.println("del= " + del);

        String count = new Qe(SysUser.class).andEq("dept_id", 1).count();
        System.out.println("count= " + count);*/


    }

    private static class User {
        private Integer id;
        private String name;
        private BigDecimal money;
        private Integer deptId;
        private Date createDate;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }

        public Integer getDeptId() {
            return deptId;
        }

        public void setDeptId(Integer deptId) {
            this.deptId = deptId;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }
    }

    private static class Dept {
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
