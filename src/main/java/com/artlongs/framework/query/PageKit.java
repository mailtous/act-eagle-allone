package com.artlongs.framework.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Func :
 *
 * @author: leeton on 2019/6/11.
 */
public class PageKit {
    static String pageNumberName = "pageNumber";
    static String pageSizeName = "pageSize";

    static int pageSizeValue = 20;

    /**
     * sql格式化工具
     *
     * @param sql 正常的sql语句
     * @return 格式化后的sql语句
     */
    public static String formatSql(String sql) {
        return SqlFormatter.format(sql,true);
    }

    /**
     * 获取当前页码
     *
     * @param paras 参数map
     * @return 如果没有找到, 默认返回1
     */
    public static int getPageNumber(Map<String, Object> paras) {
        Integer pageNumber = (Integer) paras.get(pageNumberName);

        return pageNumber == null ? 1 : pageNumber.intValue();
    }

    /**
     * 获取每页显示的数量
     *
     * @param paras 参数map
     * @return 如果没有找到, 返回设置的默认大小
     */
    public static int getPageSize(Map<String, Object> paras) {
        Integer pageSize = (Integer) paras.get(pageSizeName);

        return pageSize == null ? pageSizeValue : pageSize.intValue();
    }

    // Page limit offset ================
    static long mysqlOffset(boolean offsetStartZero, long start) {
        return start - (offsetStartZero ? 0 : 1);
    }

    static long postgresOffset(boolean offsetStartZero, long start) {
        return start - (offsetStartZero ? 0 : 1);
    }

    static long oracleOffset(boolean offsetStartZero, long start) {
        return start + (offsetStartZero ? 1 : 0);
    }

    static long oraclePageEnd(long offset, long pageSize) {
        return offset + pageSize;
    }

    static long db2sqlOffset(boolean offsetStartZero, long start) {
        return start + (offsetStartZero ? 1 : 0);
    }

    static long db2sqlPageEnd(long offset, long pageSize) {
        return offset + pageSize - 1;
    }

    static long sqlLiteOffset(boolean offsetStartZero, long start) {
        return start - (offsetStartZero ? 0 : 1);
    }

    static long sqlServerOffset(boolean offsetStartZero, long start) {
        return start + (offsetStartZero ? 1 : 0);
    }

    static long sqlServer2012Offset(boolean offsetStartZero, long start) {
        return start - (offsetStartZero ? 0 : 1);
    }

    static long sqlServerPageEnd(long offset, long pageSize) {
        return offset + pageSize - 1;
    }
// Page limit offset end ================

    public static String getCountSql(String selectSql) {
        String sql = PageKit.formatSql(selectSql);
        final String orderby = SqlFormatter.indentString + "order by";
        final String groupby = SqlFormatter.indentString + "group by";
        final String limit = SqlFormatter.indentString + "limit";
        final String from = SqlFormatter.indentString + "from";
        //
        int fromIndex = 0;
        boolean fromIndexOver = false;
        Map<String, Object> sqlMap = new HashMap<>();
        sqlMap.put("sql", sql);
        sqlMap.put("fromIndexOver", fromIndexOver);
        findAndRemoveSymbol(sqlMap, from);
        findAndRemoveSymbol(sqlMap, groupby);
        findAndRemoveSymbol(sqlMap, orderby);
        findAndRemoveSymbol(sqlMap, limit);

        sql = "select count(1) \n" + sqlMap.get("sql");
        sqlMap.clear();
        return sql;
    }

    private static void findAndRemoveSymbol(Map<String, Object> sqlMap, String symbol) {
        String sql = String.valueOf(sqlMap.get("sql"));
        boolean fromIndexOver = (boolean)sqlMap.get("fromIndexOver");
        int fromIndex = 0;
        int fromEnd = 0;
        //
        String[] subSqlArr = sql.split("\n");
        int total_len = sql.length();
        for (String s : subSqlArr) {
            s = s.toLowerCase(); //转为小写以方便匹配
            if (fromEnd >= total_len) break;
            if (!fromIndexOver) {
                if (!fromIndexOver && s.equals(symbol)) {// find first 'form' symbol
                    fromIndexOver = true;
                    sqlMap.remove("fromIndexOver");
                    sqlMap.put("fromIndexOver", fromIndexOver);
                } else {
                    fromIndex += s.length() + 1;
                }
            }

            if (StringKit.startsWith(s,symbol,true) && (!symbol.equals("    from"))) { // find spc symbol then break elas fromEnd++
                break;
            }
            fromEnd += s.length() + 1;
        }
        sql = sql.substring(fromIndex, fromEnd - 1);
        sqlMap.remove("sql");
        sqlMap.put("sql", sql);
    }


    public static void main(String[] args) {
        String sql = "FROM user where name=:Name and id IN(select id from dept group by id ) GROUP by id  order by id desc ";
        sql = PageKit.getCountSql(sql);
        System.out.println(sql);
        System.out.println("===================================");

        ///
        StringBuffer sb = new StringBuffer("FROM quote_spread_properties qsp LEFT JOIN");
        sb.append(" quote q ON qsp.quote_id = q.id WHERE q.com_id = ? AND q.status < 2 AND q.type_number = ? AND q.product_number = ?");
        sb.append(" AND q.factory_name = ? AND q.brand_number = ?");
        String hql = PageKit.getCountSql(sb.toString());
        System.out.println(hql);
    }

}
