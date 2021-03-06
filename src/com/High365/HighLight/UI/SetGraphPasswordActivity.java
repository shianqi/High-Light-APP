package com.High365.HighLight.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.High365.HighLight.Service.UserInfoService;
import com.High365.HighLight.UI.LocusPassWordView.OnCompleteListener;
import com.High365.HighLight.R;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.StringUtil;

/**
 * 修改图形锁密码界面<br>
 *     此文件是九宫格图形锁的工具类(这个工具是互联网上的开源代码,不做详细介绍)<br>
 *     详细内容请参考<a href="http://www.cnblogs.com/weixing/p/3413998.html">Android之九宫格解锁的实现</a>
 * @author unknown
 * @version unknown
 */
public class SetGraphPasswordActivity extends Activity {
    private LocusPassWordView lpwv;
    private String password;
    private boolean needverify = true;
    private Toast toast;
    TextView toastTv;
    private Button buttonSave;
    private Button tvReset;

    private void showToast(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpassword_activity);

        buttonSave = (Button) this.findViewById(R.id.tvSave);
        tvReset = (Button) this.findViewById(R.id.tvReset);
        toastTv = (TextView) findViewById(R.id.promptText);

        lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplication());


        if(new UserInfoService().isFirstLogin(sharedPreferencesManager.readString("UserID"),getApplication())){

        }else {
            toastTv.setText("请先输入原密码");
            buttonSave.setVisibility(View.INVISIBLE);
            tvReset.setVisibility(View.INVISIBLE);
        }

        buttonSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isNotEmpty(password)) {
                    lpwv.resetPassWord(password);
                    lpwv.clearPassword();
                    showToast("密码修改成功,请记住密码.");
                    startActivity(new Intent(SetGraphPasswordActivity.this,
                            MyActivity.class));
                    finish();
                } else {
                    lpwv.clearPassword();
                    showToast("密码不能为空,请输入密码.");
                }
            }
        });

        tvReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lpwv.clearPassword();
            }
        });



        lpwv.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String mPassword) {
                password = mPassword;
                if (needverify) {
                    if (lpwv.verifyPassword(mPassword)) {
                        buttonSave.setVisibility(View.VISIBLE);
                        tvReset.setVisibility(View.VISIBLE);
                        showToast("密码输入正确,请输入新密码!");
                        toastTv.setText("请输入新密码");
                        lpwv.clearPassword();
                        needverify = false;
                    } else {
                        showToast("错误的密码,请重新输入!");
                        lpwv.clearPassword();
                        password = "";
                    }
                }
            }
        });

        // 如果密码为空,直接输入密码
        if (lpwv.isPasswordEmpty()) {
            this.needverify = false;
            showToast("请输入密码");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
