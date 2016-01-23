package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends Activity {

    private static final long DELAY_TIME = 2000;
    private static final int GOHOME = 1000;
    private static final int LOGIN = 1001;
    private static final int REGIST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        init();
    }

    /**
     * 初始化页面
     */
    private void init(){
        handler.sendEmptyMessageDelayed(LOGIN,DELAY_TIME);
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
    private void regist(){}
}
