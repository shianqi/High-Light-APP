package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * <p>程序加载界面</p>
 * 该段代码负责进入程序时显示欢迎界面，并在2秒后根据用户登陆情况分别
 * 跳转到不同的页面<br>
 * 根据用户登陆的不同情况，向Handler类的handleMessage(Message msg)方法传递不同的状态码<br>
 * handleMessage(Message msg)接收到不同的状态码，从而跳转到不同界面。
 * @author 史安琪
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        init();
    }

    /**
     * 初始化页面，此处调用登陆状态判断函数
     */
    private void init(){
        handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
        //此处进行登陆判断，并跳转。修改第一个参数就好。
        //例如跳转到登陆界面： handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
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
}
