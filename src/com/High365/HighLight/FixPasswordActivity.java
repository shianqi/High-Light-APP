package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 修改密码界面
 */
public class FixPasswordActivity extends Activity{
    Button verifyFix;
    EditText oldPassword;
    EditText newPassword1;
    EditText newPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixpassword_activity);

        init();
    }

    public void init(){
        verifyFix=(Button)findViewById(R.id.verifyFixButton);
        oldPassword=(EditText)findViewById(R.id.oldPassword);
        newPassword1=(EditText)findViewById(R.id.newPassword1);
        newPassword2=(EditText)findViewById(R.id.newPassword2);

        verifyFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查两次输入的新密码是否相同
                //修改密码
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            goUserInformationActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goUserInformationActivity(){
        Intent intent = new Intent(FixPasswordActivity.this, UserInformationActivity.class);
        FixPasswordActivity.this.startActivity(intent);
        FixPasswordActivity.this.finish();
    }
}
