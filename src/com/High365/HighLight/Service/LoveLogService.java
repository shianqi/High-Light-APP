package com.High365.HighLight.Service;

import android.content.Context;
import android.util.Log;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Bean.RankModel;
import com.High365.HighLight.Bean.SimpleModel;
import com.High365.HighLight.Bean.UpdateModel;
import com.High365.HighLight.Interface.GetRankListener;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Util.HttpRequest;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author hupeng
 * 这个类主要负责LoveLog的业务逻辑的处理和本地数据库的访问
 *
 * 此类继承于线程类
 * url:服务器的url地址的一部分,IP地址与端口号在HttpRequest类中定义,这样做只需要修改那边的HOST的值就可以方便的更换服务器的地址与参数
 * param:参数
 * httpResponseStr:服务器所返回的json字符串
 * 由于Android的网络通信模块必须放在子线程中,若放在主线程中会导致阻塞主线程.
 */
public class LoveLogService {
    private String url;
    private String httpResponseStr;
    private RequestParams params;

    public void update(LoveLogBean loveLogBean,Context context){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userID = sharedPreferencesManager.readString("UserID");
        String secretKey = sharedPreferencesManager.readString("secretKey");

        url = "updateLoveLog.action";
        params = new RequestParams();
        params.add("userID",userID);
        params.add("secretKey",secretKey);
        params.add("jsonString",new Gson().toJson(loveLogBean));

        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try{
                    httpResponseStr = new String(bytes);
                    UpdateModel updateModel = new Gson().fromJson(httpResponseStr, UpdateModel.class);
                    if (updateModel!=null){
                        if (updateModel.getStatus() == 1){
                            //当更新成功时,更新本地数据库中的updateFlag值,值为1时则已经成功上传到远程服务器
                            loveLogBean.setUpdateFlag(1);
                            SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
                            sqlLiteManager.updateOrInsertLoveLog(loveLogBean);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }

    /**
     * @param oper 获取oper的值不同，获取的数据类别
     * @param context context
     * @param listener 回调是否成功
     * oper=11:个人周排行榜
     * oper=12:个人月排行榜
     * oper=13:个人年排行榜
     * oper=21:全部用户周排行榜
     * oper=22:全部用户月排行榜
     * oper=23:全部用户年排行榜
     */
    public void getRankModelList(int oper,Context context,GetRankListener listener) {
        String userID = new SharedPreferencesManager(context).readString("UserID");
        url = "getRank.action";
        params = new RequestParams();
        params.add("userID",userID);
        params.add("oper",oper+"");
        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                List<RankModel> list = new ArrayList<RankModel>();
                try {
                    httpResponseStr = new String(bytes);
                    Log.d("服务器返回的数据",httpResponseStr);
                    list = new Gson().fromJson(httpResponseStr,new TypeToken<List<RankModel>>() {}.getType());
                    listener.onSuccess(list);
                }catch (Exception e){
                    listener.onFailure("获取信息失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onFailure(throwable.getMessage());
            }
        });
    }

    /**
     * 获取用户日志
     * @param context context
     * @return 用户的日志列表
     */
    public List<LoveLogBean>getLoveLogList(Context context){
        SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
        String userID = new SharedPreferencesManager(context).readString("UserID");
        List<LoveLogBean> list = sqlLiteManager.getLoveLogsByUserID(userID);


        for (int i=0;i<list.size();i++) {
            LoveLogBean loveLogBean = list.get(i);
            String sexState = loveLogBean.getSexFrameState();
            int index = 0;
            int n = 0;
            int sum = 0;
            String temp = "";
            String tmp = "";
            if (sexState.length() > 100) {
                for (int j = 0; j < sexState.length(); j = j + 2) {

                    if (index == j * 51 / sexState.length()) {
                        tmp = "";
                        tmp = tmp + sexState.charAt(j) + sexState.charAt(j + 1);
                        sum += Integer.parseInt(tmp);
                        n++;
                    } else {
                        index = j * 51 / sexState.length();
                        if (sum / n < 10) {
                            temp += "0" + sum / n;
                        } else {
                            temp += sum / n;
                        }
                        sum = 0;
                        n = 0;


                        tmp = "";
                        tmp = tmp + sexState.charAt(j) + sexState.charAt(j + 1);
                        sum += Integer.parseInt(tmp);
                        n++;
                    }
                    Log.i("temp" + j, "?" + temp + "?");
                }
                Log.i("temp" + i, "?" + temp + "?");
                loveLogBean.setSexFrameState(temp);
            }
        }


        return list;
    }


    /**
     * 在第一次登录时，获取最新的10条记录插在本地数据库中
     * @param context Activity上下文
     * */
    public void getLast10LoveLogs(Context context){
        if (getLoveLogList(context).size()!=0){
            return;
        }
        try {
            url = "getLoveLog.action";
            params =  new RequestParams();
            params.add("userId",new SharedPreferencesManager(context).readString("UserID"));
            HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        //Log.i("getLoveLog","success");
                        httpResponseStr = new String(bytes);
                        Log.i("xxxxxxxxxxxxxxxxxxx",httpResponseStr);
                        //json转实体
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create();
                        List<LoveLogBean> loveLogBeens = gson.fromJson(httpResponseStr,new TypeToken<List<LoveLogBean>>(){}.getType());
                        //写入数据库----+++++
                        SqlLiteManager  sqlLiteManager = new SqlLiteManager(context);
                        for (int j=0;j<loveLogBeens.size();j++){
                            loveLogBeens.get(j).setLogID(null);
                            sqlLiteManager.updateOrInsertLoveLog(loveLogBeens.get(j));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("getLoveL","error:" + throwable.getMessage());

                }
            });
        }catch (Exception e){
            Log.i("LoveLog","throw Exception!" );
        }
    }


