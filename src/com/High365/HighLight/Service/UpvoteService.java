package com.High365.HighLight.Service;

import android.content.Context;
import com.High365.HighLight.Bean.SimpleModel;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Util.HttpRequest;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

/**
 * 点赞功能的业务逻辑类
 */
public class UpvoteService {
    private String url;
    private RequestParams params;
    private String responseStr;

    /**
     *  点赞功能的业务逻辑实现
     *  @param circleId 朋友圈Id
     *  @param context activity上下文
     *  @param listener 普通监听器，有两个回掉
     * */
    public void addUpvote(Integer circleId, Context context, Listener listener){
        url = "upvote.action";

        String userId = new SharedPreferencesManager(context).readString("UserID");

        params = new RequestParams();
        params.add("circleId",circleId+"");
        params.add("userId",userId);

        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                responseStr = new String(bytes);
                try {
                    SimpleModel simpleModel = new Gson().fromJson(responseStr,SimpleModel.class);
                    if (simpleModel.getStatus() == 1){
                        listener.onSuccess();
                    }else {
                        listener.onFailure("失败："+ simpleModel.getMsg());
                    }
                }catch (Exception e){
                    listener.onFailure("解析发生错误，原因：" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onFailure("网络请求失败，原因：" + throwable.getLocalizedMessage());
            }
        });

    }
}
