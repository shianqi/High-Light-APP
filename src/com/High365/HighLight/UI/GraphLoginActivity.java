package com.High365.HighLight.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.High365.HighLight.UI.LocusPassWordView.OnCompleteListener;
import com.High365.HighLight.R;
import com.High365.HighLight.Util.ToastManager;

/**
 * 此文件是九宫格图形锁的界面(这个工具是互联网上的开源代码,此处不做详细介绍)<br>
 *     主要负责处理图形登陆的逻辑<br>
 *     详细内容请参考<a href="http://www.cnblogs.com/weixing/p/3413998.html">Android之九宫格解锁的实现</a>
 */
public class GraphLoginActivity extends Activity {
    /**
     * 图形密码登陆界面的view
     */
    private LocusPassWordView lpwv;
    private static final long DELAY_TIME = 300;
    private final int RESET = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_login_page);
        lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
        lpwv.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String mPassword) {
                // 如果密码正确,则进入主页面。
                if (lpwv.verifyPassword(mPassword)) {
                    ToastManager.toast(getApplication(),"登陆成功！");
                    Intent intent = new Intent(GraphLoginActivity.this,
                            MyActivity.class);
                    // 打开新的Activity
                    startActivity(intent);
                    finish();
                } else {
                    ToastManager.toast(getApplication(),"密码输入错误,请重新输入");
                    lpwv.markError();
                    handler.sendEmptyMessageDelayed(RESET,DELAY_TIME);
                    lpwv.reset();
                }
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RESET:
//                    ToastManager.toast(getApplication(),"11");
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 如果密码为空,则进入设置密码的界面
        View noSetPassword = (View) this.findViewById(R.id.tvNoSetPassword);
        TextView toastTv = (TextView) findViewById(R.id.login_toast);
        if (lpwv.isPasswordEmpty()) {
            lpwv.setVisibility(View.GONE);
            noSetPassword.setVisibility(View.VISIBLE);
            toastTv.setText("请先绘制手势密码");
            noSetPassword.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GraphLoginActivity.this,
                            SetGraphPasswordActivity.class);
                    // 打开新的Activity
                    startActivity(intent);
                    finish();
                }

            });
        } else {
            toastTv.setText("请输入手势密码");
            lpwv.setVisibility(View.VISIBLE);
            noSetPassword.setVisibility(View.GONE);
        }
    }

    /**
     * 回到登陆界面
     */
    public void goLoginActivity(){
        Intent intent = new Intent(GraphLoginActivity.this, LoginActivity.class);
        GraphLoginActivity.this.startActivity(intent);
        GraphLoginActivity.this.finish();
    }

    /**
     * 监听返回键
     * @param keyCode 按键
     * @param event 事件
     * @return 按键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            goLoginActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
