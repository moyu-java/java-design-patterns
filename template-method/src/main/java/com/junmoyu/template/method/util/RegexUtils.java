package com.junmoyu.template.method.util;

import java.util.regex.Pattern;

/**
 * 正则校验类
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public class RegexUtils {

    /**
     * 正则：手机号
     */
    public static final String REGEX_MOBILE = "^[1]\\d{10}$";

    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 验证手机号
     *
     * @param input 待验证文本
     * @return true: 匹配; false: 不匹配
     */
    public static boolean isMobile(CharSequence input) {
        return isMatch(REGEX_MOBILE, input);
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return true: 匹配; false: 不匹配
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return true: 匹配; false: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}
