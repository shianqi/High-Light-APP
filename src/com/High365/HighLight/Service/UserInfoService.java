package com.High365.HighLight.Service;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import com.High365.HighLight.Bean.UpdateModel;
import com.High365.HighLight.Bean.UserInfoBean;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Util.HttpRequest;
import com.High365.HighLight.Util.MD5;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.google.gson.Gson;
import android.os.Looper;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;

/**
 * 这个类主要负责UserInfo的业务逻辑的处理和本地数据库的访问<br>
 * @author hupeng
 * @version 1.0
 * 此类继承于线程类
 * url:服务器的url地址的一部分,IP地址与端口号在HttpRequest类中定义,这样做只需要修改那边的HOST的值就可以方便的更换服务器的地址与参数<br>
 * param:参数<br>
 * httpResponseOutputStreamString:服务器所返回的json字符串<br>
 * 由于Android的网络通信模块必须放在子线程中,若放在主线程中会导致阻塞主线程.<br>
 */
public class UserInfoService extends Thread{

    String url;
    String param;
    private String WP;
    String httpResponseOutputStreamString;
    Listener listener;
    Integer taskId;
    Context context;
    UserInfoBean userInfoBean;
    /**
     * 登陆方法
     * @param userID 用户名
     * @param password 密码
     * @param listener 实现回调的接口
     * @param context context对象
     * 成功回调:onSuccess,失败回调:onFailure.
     * */
    public void login(String userID,String password,Listener listener,Context context){

        this.listener = listener;
        this.context = context;
        WP = new MD5().encryptPassword(password);

        //登陆操作前首先判断

        //首先,判断用户名密码是否在本地数据库中,若在,判断密码的一致性,则直接登录成功
        SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
        userInfoBean = sqlLiteManager.findUserInfoById(userID);
        if (userInfoBean!=null){
            if (userInfoBean.getUserPwd()!=null && userInfoBean.getUserPwd().equals(WP)){
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
                sharedPreferencesManager.writeString("UserID",userID);
                sharedPreferencesManager.writeString("WP",WP);
                listener.onSuccess();
                return;
            }
        }
        //执行网络请求,在线判断密码的正确与否
        url = "login.action";
        param = "username=" + userID + "&password=" + password;
        taskId = 0;
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this.context);
        sharedPreferencesManager.writeString("UserID",userID);
        start();
    }

    /**
     * 注册方法,必须联网才能注册
     * @param context context对象
     * @param userID 用户ID
     * @param password 用户密码
     * @param gender 用户的性别
     * @param birthDay 用户生日
     * @param listener 实现回调的接口
     * 成功回调:onSuccess,失败回调:onFailure.
     */
    public void register(String userID,String password,int gender,String birthDay,Context context,Listener listener){
        this.listener = listener;
        this.context = context;
        url= "register.action";
        param = "username=" + userID + "&password=" + password + "&gender=" + gender + "&birthDay=" + birthDay;
        taskId = 1;
        userInfoBean = new UserInfoBean();
        userInfoBean.setUserId(userID);
        userInfoBean.setUserPwd(password);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            userInfoBean.setUserBirthDay(simpleDateFormat.parse(birthDay));
        }catch (Exception e){
            e.printStackTrace();
        }
        userInfoBean.setUserGender(gender);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this.context);
        sharedPreferencesManager.writeString("UserID",userID);
        start();
    }

    /**
     * 执行用户信息更新的方法
     * @param userInfoBean 存放用户信息的对象
     * @param context context对象
     * @param listener 实现回调的接口
     * 成功回调:onSuccess,失败回调:onFailure.
     * */
    public void updateUserInfo(UserInfoBean userInfoBean,Context context,Listener listener){
        this.listener = listener;
        this.context = context;
        SharedPreferencesManager spm = new SharedPreferencesManager(context);
        url = "updateUserInfo.action";
        param = "userID=" + spm.readString("UserID") + "&secretKey=" + spm.readString("secretKey") + "&userInfoJson=" + new GsonBuilder().setDateFormat("yyyy-MM-dd").disableHtmlEscaping().create().toJson(userInfoBean);
        taskId = 2;
        new SqlLiteManager(context).saveOrupdateUserInfo(userInfoBean);
        start();
    }

    /**
     * 从服务器端获取用户身份,获得数据后直接将本地数据库中的数据进行更新,由于不需要直接进行UI操作,因此不需要使用监听器
     * @param context context对象
     * */
    public void getUserInfo(Context context){
        this.context = context;
        SharedPreferencesManager spm = new SharedPreferencesManager(context);
        url = "getUserInfo.action";
        param = "userID=" + spm.readString("UserID") + "&secretKey=" + spm.readString("secretKey");
        taskId = 3;
        start();
    }

    @Override
    public void run() {
        Looper.prepare();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        switch (taskId){
            case 0:
                //login
                try{
                    httpResponseOutputStreamString = HttpRequest.sendPost(url,param);
                    LoginModel loginModel = new Gson().fromJson(httpResponseOutputStreamString,LoginModel.class);
                    //判断登录结果
                    if (loginModel.getStatus().equals(1)){
                        SharedPreferences sharedPreferences = context.getSharedPreferences("config",context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("secretKey",loginModel.getSecretKey());
                        editor.commit();

                        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this.context);
                        sharedPreferencesManager.writeString("WP",WP);

                        System.out.println(loginModel.getSecretKey());

                        listener.onSuccess();
                        return;
                    }else {
                        listener.onFailure(loginModel.getErrorInfo());
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onFailure("网络连接失败");
                    return;
                }
            case 1:
                //Register
                try{
                    httpResponseOutputStreamString = HttpRequest.sendPost(url,param);
                    RegisterModel registerModel = new Gson().fromJson(httpResponseOutputStreamString,RegisterModel.class);
                    //判断注册结果
                    if (registerModel.getStatus().equals(1)){
                        //写入secetKey到SharePreferences中
                        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
                        sharedPreferencesManager.writeString("secretKey",registerModel.getSecretKey());
                        //写入用户的注册数据到UserInfo表中
                        SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
                        SQLiteDatabase highLightDatabase = sqlLiteManager.getWritableDatabase();
                        //若原有记录则删除
                        String sql = "DELETE FROM UserInfo where userID = '"+ userInfoBean.getUserId()+"';";
                        highLightDatabase.execSQL(sql);
                        //插入新纪录
                        ContentValues cv = new ContentValues();
                        cv.put("UserID",userInfoBean.getUserId());
                        cv.put("UserPWD",userInfoBean.getUserPwd());
                        cv.put("UserGender",userInfoBean.getUserGender());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cv.put("UserBirthDay",sdf.format(userInfoBean.getUserBirthDay()));
                        highLightDatabase.insert("UserInfo",null,cv);
                        listener.onSuccess();
                        return;
                    }else{
                        listener.onFailure(registerModel.getErrorInfo());
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onFailure("网络连接失败");
                    return;
                }
            case 2:
                //Update
                try {
                    httpResponseOutputStreamString = HttpRequest.sendPost(url,param);
                    System.out.println(httpResponseOutputStreamString);
                    UpdateModel updateModel = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(httpResponseOutputStreamString,UpdateModel.class);
                    if (updateModel.getStatus() == 1){
                        listener.onSuccess();
                        return;
                    }else{
                        listener.onFailure(updateModel.getErrorInfo());
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    listener.onFailure("网络连接失败");
                    break;
                }
            case 3:
                //getUserInfo
                try {
                    httpResponseOutputStreamString = HttpRequest.sendPost(url,param);
                    UserInfoBean userInfoBean = gson.fromJson(httpResponseOutputStreamString,UserInfoBean.class);
                    if (userInfoBean==null){
                        return;
                    }
                    SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
                    sqlLiteManager.saveOrupdateUserInfo(userInfoBean);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
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

    /**
     * 此内部类为一个JavaBean
     * 为注册结果Json字符串所对应的bean
     * */
    class RegisterModel{

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

        private  Integer status;
        private String secretKey;
        private String errorInfo;

    }


}
