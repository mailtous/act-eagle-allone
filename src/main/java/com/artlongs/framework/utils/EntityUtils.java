package com.artlongs.framework.utils;

import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.model.SysUser;
import org.osgl.util.C;
import org.osgl.util.Keyword;
import org.osgl.util.S;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Function: 转换实体类的私有属性的字段名称,并且保存到MAP
 *
 * @Autor: leeton
 * @Date : 2/23/18
 */
public class EntityUtils {

    public static final String PREFIX_CHAR = "@";  //sql语句里标记字段的前缀字符
    public static final String END_CHAR = " ";     //sql语句里标记字段的后缀字符
    public static final String BLANK = " ";        //空格

    private static Map<String, String> sqlMap = C.newMap(); //sql 语句的缓存

    /**
     * 转换实体类的字段名称,并且保存到MAP
     *
     * @param targetClzz 实体类
     * @param changeTo   转换后的类型(驼峰,下划线)
     * @return
     */
    public static Map<String, String> cateFieldsToMap(Class targetClzz, Keyword.Style changeTo) {
        Map<String, String> fieldMap = C.newMap();
        cateFields(targetClzz, fieldMap, changeTo);
        return fieldMap;

    }

    /**
     * 把实体类的私有属性名称存入MAP,按(myName,my_name)格式保存
     *
     * @param clz
     * @param fieldMap
     * @param sep
     */
    public static void cateFields(Class clz, Map<String, String> fieldMap, Keyword.Style sep) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (notFilterModifier(field.getModifiers())) { // 跳过修饰的属性
//                System.err.println("modifier=" + field.getModifiers());
                fieldMap.putIfAbsent(field.getName(), toStyle(field.getName(), sep));
            }
        }
        if (null != clz.getSuperclass() && (!clz.getSuperclass().getName().equalsIgnoreCase("java.lang.Object"))) {
            cateFields(clz.getSuperclass(), fieldMap, sep);
        }
    }

    public static String toStyle(String target, Keyword.Style sep) {
        if (sep.equals(Keyword.Style.CAMEL_CASE)) {
            target = S.camelCase(target);
        }
        if (sep.equals(Keyword.Style.UNDERSCORE)) {
            target = S.underscore(target);
        }

        return target;
    }

    /**
     * 跳过指定的修饰符
     *
     * @param modifier
     * @return
     */
    public static boolean notFilterModifier(int modifier) {
        if (1 == modifier) return false; // 跳过 public 修饰的属性
        if (128 == modifier) return false; // 跳过 transient 修饰的属性
        if (25 == modifier) return false; // 跳过 public static final 修饰的属性
        if (129 == modifier) return false; // 跳过 public transient 修饰的属性
        return true;
    }

    /**
     * 格式化 SQL 把驼峰属性名称转为下划线的格式
     *
     * @param sql
     * @param clzz
     * @return ( @funcName --> func_name)
     */
    public static String formatSql(String sql, Class clzz) {
        Map<String, String> fieldMap = cateFieldsToMap(clzz, Keyword.Style.UNDERSCORE);
        String key = sql;
        if(S.blank(sqlMap.get(key))){
            String regx = PREFIX_CHAR + "(.*?)" + END_CHAR;
            Pattern p = Pattern.compile(regx);
            Matcher m = p.matcher(sql);
            while (m.find()) {
                sql = sql.replace(PREFIX_CHAR + m.group(1) + END_CHAR, fieldMap.get(m.group(1)) + BLANK);
            }
            sqlMap.putIfAbsent(key, sql);
        }

        return sql;
    }


    public static void main(String[] args) throws Exception {

        Map<String, String> fieldMap = cateFieldsToMap(SysFunc.class, Keyword.Style.UNDERSCORE);
        for (String s : fieldMap.keySet()) {
            System.err.println(s + " == " + fieldMap.get(s));
        }

        String str = "Select * from sys_func where @funcName = ? and @funcUrl = ?";
        System.err.println(formatSql(str, SysFunc.class));

        QE qe = new QE();
        qe.k(SysUser.Dao.userName).eq("linton");

    }


}
