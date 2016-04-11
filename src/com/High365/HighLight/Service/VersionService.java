package com.High365.HighLight.Service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.High365.HighLight.Bean.VersionModel;
import com.High365.HighLight.Interface.VersionListener;
import com.High365.HighLight.Util.HttpRequest;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

/**
 * 版本服务类，用于获得当前版本以及版本更新.
 */
public class VersionService {

    public static String getAppVersionName(Context context) {
        String versionName = "";
        Integer versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }

        return versionName;
    }

    public static Integer getAppVersionCode(Context context) {
        String versionName = "";
        Integer versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
           if (versionCode == null){
               return 0;
           }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }


    public void updateVersionListener(Context context, VersionListener listener){
        RequestParams params = new RequestParams();
        String url = "updateVersion.action";
        params.put("versionCode",getAppVersionCode(context)+"");

        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String reponseStr = new String(bytes);
                try {
                    VersionModel versionModel = new Gson().fromJson(reponseStr,VersionModel.class);
                    if (versionModel.getState() == 1){
                        listener.onSuccess(versionModel.getUrl());
                        return;
                    }else{
                        listener.onFailure("未找到新版本");
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onFailure("连接服务器时发生错误");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onFailure("连接到服务器失败");
            }
        });

    }


}
