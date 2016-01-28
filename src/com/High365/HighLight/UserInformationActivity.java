package com.High365.HighLight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.High365.util.SdUtil;

import java.io.File;


public class UserInformationActivity extends Activity {
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    private ImageView userPhoto;
    private Button fixUserInformationButton;
    private Button fixPasswordButton;
    private Button fixGraphPasswordButton;
    private Button logoutButton;
    private TextView usernameTextView;
    private EditText nicknameEditText;
    private TextView sexTextView;
    private EditText birthdayEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private UserInfoBean userInfoBean;

    private String[] items = new String[] { "选择本地图片", "拍照" };


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
     * 绑定各个元素的id
     */
    private void init(){
        userInfoBean=new UserInfoBean();
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

        /**
         * 点击图片选择相册或者拍照图片
         */
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }else{
                    userInfoBean.setFixAble(true);
                    nicknameEditText.setEnabled(true);
                    birthdayEditText.setEnabled(true);
                    emailEditText.setEnabled(true);
                    phoneEditText.setEnabled(true);
                    birthdayEditText.setEnabled(true);
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

            }
        });

        /**
         * 设置用户的性别修改
         * 当此时用户的信息为可修改状态时，单击更改用户性别
         */
        sexTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoBean.isFixAble()){
                    if(sexTextView.getText()=="男"){
                        sexTextView.setText("女");
                        userInfoBean.setUserGender(0);
                    }else{
                        sexTextView.setText("男");
                        userInfoBean.setUserGender(1);
                    }
                }
            }
        });
    }

    /**
     * 先将用户输入的内容提取出来，并初步判断是否合法，
     * 之后为用户修改信息
     */
    public void saveFixedUserInformatin(){

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
     * 切换到登陆
     */
    public void goFixGraphPasswordActivity(){
        Intent intent = new Intent(UserInformationActivity.this, SetGraphPasswordActivity.class);
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
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
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
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param data data
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
}
