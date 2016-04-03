package com.High365.HighLight.Service;

import android.content.Context;
import com.High365.HighLight.Bean.CommentModel;
import com.High365.HighLight.Bean.SimpleModel;
import com.High365.HighLight.Interface.GetListListener;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Util.HttpRequest;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论的业务逻辑类
 */
public class CommentService {
    /**
     * http请求的相对地址
     * */
    private String url;

    /**
     * http请求的参数
     * */
    private RequestParams params;

    /**
     * http返回流
     * */
    private String responseStr;

    /**
     * 添加评论
     * */
    public void addComment(Integer circleId, String text, Context context, Listener listener){
        url = "updateComment.action";
        params = new RequestParams();

        String userId =  new SharedPreferencesManager(context).readString("UserID");

        params.add("userId",userId);
        params.add("circleId",circleId+"");
        params.add("text",text);

        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    //获取返回信息
                    responseStr = new String(bytes);
//                    //转会为实体
//                    List<CommentModel>list = new Gson().fromJson(responseStr,new TypeToken<List<CommentModel>>(){}.getType());
//                    listener.onSuccess(list);
                    SimpleModel simpleModel = new Gson().fromJson(responseStr,SimpleModel.class);
                    if (simpleModel.getStatus() == 1){
                        listener.onSuccess();
                    }else{
                        listener.onFailure("评论失败");
                    }
                }catch (Exception e){
                    listener.onFailure("解析发生错误，原因" + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onFailure("发送http请求失败，请检查网络设置,原因：" + throwable.getLocalizedMessage());
            }
        });
    }

    /**
     * 获取某一个朋友圈信息的评论列表
     * */
    public void getCommentList(Integer circleId, Context context, GetListListener listListener){
        url = "getComment.action";
        params = new RequestParams();
        params.add("circleId",circleId+"");

        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                responseStr = new String(bytes);
                try {
                    //转会为实体
                    List<CommentModel>list = new Gson().fromJson(responseStr,new TypeToken<List<CommentModel>>(){}.getType());
                    listListener.onSuccess(list);
                }catch (Exception e){
                    listListener.onFailure("解析发生错误，原因" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listListener.onFailure("请求失败，原因：" + throwable.getLocalizedMessage());
            }
        });
    }
}
