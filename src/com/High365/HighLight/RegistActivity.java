package com.High365.HighLight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 注册界面
 * @author 史安琪
 */
public class RegistActivity extends Activity{

    /**
     * 用户昵称，长度不超过16位
     */
    private String nickname;
    /**
     * 性别，1为男，0为女
     */
    private int sex = 1;
    /**
     * 用户生日，以8位字符串表示，例如：19900101
     */
    private String birthday;
    /**
     * 用户密码，长度不超过16位
     */
    private String password;

    private EditText nicknameText;
    private EditText sexText;
    private EditText birthdayText;
    private EditText passwordText;

    private Button registButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_page);

        nicknameText=(EditText)findViewById(R.id.userNameText);
        sexText=(EditText)findViewById(R.id.userSexText);
        birthdayText=(EditText)findViewById(R.id.birthdayText);
        passwordText=(EditText)findViewById(R.id.passwordText);

        registButton=(Button)findViewById(R.id.registButton);

        setListerner();


    }


    private void setListerner(){
        sexText.setOnClickListener(new View.OnClickListener() {
            /**
             * 此处点击文本框修改用户性别，方便用户输入
             * sex  1为男，0为女
             * @param v view
             */
            @Override
            public void onClick(View v) {
                if(sex==1){
                    sex=0;
                    sexText.setText("性别：女");
                }else{
                    sex=1;
                    sexText.setText("性别：男");
                }
            }
        });


        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * 注册按钮响应函数
             * @param v view
             */
            public void onClick(View v) {
                //此处进行本地检查用户输入是否合法
                //有不合法内容用Toast告诉用户，此处用我封装的Toast
                //例如： ToastManager.toast(getApplicationContext(),"昵称输入的不对哦");
                ToastManager.toast(getApplicationContext(),"昵称输入的不对哦");
            }
        });
    }
}
