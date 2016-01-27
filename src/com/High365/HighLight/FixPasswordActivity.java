package com.High365.HighLight;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.KeyEvent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    final private int SUCCESS = 1;
    final private int FAILURE = 0;
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
                String oldPass = oldPassword.getText().toString();
                String newPass1 = newPassword1.getText().toString();
                String newPass2 = newPassword2.getText().toString();

                //检查两次的密码是否相同
                if (!newPass1.equals(newPass2)){
                    ToastManager.toast(FixPasswordActivity.this,"两次输入的新密码不一致");
                    return;
                }
                //检查新密码长度,默认长度必须大于等于6
                if (newPass1.length()<6){
                    ToastManager.toast(FixPasswordActivity.this,"新密码长度过短");
                    return;
                }
                //检查输入的旧密码是否正确,跟本地数据库中的数据进行比对
                SqlLiteManager sqlLiteManager = new SqlLiteManager(FixPasswordActivity.this);
                SQLiteDatabase highLightDB = sqlLiteManager.getWritableDatabase();
                String sql = "select * from userInfo where userID = '" + new SharedPreferencesManager(FixPasswordActivity.this).readString("UserID") + "';";
                Cursor cursor = highLightDB.rawQuery(sql,null);
                String currentOldPass=null;
                if (cursor.moveToFirst()){
                    currentOldPass = cursor.getString(cursor.getColumnIndex("UserPWD"));
                }
                //旧密码是否正确在服务器端进行判断
//                if (!oldPass.equals(currentOldPass)){
//                    ToastManager.toast(FixPasswordActivity.this,"旧密码不正确");
//                    return;
//                }
                //发送请求到远程服务器请求更改密码,更改成功后修改本地数据库中的密码
                //首先暂存新密码,等到密码修改成功后,将暂存的新密码写入数据库
                SharedPreferencesManager spm = new SharedPreferencesManager(FixPasswordActivity.this);
                spm.writeString("tempNewPWD",newPass1);
                //执行网络请求进行数据更新操作
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setUserPwd(newPass1);
                UserInfoService userInfoService = new UserInfoService();
                userInfoService.updateUserInfo(userInfoBean, FixPasswordActivity.this, new Listener() {
                    @Override
                    public void onSuccess() {
                        Message message = new Message();
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Message message = new Message();
                        message.what = FAILURE;
                        message.obj = "密码更新失败,失败原因:" + msg;
                        handler.sendMessage(message);
                    }
                });
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
     * 更新成功时所做的操作
     * */
    void onSuccess(){
        ToastManager.toast(FixPasswordActivity.this,"密码更新成功!");
        //读出数据
        SharedPreferencesManager spm = new SharedPreferencesManager(FixPasswordActivity.this);
        String tempNewPass = spm.readString("tempNewPWD");
        String userID = spm.readString("UserID");
        //获得数据库实例,更新数据库中的用户密码
        SQLiteDatabase highLightDB = new SqlLiteManager(FixPasswordActivity.this).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserPwd",tempNewPass);
        try{
            highLightDB.update("userInfo",cv,"userID=?",new String[]{userID});
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 更新失败时所做的操作
     * */
    void onFailure(String msg){
        ToastManager.toast(FixPasswordActivity.this,msg);
    }

}
