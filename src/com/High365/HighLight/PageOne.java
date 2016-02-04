package com.High365.HighLight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.sql.Timestamp;

/**
 * 第一个界面的逻辑代码
 * @author 史安琪
 */
public class PageOne extends Fragment{

    private View view;
    private ImageView light1;


    private String sexFrameState = "";

    /**
     * 灯光亮度
     */
    private int state;
    /**
     * Toast实例，用于对本页出现的所有Toast进行处理
     */
    private static Toast myToast;

    private AudioRecorder audioRecorder;
    private LoveLogBean loveLogBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frist_fragment, container, false);
        init();
        audioRecorder = new AudioRecorder(0);
        loveLogBean = new LoveLogBean();
        return view;
    }

    /**
     * 初始化函数，用于此fragment的初始化
     */
    private void init(){
        state=0;
        changeBrightness(0);

        light1=(ImageView)view.findViewById(R.id.light1);

        //点击
        light1.setOnClickListener(new View.OnClickListener() {
            /**
             *设置发送录音的暂停与开始请求，并更改灯泡亮度
             * @param v view
             */
            @Override
            public void onClick(View v) {
                if (state!=0){
                    state=0;
                    changeBrightness(0);
                    myToast(view,"录音暂停中");
                    audioRecorder.isGetVoiceRun = false;
                }else{
                    state=255;
                    changeBrightness(255);
                    myToast(view,"录音已开始");
                    loveLogBean.setSexStartTime(new Timestamp(System.currentTimeMillis()));
                    audioRecorder.isGetVoiceRun = false;
                    audioRecorder.getNoiseLevel(new AudioRecorderListener() {
                        @Override
                        public void onSuccess(double level) {
                            Message message = new Message();
                            message.obj = level;
                            Log.d("out",""+level);
                            handler.sendMessage(message);
                            int voice = (int)level;
                            Log.d("voice",voice+"");
                            if (voice<10){
                                sexFrameState += "0" + voice;
                            }else{
                                if (voice>=100){
                                    sexFrameState += "99";
                                }else {
                                    sexFrameState += voice;
                                }
                            }
                            Log.d("sexFrame",sexFrameState);
                        }
                    });
                }
            }
        });

        //长按
        light1.setOnLongClickListener(new View.OnLongClickListener() {
            /**
             * 设置长按监听函数，用于处理长按关闭录音。
             * 判断当前状态，如果出于录音状态则结束录音，否则不进行处理
             * @param v view
             * @return 当当前出于录音状态时，返回成功，否则返回失败
             */
            @Override
            public boolean onLongClick(View v) {
                if(state!=0){
                    state=0;
                    changeBrightness(0);
                    audioRecorder.stopRecord();
                    myToast(view,"录音已结束");
                    loveLogBean.setSexEndTime(new Timestamp(System.currentTimeMillis()));
                    loveLogBean.setUserID(new SharedPreferencesManager(getActivity()).readString("UserID"));
                    loveLogBean.setRecordFileName(audioRecorder.recordFileName);
                    loveLogBean.setUpdateFlag(0);
                    loveLogBean.setSexFrameState(sexFrameState);
                    loveLogBean.setSexTime(new Timestamp(loveLogBean.getSexEndTime().getTime()-loveLogBean.getSexStartTime().getTime()));
                    loveLogBean.setSexHighTime(new Timestamp(0));
                    loveLogBean.setSexSubjectiveScore(0);
                    loveLogBean.setSexObjectiveScore(0);
                    SqlLiteManager sqlLiteManager = new SqlLiteManager(getActivity());
                    sqlLiteManager.updateOrInsertLoveLog(loveLogBean);
                    showDialog();
                    return true;
                }else {
                    return false;
                }

            }
        });

    }

    /**
     * 线程间的通信,接收消息,刷新主线程UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            double volLevel = (Double)msg.obj;
            int lightLevel = ((int)(volLevel/100*254))>254?254:((int)(volLevel/100*254));
            //System.out.println(lightLevel);
            changeBrightness(lightLevel);
            super.handleMessage(msg);
        }
    };

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

    /**
     * 是否保存Dialog显示
     */
    public void showDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("是否记录本次录音")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //上传数据
                        evaluateDialog();
                    }
                })
                .show();
    }

    /**
     * 用户打分Dialog显示
     */
    public void evaluateDialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View evaluateDialogView = inflater.inflate(R.layout.evaluate_dialog,null);
        final SeekBar seekBar = (SeekBar)evaluateDialogView.findViewById(R.id.evaluate_dialog_seekBar);

        new AlertDialog.Builder(getActivity())
                .setTitle("自我评价")
                .setView(evaluateDialogView)
                .setNegativeButton("跳过",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}