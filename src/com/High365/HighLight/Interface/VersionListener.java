package com.High365.HighLight.Interface;

/**
 * Created by HUPENG on 2016/4/11.
 */
public interface VersionListener {
    /**
     * 成功回调
     * */
    public void onSuccess(String url);

    /**
     * 失败回调
     * @param msg 错误信息
     * */
    public void onFailure(String msg);

}
