package com.High365.util;

/**
 * 此文件是九宫格图形锁的工具类
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
