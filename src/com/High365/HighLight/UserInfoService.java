package com.High365.HighLight;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

/**
 * @author hupeng
 * 这个类主要负责UserInfo的业务逻辑的处理和本地数据库的访问
 */
public class UserInfoService {




    /**
     * @param username 用户名
     * @param password 密码
     * 成功回调:onSuccess,失败回调:onFailure.
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

    /**
     * 此类为一个线程类
     * url:服务器的url地址的一部分,IP地址与端口号在HttpRequest类中定义,这样做只需要修改那边的HOST的值就可以方便的更换服务器的地址与参数
     * param:参数
     * httpResponseOutputStreamString:服务器所返回的json字符串
     * 由于Android的网络通信模块必须放在子线程中,若放在主线程中会导致阻塞主线程.
     * */
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

    /**
     * 此内部类为一个JavaBean
     * 为登录结果Json字符串所对应的bean
     * */
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

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }
        /**
         * status:表示返回的状态码,1:登录成功,0:登录失败
         * errorInfo:当登录失败的返回的失败信息
         * secretKey:
         * */
        private  Integer status;
        private String secretKey;
        private String errorInfo;
    }

}
