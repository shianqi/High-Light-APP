package com.High365.HighLight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 注册界面
 * @author 史安琪
 */
public class RegistActivity extends Activity{

    private String nickname;
    private int sex = 1;
    private String birthday;
    private String password;

    private EditText nicknameText;
    private EditText sexText;
    private EditText birthdayText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_page);

        nicknameText=(EditText)findViewById(R.id.userNameText);
        sexText=(EditText)findViewById(R.id.userSexText);
        birthdayText=(EditText)findViewById(R.id.birthdayText);
        passwordText=(EditText)findViewById(R.id.passwordText);

        sexText.setOnClickListener(new View.OnClickListener() {
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
    }
}
