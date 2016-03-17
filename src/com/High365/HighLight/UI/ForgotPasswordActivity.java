package com.High365.HighLight.UI;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.R;
import com.High365.HighLight.Service.UserInfoService;
import com.High365.HighLight.Util.ToastManager;

/**
 * 密码找回界面
 * Created by killer on 2016/3/16.
 */
public class ForgotPasswordActivity extends Activity{
    private EditText username;
    private EditText email;
    private Button button;
    private UserInfoService userInfoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        init();
    }

    /**
     * 初始化组件，渲染界面
     */
    public void init(){
        username = (EditText)findViewById(R.id.forgotPasswordUsername);
        email = (EditText)findViewById(R.id.forgotPasswordEmailAddress);
        button = (Button)findViewById(R.id.forgetPasswordButton);
        userInfoService = new UserInfoService();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInfoService.forgrtPasswd(getApplicationContext(), username.getText().toString(), email.getText().toString(), new Listener() {
                    @Override
                    public void onSuccess() {
                        showDialog("验证邮件已经发送到您的邮箱，请注意查收。");
                    }

                    @Override
                    public void onFailure(String msg) {
                        showDialog(msg);
                    }
                });

            }
        });
    }

    /**
     * 是否保存Dialog显示
     */
    public void showDialog(String msg){
        new AlertDialog.Builder(ForgotPasswordActivity.this)
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
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
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            ForgotPasswordActivity.this.startActivity(intent);
            ForgotPasswordActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
