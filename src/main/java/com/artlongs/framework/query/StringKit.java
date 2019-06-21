package com.artlongs.framework.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Func :
 *
 * FROM beetlsql
 */
public class StringKit {
    public static final String EMPTY = "";

    public static final int INDEX_NOT_FOUND = -1;

    public static final String[] EMPTY_STRING_ARRAY = new String[0];


    static  String lineSeparator = System.getProperty("line.separator", "\n");

    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder())
                .append(Character.toLowerCase(s.charAt(0)))
                .append(s.substring(1)).toString();
    }

    // 首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder())
                .append(Character.toUpperCase(s.charAt(0)))
                .append(s.substring(1)).toString();
    }

    // 大写字母前面加上下划线并转为全小写
    public static String enCodeUnderlined(String s) {
        if (null == s) return null;
        char[] chars = toLowerCaseFirstOne(s).toCharArray();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if(Character.isUpperCase(chars[i])){
                temp.append("_");
            }
            temp.append(Character.toLowerCase(chars[i]));
        }
        return temp.toString();
    }

    // 删除下划线并转把后一个字母转成大写
    public static String deCodeUnderlined(String str) {
        if (null == str) return null;
        String[] splitArr = str.split("_");
        StringBuilder sb = new StringBuilder();

        for(int i=0 ;i<splitArr.length ;i++){
            if(i == 0){
                sb.append(splitArr[0].toLowerCase());
                continue;
            }

            sb.append(toUpperCaseFirstOne(splitArr[i].toLowerCase()));
        }

        return sb.toString();
    }


    /**
     * 去空格
     * @param str
     * @return
     */
    public static String trimAllWhitespace(String str) {
        if (!((CharSequence) str != null && ((CharSequence) str).length() > 0)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        int index = 0;
        while (sb.length() > index) {
            if (Character.isWhitespace(sb.charAt(index))) {
                sb.deleteCharAt(index);
            }
            else {
                index++;
            }
        }
        return sb.toString();
    }

    /**
     * 左边去空格
     * @param str
     * @return
     */
    public static String trimLeft(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int index = 0;
        while (str.length() > index) {
            if (!Character.isWhitespace(str.charAt(index))) {
                str = str.substring(index,str.length());
                break;
            }
            index++;
        }
        return str;
    }

    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    public static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }


    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }


    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断一个 Object 是否为空，不包含集合对象的判断
     *
     * @param obj need to determine the object
     * @author larrykoo
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        return false;
    }

    /**
     * 严格判断一个 Object 是否为空，包括对象为 null，字符串长度为0，集合类，Map 为 empty 的情况
     *
     * @param obj
     * @author larrykoo
     * @return
     */
    public static boolean isNullOrEmptyObject(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    public static String beforeLast(String str, char separatorChar) {
        int pos = str.lastIndexOf((int) separatorChar);
        return pos == -1 ? "" : str.substring(0, pos);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

//	public static String removeLastToken(String str,String token) {
//	    if()
//	}


    public static void main(String[] args) {
        System.err.println(trimAllWhitespace(" fsdfsd sdfds fsd "));
        System.err.println(trimLeft("     from user"));

    }



}