    /**
     * 将数据库中之前没有指教成功的往服务器上提交
     * @param context Activity上下文
     * */
    public void updateFormDataBase(Context context){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userID = sharedPreferencesManager.readString("UserID");
        String secretKey = sharedPreferencesManager.readString("secretKey");
        SqlLiteManager sqLiteDatabase = new SqlLiteManager(context);

        try{
            url = "updateLoveLog.action";
            params = new RequestParams();
            params.add("userID",userID);
            params.add("secretKey",secretKey);
            LoveLogBean loveLogBeen= sqLiteDatabase.getOneLoveLogBeanByUserId(userID);
            if(loveLogBeen==null){
                return;
            }
            params.add("jsonString",new Gson().toJson(loveLogBeen));
            HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    httpResponseStr = new String(bytes);
                    UpdateModel updateModel = new Gson().fromJson(httpResponseStr, UpdateModel.class);
                    if (updateModel!=null){
                        if (updateModel.getStatus() == 1){
                            //当更新成功时,更新本地数据库中的updateFlag值,值为1时则已经成功上传到远程服务器
                            loveLogBeen.setUpdateFlag(1);
                            SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
                            sqlLiteManager.updateOrInsertLoveLog(loveLogBeen);
                        }
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i("updateFormDatabase","update fail");
                }
            });
        }catch (Exception e){
            Log.i(".updateFormDatabase","throw Exception:"  + e.getMessage());
        }
    }


    /**
     * 找回密码的业务逻辑
     * */
    public void forgrtPasswd(Context context,String userId,Listener listener){
        //判断userI格式的合法性
        if (userId == null  || userId.length() <5){
            listener.onFailure("请先输入用户名");
            return;
        }

        url = "sendForgetEmail.action";
        params = new RequestParams();
        params.add("userId",userId);

        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                //获取返回的Json串
                httpResponseStr = new String(bytes);
                //构造实体类
                try {
                    SimpleModel simpleModel = new Gson().fromJson(httpResponseStr,SimpleModel.class);
                    if (simpleModel.getStatus() == 1){
                        //验证成功
                        listener.onSuccess();
                    }else{
                        listener.onFailure("找回密码失败，原因："+ simpleModel.getMsg());
                    }
                }catch (Exception e){
                    listener.onFailure("找回密码失败，原因：" + "服务器发生错误");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onFailure("找回密码失败，原因：" + throwable.getLocalizedMessage());
            }
        });
    }
}
