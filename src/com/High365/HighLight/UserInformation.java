package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class UserInformation extends Activity {
    private Button fixUserInformationButton;
    private Button fixPasswordButton;
    private Button fixGraphPasswordButton;
    private Button logoutButton;
    private TextView usernameTextView;
    private TextView sexTextView;
    private EditText birthdayEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    public UserEntity userEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);

        init();
        //此处获取用户信息
        getUserInformation();
    }

    private void getUserInformation() {
        //获取用户信息后替换输入框中的“加载中”
    }

    /**
     * 绑定id
     */
    private void init(){
        userEntity=new UserEntity();
        fixUserInformationButton=(Button)findViewById(R.id.fixUserInformationButton);
        fixPasswordButton=(Button)findViewById(R.id.fixPasswordButton);
        fixGraphPasswordButton=(Button)findViewById(R.id.fixGraphPasswordButton);
        logoutButton=(Button)findViewById(R.id.logoutButton);

        usernameTextView=(TextView)findViewById(R.id.usernameTextView);
        sexTextView=(TextView)findViewById(R.id.sexTextView);
        birthdayEditText=(EditText)findViewById(R.id.birthdayEditText);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        phoneEditText=(EditText)findViewById(R.id.phoneEditText);


        /**
         * 点一下进入信息可修改模式
         * 并将按钮变成可修改模式
         */
        fixUserInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEntity.isFixAble()){
                    //储存编辑后的信息
                    birthdayEditText.setEnabled(false);
                    userEntity.setFixAble(false);
                }else{
                    birthdayEditText.setEnabled(true);
                    userEntity.setFixAble(true);
                    //else
                }
            }
        });

        fixPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fixGraphPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSetPassword();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sexTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_DOWN){
            goMainPage();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 切换到主界面
     */
    public void goMainPage(){
        Intent intent = new Intent(UserInformation.this, MyActivity.class);
        UserInformation.this.startActivity(intent);
        UserInformation.this.finish();
    }

    /**
     * 切换到登陆
     */
    public void goSetPassword(){
        Intent intent = new Intent(UserInformation.this, SetGraphPasswordActivity.class);
        UserInformation.this.startActivity(intent);
        UserInformation.this.finish();
    }
}
