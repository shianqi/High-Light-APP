package com.High365.HighLight.UI;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.High365.HighLight.*;
import com.High365.HighLight.Bean.UserInfoBean;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Service.UserInfoService;
import com.High365.HighLight.Util.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 显示修改用户信息界面<br>
 * @author shianqi@imudges.com
 * @version 1.0
 * 此页面的功能如下：<br>
 *     1.显示用户基本信息。<br>
 *     2.修改用户信息。<br>
 *     3.修改用户头像（可以从本地图库中通过裁剪得到，或者拍照上传头像）。<br>
 *     4.跳转到修改密码，图形密码界面。<br>
 *     5.退出当前已经登陆的用户。<br>
 */
public class UserInformationActivity extends Activity {
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    /**
     * 锁定状态
     */
    private int lockState = 2;
    /**
     * 状态码，表示成功
     */
    final private int SUCCESS = 1;
    /**
     * 状态码，表示失败
     */
    final private int FAILURE = 0;
    /**
     * 图片文件名字
     */
    private static final String IMAGE_FILE_NAME = "faceImage_11.jpg";

    /**
     * 用户头像控件
     */
    private ImageView userPhoto;
    /**
     * 修改用户信息按钮
     */
    private Button fixUserInformationButton;
    /**
     * 修改密码按钮
     */
    private Button fixPasswordButton;
    /**
     * 修改图形锁密码按钮
     */
    private Button fixGraphPasswordButton;
    /**
     * 退出登陆按钮
     */
    private Button logoutButton;
    /**
     * 用户名文字显示组件
     */
    private TextView usernameTextView;
    /**
     * 用户昵称文字编辑组件
     */
    private EditText nicknameEditText;
    /**
     * 性别文字显示组件
     */
    private TextView sexTextView;
    /**
     * 用户生日文字编辑组件
     */
    private EditText birthdayEditText;
    /**
     * 用户邮箱文字编辑组件
     */
    private EditText emailEditText;
    /**
     * 用户手机号文字编辑组件
     */
    private EditText phoneEditText;
    /**
     * 存放用户信息的对象
     */
    private UserInfoBean userInfoBean;

    /**
     * 选择上传方式时弹出菜单文字
     */
    private String[] items = new String[] { "选择本地图片", "拍照" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);

