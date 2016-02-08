package com.High365.HighLight;

/**
 * 点位置<br>
 *     此文件是九宫格图形锁的工具类(这个工具是互联网上的开源代码,不做详细介绍)<br>
 *     详细内容请参考<a href="http://www.cnblogs.com/weixing/p/3413998.html">Android之九宫格解锁的实现</a>
 * @author shianqi@imudges.com
 * @version 1.0
 */
public class Point {
    public static int STATE_NORMAL = 0; // 未选中
    public static int STATE_CHECK = 1; // 选中 e
    public static int STATE_CHECK_ERROR = 2; // 已经选中,但是输错误

    public float x;
    public float y;
    public int state = 0;
    public int index = 0;// 下标

    public Point() {

    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
