package com.High365.HighLight;

import java.util.List;

/**
 * 这是一个监听器类
 * 为获取排名的监听器
 */
public interface GetRankListener {
    public void onSuccess(List list);
    public void onFailure(String msg);
}