        init();
    }

    /**
     * 绑定各个元素的id,初始化控件属性,初始化控件值
     */
    private void init(){
        userInfoBean=new UserInfoBean();
        userInfoBean.setFixAble(false);
        userPhoto=(ImageView)findViewById(R.id.userPhoto);
        fixUserInformationButton=(Button)findViewById(R.id.fixUserInformationButton);
        fixPasswordButton=(Button)findViewById(R.id.fixPasswordButton);
        fixGraphPasswordButton=(Button)findViewById(R.id.fixGraphPasswordButton);
        logoutButton=(Button)findViewById(R.id.logoutButton);

        usernameTextView=(TextView)findViewById(R.id.usernameTextView);
        nicknameEditText=(EditText)findViewById(R.id.niceNameEditText);
        sexTextView=(TextView)findViewById(R.id.sexTextView);
        birthdayEditText=(EditText)findViewById(R.id.birthdayEditText);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        phoneEditText=(EditText)findViewById(R.id.phoneEditText);

        //初始化控件属性
        usernameTextView.setTextColor(Color.WHITE);
        sexTextView.setTextColor(Color.BLACK);
        nicknameEditText.setEnabled(false);
        nicknameEditText.setTextColor(Color.BLACK);
        birthdayEditText.setEnabled(false);
        birthdayEditText.setTextColor(Color.BLACK);
        emailEditText.setEnabled(false);
        emailEditText.setTextColor(Color.BLACK);
        phoneEditText.setEnabled(false);
        phoneEditText.setTextColor(Color.BLACK);

        //初始化控件值
        String userID = new SharedPreferencesManager(UserInformationActivity.this).readString("UserID");
        if (userID==null){
            return;
        }
        userInfoBean = new SqlLiteManager(UserInformationActivity.this).findUserInfoById(userID);
        if (userInfoBean!= null && userInfoBean.getUserGender()==0){
            sexTextView.setText("女");
        }else {
            sexTextView.setText("男");
        }
        nicknameEditText.setText(userInfoBean.getUserName());
        usernameTextView.setText(userInfoBean.getUserId());
        try {
            birthdayEditText.setText(new SimpleDateFormat("yyyyMMdd").format(userInfoBean.getUserBirthDay()));
        }catch(Exception e){
            birthdayEditText.setText("");
            e.printStackTrace();
        }
        emailEditText.setText(userInfoBean.getUserEmail());
        phoneEditText.setText(userInfoBean.getUserPhone());
        try{
            Bitmap bitmap = ImageEncodeUtil.base64ToBitmap(userInfoBean.getUserPhoto());
            if (bitmap != null){
                userPhoto.setImageDrawable(new BitmapDrawable(bitmap));
            }else{
                userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.mini_avatar));
            }

        }catch (Exception e){
            userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.mini_avatar));
            e.printStackTrace();
        }
        userInfoBean.setFixAble(false);
        /**
         * 点击图片选择相册或者拍照图片
         */
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoBean.setFixAble(true);
                nicknameEditText.setEnabled(true);
                birthdayEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                phoneEditText.setEnabled(true);
                birthdayEditText.setEnabled(true);
                fixUserInformationButton.setBackgroundColor(0xffaddb18);
                fixUserInformationButton.setText("确认修改");
                ToastManager.toast(getApplicationContext(),"信息已可修改");
                lockState = 0;
                showDialog();
            }
        });

        /**
         * 点一下进入信息可修改模式
         * 并将按钮变成可修改模式
         */
        fixUserInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfoBean.isFixAble()){
                    saveFixedUserInformatin();
                    userInfoBean.setFixAble(false);
                    nicknameEditText.setEnabled(false);
                    birthdayEditText.setEnabled(false);
                    emailEditText.setEnabled(false);
                    phoneEditText.setEnabled(false);
                    fixUserInformationButton.setText("修改信息");
                    fixUserInformationButton.setBackgroundColor(0xff3385ff);
                    saveFixedUserInformatin();
                }else{
                    userInfoBean.setFixAble(true);
                    nicknameEditText.setEnabled(true);
                    birthdayEditText.setEnabled(true);
                    emailEditText.setEnabled(true);
                    phoneEditText.setEnabled(true);
                    birthdayEditText.setEnabled(true);
                    fixUserInformationButton.setBackgroundColor(0xffaddb18);
                    fixUserInformationButton.setText("确认修改");
                    ToastManager.toast(getApplicationContext(),"信息已可修改");
                }

            }
        });


        /**
         * 为修改密码按钮绑定监听
         */
        fixPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFixPasswordPage();
            }
        });

        /**
         * 为修改手势密码按钮绑定监听
         */
        fixGraphPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFixGraphPasswordActivity();
            }
        });

        /**
         * 为修退出登陆码按钮绑定监听
         */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplication());
                sharedPreferencesManager.logout();
                goLoginActivity();
            }
        });
    }

    /**
     * 先将用户输入的内容提取出来，并初步判断是否合法，
     * 之后为用户修改信息
     */
    public void saveFixedUserInformatin(){
        //创建UserInfoBean对象
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUserId(new SharedPreferencesManager(UserInformationActivity.this).readString("UserID"));
        try{
            userInfoBean.setUserPhoto(ImageEncodeUtil.bitmapToBase64(((BitmapDrawable) (userPhoto.getDrawable())).getBitmap()));
        }catch (Exception e){}
        userInfoBean.setUserName(nicknameEditText.getText().toString());
        userInfoBean.setUserPhone(phoneEditText.getText().toString());
        try{
            userInfoBean.setUserBirthDay(new SimpleDateFormat("yyyyMMdd").parse(birthdayEditText.getText().toString()));
        }catch (Exception e){
            userInfoBean.setUserBirthDay(null);
            //return;
        }
        userInfoBean.setUserEmail(emailEditText.getText().toString());
        UserInfoService userInfoService = new UserInfoService();
        userInfoService.updateUserInfo(userInfoBean, UserInformationActivity.this, new Listener() {
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
                message.obj = "数据未更新至服务器,原因:" + msg;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 重写返回键，点击返回键回到主页
     * @param keyCode 按键编号
     * @param event 按键响应事件
     * @return 是否成功
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_DOWN){
            goMainPage();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示让用户选择从文件上传照片或者拍照上传照片的对话框
     */
    public void showDialog(){
        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 1:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (SdUtil.hasSdcard()) {

                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));
                                }

                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 切换到修改密码界面
     */
    public void goFixPasswordPage(){
        Intent intent = new Intent(UserInformationActivity.this, FixPasswordActivity.class);
        UserInformationActivity.this.startActivity(intent);
        UserInformationActivity.this.finish();
    }

    /**
     * 切换到主界面
     */
    public void goMainPage(){
        Intent intent = new Intent(UserInformationActivity.this, MyActivity.class);
        UserInformationActivity.this.startActivity(intent);
        UserInformationActivity.this.finish();
    }

    /**
     * 切换到修改图形密码界面
     */
    public void goFixGraphPasswordActivity(){
        Intent intent = new Intent(UserInformationActivity.this, SetGraphPasswordActivity.class);
        UserInformationActivity.this.startActivity(intent);
        UserInformationActivity.this.finish();
    }

    /**
     * 切换到修改图形密码界面
     */
    public void goLoginActivity(){
        Intent intent = new Intent(UserInformationActivity.this, LoginActivity.class);
        UserInformationActivity.this.startActivity(intent);
        UserInformationActivity.this.finish();
    }

    /**
     * Activity之间通信
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 传输数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (SdUtil.hasSdcard()) {
                        startPhotoZoom(Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(),
                                IMAGE_FILE_NAME)));
                    } else {
                        Toast.makeText(UserInformationActivity.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }


                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * @param uri 文件路径
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param data 接受到的数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //此photo为用户
            Drawable drawable = new BitmapDrawable(photo);
            userPhoto.setImageDrawable(drawable);

        }
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
        ToastManager.toast(UserInformationActivity.this,"信息同步成功!");
    }
    /**
     * 更新失败时所做的操作
     * 提示更新失败
     * */
    void onFailure(String msg){
        ToastManager.toast(UserInformationActivity.this,msg);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        lockState++;
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        Log.i("当前类名：",getCurrentClassName());
        Log.i("栈顶类名：",getTopActivity(this));
        Log.i("当前包名：",getTopActivityPackage(this));
        Log.i("规定包名：","com.High365.HighLight");

        if(lockState>=2){
            if(getCurrentClassName().equals(getTopActivity(this))||!getTopActivityPackage(this).equals("com.High365.HighLight"))
            {
                Log.e("类名错误","UserInformationActivity");
                Intent intent = new Intent(UserInformationActivity.this, GraphLoginActivity.class);
                UserInformationActivity.this.startActivity(intent);
            }
        }
    }

    public static String getCurrentClassName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[level].getClassName();
        return className;
    }

    String getTopActivity(Activity context) {
        ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            return runningTaskInfos.get(0).topActivity.getClassName();
        }else
            return null ;
    }

    public String getTopActivityPackage(Activity context) {
        ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            return runningTaskInfos.get(0).topActivity.getPackageName();
        }else
            return null ;
    }
}
