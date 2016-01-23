package com.High365.HighLight;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author 史安琪
 * 这是第二个页面
 */
public class PageOne extends Fragment{

    /**
     * light1: 灯光图片对象
     * state: 灯光当前亮度
     */
    private View view;
    private ImageView light1;
    //测试按钮
    private Button cbutton;
    private int state;
    private static Toast myToast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frist_fragment, container, false);
        init();

        /**
         * 此函数为测试使用
         */
        cbutton=(Button)view.findViewById(R.id.change);
        cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBrightness(200);
            }
        });


        return view;
    }

    /**
     * 初始化函数，用于此fragment的初始化
     */
    private void init(){
        state=0;
        changeBrightness(0);

        light1=(ImageView)view.findViewById(R.id.light1);
        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state!=0){
                    state=0;
                    changeBrightness(0);
                    myToast(view,"录音暂停中");
                }else{
                    state=255;
                    changeBrightness(255);
                    myToast(view,"录音已开始");
                }
            }
        });

        light1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(state!=0){
                    state=0;
                    changeBrightness(0);
                    myToast(view,"录音已结束");
                    return true;
                }else {
                    return false;
                }

            }
        });

    }

    /**
     * 调整灯泡的亮度
     * @param brightness 亮度(取值范围0-255，0是最暗，255是最亮)
     */
    public void changeBrightness(int brightness){
        state=brightness;
        light1=(ImageView)view.findViewById(R.id.light1);
        light1.getBackground().setAlpha(brightness);
    }

    /**
     * 此处是一个封装的Toast方法，可以取消掉上一次未完成的，直接进行下一次Toast
     * @param view view
     * @param text 需要toast的内容
     */
    public static void myToast(View view,String text){
        if (myToast != null) {
            myToast.cancel();
            myToast=Toast.makeText(view.getContext(),text,Toast.LENGTH_SHORT);
        }else{
            myToast=Toast.makeText(view.getContext(),text,Toast.LENGTH_SHORT);
        }
        myToast.show();
    }

}