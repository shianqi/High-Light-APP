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

    private View view;
    private ImageView light1;
    private Button cbutton;
    private boolean state;

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
        state=false;
        changeBrightness(0);

        light1=(ImageView)view.findViewById(R.id.light1);
        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state){
                    state=false;
                    changeBrightness(0);
                }else{
                    state=true;
                    changeBrightness(255);
                    Toast.makeText(view.getContext(),"录音已开始",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 调整灯泡的亮度
     * @param brightness 亮度(取值范围0-255，0是最暗，255是最亮)
     */
    public void changeBrightness(int brightness){
        light1=(ImageView)view.findViewById(R.id.light1);
        light1.getBackground().setAlpha(brightness);
    }

}