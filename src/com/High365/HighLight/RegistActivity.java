package com.High365.HighLight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 用户注册界面
 * @author shianqi@imudges.com
 */
public class RegistActivity extends Activity{

    /**
     * 用户名，长度不超过16位
     */
    private String username;
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

    private EditText usernameText;
    private EditText sexText;
    private EditText birthdayText;
    private EditText passwordText;
    private Button registButton;

    /**
     * 状态码，表示成功
     */
    final private int SUCCESS = 1;
    /**
     * 状态码，表示失败
     */
    final private int FAILURE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_page);

        usernameText=(EditText)findViewById(R.id.userNameText);
        sexText=(EditText)findViewById(R.id.userSexText);
        birthdayText=(EditText)findViewById(R.id.birthdayText);
        passwordText=(EditText)findViewById(R.id.passwordText);
        registButton=(Button)findViewById(R.id.registButton);

        setListener();
    }

    /**
     * 绑定监听
     */
    private void setListener(){
        /**
         * 此处点击文本框修改用户性别，方便用户输入
         * sex  1为男，0为女
         * @param v view
         */
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
                //ToastManager.toast(getApplicationContext(),"昵称输入的不对哦");
                //数据格式在服务器端验证
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                birthday = birthdayText.getText().toString();
                //点击后设置按钮不可用,防止重复点击
                registButton.setEnabled(false);
                UserInfoService userInfoService = new UserInfoService();
                userInfoService.register(username, password, sex, birthday, RegistActivity.this, new Listener() {
                    @Override
                    public void onSuccess() {
                        Message message = new Message();
                        message.what =SUCCESS;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Message message = new Message();
                        message.what=FAILURE;
                        message.obj = "注册失败,原因:" + msg;
                        handler.sendMessage(message);
                    }
                });
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
     * 注册成功时调用此方法
     * 注册成功后跳转页面
     * */
    public void onSuccess(){
        ToastManager.toast(this,"注册成功");
        Intent intent = new Intent(this, MyActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    /**
     * 注册失败时调用此方法
     * 使注册按钮可用,并提示
     */
    public void onFailure(String msgStr){
        //设置注册按钮可用,使用户能够修改注册信息后重新注册
        registButton.setEnabled(true);
        ToastManager.toast(this,msgStr);
    }

    /**
     * 重写返回键，让程序返回登陆界面
     * @param keyCode keyCode
     * @param event event
     * @return 事件是否成功
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            goLoginActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 回到登陆界面
     */
    public void goLoginActivity(){
        Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
        RegistActivity.this.startActivity(intent);
        RegistActivity.this.finish();
    }
}
