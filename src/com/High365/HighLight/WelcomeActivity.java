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
        handler.sendEmptyMessageDelayed(GOHOME,DELAY_TIME);
    }

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

    private void goHome(){
        Intent intent = new Intent(WelcomeActivity.this, MyActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private void login(){

    }

    private void regist(){}
}
