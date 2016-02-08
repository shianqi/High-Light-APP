package com.High365.HighLight;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登陆页面的处理逻辑
 * @author shianqi@imudges.com
 * @version 1.0
 */
public class LoginActivity extends Activity {
    /**
     * 填写用户名的文本框
     */
    private EditText usernameTextView;
    /**
     * 填写密码的文本框
     */
    private EditText passwordTextView;
    /**
     * 登陆时点击的按钮
     */
    private Button loginButton;

    /**
     * 执行用户登录时的业务逻辑所在的类
     */
    private UserInfoService userInfoService;

    /**
     * 跳转到注册界面
     */
    private TextView registLink;

    /**
     * 忘记密码
     */
    private TextView forgetPassword;

    /**
     * 定义常量
     * */
    final private int SUCCESS=1;
    final private int FAILURE=0;
    private long EXITTIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        usernameTextView=(EditText)findViewById(R.id.userNameText);
        passwordTextView=(EditText)findViewById(R.id.passwordText);
        loginButton=(Button)findViewById(R.id.LoginButton);
        forgetPassword=(TextView)findViewById(R.id.forgetPassword);
        registLink=(TextView)findViewById(R.id.registLink);

        init();
    }

    /**
     * 初始化
     */
    private void init(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 给登陆按钮绑定监听函数，用于处理登陆请求
             * @param v view
             */
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                userInfoService = new UserInfoService();
                userInfoService.login(usernameTextView.getText().toString(), passwordTextView.getText().toString(), new Listener() {
                    @Override
                    public void onSuccess() {
                        Message message = new Message();
                        message.what = SUCCESS;
                        message.obj = "登录成功";
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Message message = new Message();
                        message.what = FAILURE;
                        message.obj = "登录失败,失败原因:" + msg;

                        handler.sendMessage(message);
                    }
                }, LoginActivity.this);

            }
        });

        //给两个链接加下划线
        registLink.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        forgetPassword.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        registLink.setOnClickListener(new View.OnClickListener() {
            /**
             * 给切换到注册界面链接绑定
             * @param v view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
            }
        });
    }

    /**
     * 线程间的通信,接受不同的消息请求，做出处理,因为网络请求在子线程中完成,而要在主线程UI上显示网络请求的结果必须要经过线程间通信
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS:
                    onSuccess();
                    break;
                case FAILURE:
                    String msgStr = (String) msg.obj;
                    onFailure(msgStr);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 登录成功时所做的操作
     * */
    void onSuccess(){
        Intent intent = new Intent(LoginActivity.this, MyActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }
    /**
     * 登录失败时所做的操作
     * */
    void onFailure(String msg){
        ToastManager.toast(this,msg);
        loginButton.setEnabled(true);
    }

    /**
     * 设置双击返回退出程序
     * @param keyCode keyCode 按键
     * @param event event 事件
     * @return true or false
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-EXITTIME) > 2000){
                ToastManager.toast(getApplicationContext(),"再按一次退出程序");
                EXITTIME = System.currentTimeMillis();
            } else {
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                manager.restartPackage(getPackageName());
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
