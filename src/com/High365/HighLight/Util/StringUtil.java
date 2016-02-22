package com.High365.HighLight.Util;

/**
 * 密码内容操作
 * 此文件是九宫格图形锁的工具类(这个工具是互联网上的开源代码)<br>
 *     详细内容请参考<a href="http://www.cnblogs.com/weixing/p/3413998.html">Android之九宫格解锁的实现</a>
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
