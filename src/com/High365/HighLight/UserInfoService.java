package com.High365.HighLight;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;

/**
 * @author hupeng
 * 这个类主要负责UserInfo的业务逻辑的处理和本地数据库的访问
 */
public class UserInfoService {




    /**
     * @param username 用户名
     * @param password 密码
     * @return true:登陆成功,false:登陆失败
     * */
    public void login(String username,String password,Listener listener,Context context){
        String url = null;
        String param = null;
        String httpRespoenseString = null;


        url = "login.action";
        param = "username=" + username + "&password=" + password;

        MyThread myThread = new MyThread(url,param);

        try{
            myThread.start();
            httpRespoenseString = myThread.getHttpResponseOutputStreamString();
            LoginModel loginModel = new Gson().fromJson(httpRespoenseString,LoginModel.class);
            if (loginModel.getStatus().equals(1)){
                SharedPreferences sharedPreferences = context.getSharedPreferences("config",context.MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("secretKey",loginModel.getSecretKey());
                editor.commit();
                listener.onSuccess();
                return;
            }else {
                listener.onFailure("密码错误");
                return;
            }
        }catch (Exception e){
            listener.onFailure("网络连接失败");
            return;
        }
    }

    class MyThread extends Thread{
        String url;
        String param;
        String httpResponseOutputStreamString;

        public MyThread(String url,String param){
            this.url = url;
            this.param = param;
        }

        @Override
        public void run() {
            httpResponseOutputStreamString = HttpRequest.sendPost(url,param);
        }


        public String getHttpResponseOutputStreamString() {
            return httpResponseOutputStreamString;
        }
    }

    class LoginModel{
        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        private  Integer status;
        private String secretKey;

    }

}
