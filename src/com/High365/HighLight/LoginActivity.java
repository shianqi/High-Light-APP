package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登陆页面的处理逻辑
 * @author 史安琪
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
     * */
    private UserInfoService userInfoService;

    /**
     * 定义常量
     * */
    final private int SUCCESS=1;
    final private int FAILURE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        usernameTextView=(EditText)findViewById(R.id.userNameText);
        passwordTextView=(EditText)findViewById(R.id.passwordText);
        loginButton=(Button)findViewById(R.id.LoginButton);

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
                        message.what = 1;
                        message.obj = "登录成功";
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = "登录失败,失败原因:" + msg;

                        handler.sendMessage(message);
                    }
                }, LoginActivity.this);

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

    void onSuccess(){
        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MyActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }

    void onFailure(String msg){
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
    }
}
