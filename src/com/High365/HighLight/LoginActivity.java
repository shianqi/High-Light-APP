package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private UserInfoService userInfoService = new UserInfoService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        usernameTextView=(EditText)findViewById(R.id.userNameText);
        passwordTextView=(EditText)findViewById(R.id.passwordText);
        loginButton=(Button)findViewById(R.id.LoginButton);

        inti();

        userInfoService.login(usernameTextView.getText().toString(), passwordTextView.getText().toString(), new Listener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            }
        }, this);
    }

    /**
     * 初始化
     */
    private void inti(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 给登陆按钮绑定监听函数，用于处理登陆请求
             * @param v view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MyActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }



}
