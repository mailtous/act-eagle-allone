package com.artlongs.framework.utils;

import static sun.jvm.hotspot.utilities.Assert.*;

/**
 * Func :
 *
 * @author: leeton on 2019/6/21.
 */
public class Assert {
    public static void isNull(Object val, String throwMsg) {
        that(null == val, throwMsg);
    }

    public static void isBlank(Object val, String msg) {
        that(null == val || String.valueOf(val).trim().length() == 0, msg);
    }

    public static void isNotFound(Object val, String msg) {
        that(null == val || val.equals("false") || val.equals(-1), msg);
    }

    public static void isTrue(boolean test, String msg) {
        that(test, msg);
    }



}
