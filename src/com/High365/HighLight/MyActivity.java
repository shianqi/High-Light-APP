package com.High365.HighLight;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Button button1;
    private Button button2;
    private Button button3;

    private PageOne pageOne;
    private PageTwo pageTwo;
    private PageThree pageThree;
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

    private void addOnClickListener(){
        button1.setOnClickListener(new View.OnClickListener() {
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

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        pageOne=new PageOne();
        transaction.replace(R.id.fragment_main, pageOne);
        transaction.commit();
    }
}
