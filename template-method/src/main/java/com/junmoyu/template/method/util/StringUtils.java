package com.junmoyu.template.method.util;

/**
 * String 工具类
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

}
