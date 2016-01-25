package com.High365.util;

/**
 * 密码内容操作
 * 此文件是九宫格图形锁的工具类
 * @author shianqi@imudges.com
 *
 */
public class StringUtil {
    /**
     * 是否不为空
     * @param s 待判断密码
     * @return true 不为空,false 为空
     */
    public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s.trim());
    }

    /**
     * 是否为空
     * @param s 待判断密码
     * @return true 空,false 不为空
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    /**
     * 通过{n},格式化.
     *
     * @param src src
     * @param objects objects
     * @return String
     */
    public static String format(String src, Object... objects) {
        int k = 0;
        for (Object obj : objects) {
            src = src.replace("{" + k + "}", obj.toString());
            k++;
        }
        return src;
    }
}
