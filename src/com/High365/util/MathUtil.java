package com.High365.util;

/**
 * 此文件是九宫格图形锁的工具类
 */
public class MathUtil {
    /**
     * 两点间的距离
     *
     * @param x1 第一个点的横坐标
     * @param y1 第一个点的纵坐标
     * @param x2 第二个点的横坐标
     * @param y2 第二个点的纵坐标
     * @return 两点间的距离
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2));
    }

    /**
     * 计算点a(x,y)的角度
     *
     * @param x 横坐标
     * @param y 横坐标
     * @return 角度
     */
    public static double pointTotoDegrees(double x, double y) {
        return Math.toDegrees(Math.atan2(x, y));
    }
}
