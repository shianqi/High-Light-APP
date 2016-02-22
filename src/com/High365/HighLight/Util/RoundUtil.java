package com.High365.HighLight.Util;

/**
 * 此文件是九宫格图形锁的工具类(这个工具是互联网上的开源代码,不做详细介绍)<br>
 *     详细内容请参考<a href="http://www.cnblogs.com/weixing/p/3413998.html">Android之九宫格解锁的实现</a>
 */
public class RoundUtil {
    /**
     * 点在圆肉
     * @param sx sx
     * @param sy sy
     * @param r r
     * @param x x
     * @param y y
     * @return 点是否在圆内
     */
    public static boolean checkInRound(float sx, float sy, float r, float x,
                                       float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }
}
