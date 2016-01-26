package com.High365.HighLight;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/**
 * 主页面
 * <p>由三个Fragment构成，分别是录音界面，日志界面，排行榜界面</p>
 * 录音界面：
 * @see PageOne
 * 日志界面：
 * @see PageTwo
 * 排行榜界面：
 * @see PageThree
 * <br>
 */
public class MyActivity extends Activity {

    /**
     * 底部按钮组第一个按钮，录音界面
     */
    private Button button1;
    /**
     * 底部按钮组第二个按钮，日志界面
     */
    private Button button2;
    /**
     * 底部按钮组第三个按钮，排行榜界面
     */
    private Button button3;

    private PageOne pageOne;
    private PageTwo pageTwo;
    private PageThree pageThree;

    /**
     * 退出按键时长
     */
    private long EXITTIME = 0;

    private final int MYINFORMATION = 1000;
    private final int EXIT = 1003;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);

        pageOne=new PageOne();
        pageTwo=new PageTwo();
        pageThree=new PageThree();

        setDefaultFragment();
        addOnClickListener();

    }

    /**
     * 按钮功能的绑定
     */
    private void addOnClickListener(){

        button1.setOnClickListener(new View.OnClickListener() {
            /**
             * 设置第一个按钮的绑定事件，当这个按钮被点击时：如果这个Fragment不存在，则创建一个。然后切换到这个Fragment
             * @param v view
             */
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                if (pageOne==null){
                    pageOne=new PageOne();
                }
                transaction.replace(R.id.fragment_main, pageOne);
                transaction.commit();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            /**
             * 设置第二个按钮的绑定事件，当这个按钮被点击时：如果这个Fragment不存在，则创建一个。然后切换到这个Fragment
             * @param v view
             */
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();

                if (pageTwo==null){
                    pageTwo=new PageTwo();
                }
                transaction.replace(R.id.fragment_main, pageTwo);
                transaction.commit();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            /**
             * 设置第三个按钮的绑定事件，当这个按钮被点击时：如果这个Fragment不存在，则创建一个。然后切换到这个Fragment
             * @param v view
             */
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                if (pageThree==null){
                    pageThree=new PageThree();
                }
                transaction.replace(R.id.fragment_main, pageThree);
                transaction.commit();
            }
        });
    }

    /**
     * 设置默认Fragment，以录音界面为默认界面
     */
    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        pageOne=new PageOne();
        transaction.replace(R.id.fragment_main, pageOne);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-EXITTIME) > 2000){
                ToastManager.toast(getApplicationContext(),"再按一次退出程序");
                EXITTIME = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MYINFORMATION, 0, "我的信息");
        menu.add(0, EXIT, 1, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置裁断按钮的功能
     * @param item item
     * @return 事件是否成功
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MYINFORMATION:
                toUserInformation();
                break;
            case EXIT:
                exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void toUserInformation(){
        Intent intent = new Intent(MyActivity.this, UserInformation.class);
        MyActivity.this.startActivity(intent);
        MyActivity.this.finish();
    }

    /**
     * 退出应用
     */
    public void exit(){
        System.exit(0);
    }
}
