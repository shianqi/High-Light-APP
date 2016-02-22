package com.High365.HighLight.Interface;

import java.util.List;

/**
 * 接口,获取服务器端的的List数据时所用的监听器
 */
public interface GetListListener {
    /**
     * 成功回调
     * */
    public void onSuccess(Object list);

    /**
     * 失败回调
     * @param msg 错误信息
     * */
    public void onFailure(String msg);
}
