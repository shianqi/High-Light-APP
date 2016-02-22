package com.High365.HighLight.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.High365.HighLight.*;
import com.High365.HighLight.Bean.UserInfoBean;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.High365.HighLight.Util.ToastManager;

/**
 * <p>程序加载界面(导航页)</p>
 * 该段代码负责进入程序时显示欢迎界面，并在2秒后根据用户登陆情况分别
 * 跳转到不同的页面<br>
 * 根据用户登陆的不同情况，向Handler类的handleMessage(Message msg)方法传递不同的状态码<br>
 * handleMessage(Message msg)接收到不同的状态码，从而跳转到不同界面。
 * @author shianqi@imudges.com
 * @version 1.0
 */
public class WelcomeActivity extends Activity {

    /**
     * 跳转等待时间（毫秒）
     */
    private static final long DELAY_TIME = 2000;
    /**
     * 跳转到主页面的状态码
     */
    private static final int GOHOME = 1000;
    /**
     * 跳转到登陆页面的状态码
     */
    private static final int LOGIN = 1001;
    /**
     * 跳转到注册页面的状态码
     */
    private static final int REGIST = 1002;
    /**
     * 跳转到图形登陆页面的状态码
     */
    private static final int GRAPHLOGIN = 1003;
    /**
     * 跳转到设置图形登陆密码页面的状态码
     */
    private static final int GRAPHSETPASSWORD = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        init();
    }

    /**
     * 初始化页面，此处调用登陆状态判断函数<br>
     * 首先先在SharedPreferences中查找是否有上次用户登陆过的账号和密码(经过加密)，
     * 有就用这个账号密码和数据库里面的账号密码对应，相同就跳转到图形界面上。
     * 密码不相同，或者没有就切换到正常登陆。
     */
    private void init(){
        //handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplication());
        Log.i("用户信息",sharedPreferencesManager.readString("UserID")+" "+sharedPreferencesManager.readString("WP"));
        if(sharedPreferencesManager.readString("UserID")!=null&&sharedPreferencesManager.readString("WP")!=null){
            SqlLiteManager sqlLiteManager = new SqlLiteManager(getApplication());
            UserInfoBean userInfoBean = sqlLiteManager.findUserInfoById(sharedPreferencesManager.readString("UserID"));
            if (userInfoBean!=null){
                if (userInfoBean.getUserPwd()!=null && userInfoBean.getUserPwd().equals(sharedPreferencesManager.readString("WP"))){
                    handler.sendEmptyMessageDelayed(GRAPHLOGIN,DELAY_TIME);
                }else{
                    handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
                }
            }else{
                ToastManager.toast(getApplication(),"错误提示：userBean为空");
                handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
            }
        }else{
            handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
        }
    }

    /**
     * 接受不同的消息请求，做出处理
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOHOME:
                    goHome();
                    break;
                case LOGIN:
                    login();
                    break;
                case REGIST:
                    regist();
                    break;
                case GRAPHLOGIN:
                    graphLogin();
                    break;
                case GRAPHSETPASSWORD:
                    setGraphPassword();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 切换到主页面
     */
    private void goHome(){
        Intent intent = new Intent(WelcomeActivity.this, MyActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    /**
     * 切换到登陆界面
     */
    private void login(){
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    /**
     * 切换到注册界面
     */
    private void regist(){
        Intent intent = new Intent(WelcomeActivity.this, RegistActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    /**
     * 切换到图形登陆界面
     */
    private void graphLogin(){
        Intent intent = new Intent(WelcomeActivity.this, GraphLoginActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    /**
     * 切换到设置图形登陆密码界面
     */
    private void setGraphPassword(){
        Intent intent = new Intent(WelcomeActivity.this, SetGraphPasswordActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
