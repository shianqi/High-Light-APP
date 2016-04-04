package com.High365.HighLight.Service;

import android.content.Context;
import com.High365.HighLight.Bean.FriendCircleModel;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Bean.UpdateModel;
import com.High365.HighLight.Interface.GetListListener;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Util.HttpRequest;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.ToastManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.util.List;

/**
 * Created by HUPENG on 2/13/16.
 * 这个类主要负责处理朋友圈的本地逻辑
 */
public class FriendCircleService {
    /**
     * http请求中的URL;
     * */
    private String url;
    /**
     * http请求中所需要的参数;
     * */
    private RequestParams params;

    /**
     * 此私有成员保存http请求的结果;
     * */
    private String httpResponseStr;
    /**
     * 显示在界面上的最大的朋友圈ID(最新的朋友圈ID)
     * */
    private Integer bigID;

    /**
     * 显示在界面上的最小的朋友圈ID(最旧的朋友圈ID)
     * */
    private Integer smallID;


    /**
     * 构造函数,初始化一些参数
     * */
    public FriendCircleService(){
        smallID = 0;
        bigID = 0;
    }


    /**
     * 分享到朋友圈的逻辑代码;<br/>
     * @param context activity上下文;
     * @param friendCircleModel 有activity传过来的JavaBean对象;
     * @param listener 监听器接口,在调用点即activity进行实现;
     * 一旦分享成功,则进行listener的成功回调,否则进行失败回调.
     * */
    public void shareToFriendCircle(Context context,FriendCircleModel friendCircleModel,Listener listener){

        SharedPreferencesManager spm = new SharedPreferencesManager(context);

        //初始化请求参数
        this.url = "shareFriendCircle.action";
        try {

            params = new RequestParams();
            params.add("userID",spm.readString("UserID"));
            params.add("secretKey",spm.readString("secretKey"));
            params.add("jsonString",new Gson().toJson(friendCircleModel));
            HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        httpResponseStr = new String(bytes);
                        //将返回的Json字符串转换成Json对象,对此Json对象进行分析然后进行回调;
                        UpdateModel updateModel = new Gson().fromJson(httpResponseStr,UpdateModel.class);
                        if (updateModel.getStatus() == 1){
                            // 此处进行成功回调
                            listener.onSuccess();
                            return;
                        }else{
                            //此处进行失败回调
                            listener.onFailure(updateModel.getErrorInfo());
                            return;
                        }
                    }catch (Exception e){
                        listener.onFailure("网络请求是发生错误");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    listener.onFailure(throwable.getMessage());
                }
            });
        }catch (Exception e){
            listener.onFailure("网络请求初始化失败");
            return;
        }
    }

    /**
     * 获取下拉刷新的List的逻辑代码;<br/>
     * @param context Activity上下文;<br/>
     * @param getListListener 监听器,在成功的回调里有List对象,在失败的回调里有错误信息;<br/>
     * 一旦获取成功则进行成功回调,一般发生异常抛出就进行失败回调.<br/>
     * */
    public void getDownPullList(Context context,GetListListener getListListener){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userId = sharedPreferencesManager.readString("UserID");
        //初始化请求参数
        url = "getFriendCircle.action";
        params = new RequestParams();
        params.add("oper","0");
        params.add("userId",userId);
        params.add("begin","0");
        try{
            HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        httpResponseStr = new String(bytes);
                        //将返回的对象转换成List
                        List<FriendCircleModel>list = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create().fromJson(httpResponseStr,new TypeToken<List<FriendCircleModel>>(){}.getType());
                        if (list.size()!=0){
                            bigID = list.get(0).getCircleId();
                            smallID = list.get(list.size()-1).getCircleId();
                        }
                        getListListener.onSuccess(list);
                    }catch (Exception e){
                        e.printStackTrace();
                        getListListener.onFailure("网络请求失败");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    getListListener.onFailure(throwable.getMessage());
                }
            });
        }catch (Exception e){
            getListListener.onFailure("网络请求初始化失败");
        }
    }

    /**
     * 获取上拉加载的List的逻辑代码;<br/>
     * */

    public void getUpPulllist(Context context,GetListListener getListListener){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userId = sharedPreferencesManager.readString("UserID");
        //初始化请求参数
        url = "getFriendCircle.action";
        params = new RequestParams();
        params.add("oper","1");
        params.add("begin",smallID+"");
        params.add("userId",userId);


        try{
            HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try{
                        httpResponseStr = new String(bytes);
                        //将返回的对象转换成List
                        List<FriendCircleModel>list =new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create().fromJson(httpResponseStr,new TypeToken<List<FriendCircleModel>>(){}.getType());
                        if (list.size()!=0){
                           smallID = list.get(list.size()-1).getCircleId();
                        }

                        if (list.size() == 0){
                            ToastManager.toast(context,"已经到底啦！");
                        }
                        getListListener.onSuccess(list);
                    }catch (Exception e){
                        e.printStackTrace();
                        getListListener.onFailure("网络请求失败");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    getListListener.onFailure(throwable.getMessage());
                }
            });
        }catch (Exception e){
            getListListener.onFailure("网络请求初始化失败");
        }
    }

    /**
     * 将LoveLog对象转换成FriendCircle对象
     * @param loveLogBean lvoeLog对象模型
     * @param shareText 用户要分享的文字
     * */
    public FriendCircleModel fromLoveLog(String shareText,LoveLogBean loveLogBean){

        FriendCircleModel friendCircleModel = new FriendCircleModel();
        friendCircleModel.setUserId(loveLogBean.getUserId());
        friendCircleModel.setSexTime(loveLogBean.getSexTime());
        friendCircleModel.setSexSubjectiveScore(loveLogBean.getSexSubjectiveScore());
        friendCircleModel.setSexObjectiveScore(loveLogBean.getSexObjectiveScore());
        friendCircleModel.setSexFrameState(loveLogBean.getSimpleSexFrameState());
        friendCircleModel.setShareText(shareText);
        return friendCircleModel;
    }

}
