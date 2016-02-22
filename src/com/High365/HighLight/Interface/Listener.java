package com.High365.HighLight.Interface;

/**
 * @author HUPENG
 * 监听器接口,用于实现异步回调
 */
public interface Listener{
    /**
     * 成功回调
     * */
    public void onSuccess();

    /**
     * 失败回调
     * @param msg 错误信息
     * */
    public void onFailure(String msg);
}
