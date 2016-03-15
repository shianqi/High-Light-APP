package com.High365.HighLight.UI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.High365.HighLight.*;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Interface.AudioRecorderListener;
import com.High365.HighLight.Interface.GetPowerListener;
import com.High365.HighLight.Interface.LightLevelListener;
import com.High365.HighLight.Service.LoveLogService;
import com.High365.HighLight.Util.AudioRecorder;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.High365.HighLight.Util.ToastManager;

import java.sql.Timestamp;

/**
 * 录音界面
 * @author shianqi@imudges.com
 * @version 1.0
 */
public class PageOne extends Fragment{

    /**
     * 录音界面的View
     */
    private View view;
    /**
     * 灯泡点亮时的图片，通过改变其透明度可以实现灯泡的明暗变化
     */
    private ImageView light1;

    /**
     * 性的状态帧
     */
    private String sexFrameState = "";

    /**
     * loveLog的服务类
     */

    private LoveLogService loveLogService;

    /**
     * 灯光亮度
     */
    private int state;
    PowerManager powerManager;

    /**
     * 滑动条，负责录音结束后用户进行评价打分
     */
    private AudioRecorder audioRecorder;
    private PowerManager.WakeLock wakeLock;

    private final int START = 1;
    private final int PAUSE = 2;
    private final int STOP = 0;
    private int recordingState = STOP;

    public int getRecordingState() {
        return recordingState;
    }

    public void setRecordingState(int recordingState) {
        this.recordingState = recordingState;
    }

    /**
     * 日志Bean对象
     */
    private LoveLogBean loveLogBean;
    private ImageView playButton;
    private ImageView stopButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frist_fragment, container, false);
        init();
        audioRecorder = new AudioRecorder(0);

        return view;
    }

    /**
     * 初始化函数，用于此fragment的初始化
     */
    private void init(){
        powerManager = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");

        state=0;
        changeBrightness(0);

        playButton = (ImageView)view.findViewById(R.id.play_button);
        stopButton = (ImageView)view.findViewById(R.id.stop_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getRecordingState()==STOP){
                    recordingStart();
                    playButton.setBackgroundResource(R.drawable.pause_on);
                    stopButton.setBackgroundResource(R.drawable.stop_on);
                }else if(getRecordingState()==PAUSE){
                    recordingStart();
                    playButton.setBackgroundResource(R.drawable.pause_on);
                    stopButton.setBackgroundResource(R.drawable.stop_on);
                }else{
                    recordingPause();
                    playButton.setBackgroundResource(R.drawable.play_on);
                    stopButton.setBackgroundResource(R.drawable.stop_on);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecordingState()==START){
                    recordingStop();
                    playButton.setBackgroundResource(R.drawable.play_on);
                    stopButton.setBackgroundResource(R.drawable.stop_off);
                }else if(getRecordingState()==PAUSE){
                    recordingStop();
                    playButton.setBackgroundResource(R.drawable.play_on);
                    stopButton.setBackgroundResource(R.drawable.stop_off);
                }else{

                }
            }
        });
    }

    /**
     * 录音开始
     */
    public void recordingStart(){
        setRecordingState(START);
        //设置不锁屏
        wakeLock.acquire();
        changeBrightness(255);
//        ToastManager.toast(getActivity(),"录音已开始");
        loveLogBean = new LoveLogBean();
        loveLogBean.setSexStartTime(new Timestamp(System.currentTimeMillis()));
        audioRecorder.isGetVoiceRun = false;
        audioRecorder.startRecord();
        //设置灯泡亮度
        audioRecorder.getLightLevel(new LightLevelListener() {
            @Override
            public void getValue(int v) {
                Message msg = new Message();
                msg.obj = v ;
                handler.sendMessage(msg);
            }
        });
        //获取能量数据
        audioRecorder.getPower(new GetPowerListener() {
            @Override
            public void getvalue(double v) {
                loveLogBean.addToSexFrameState(v);
            }
        });
    }

    public void recordingStop(){
        //取消不锁屏
        wakeLock.release();
        setRecordingState(STOP);

        changeBrightness(0);
        audioRecorder.stopRecord();
//        ToastManager.toast(getActivity(),"录音已结束");
        loveLogBean.setSexEndTime(new Timestamp(System.currentTimeMillis()));
        loveLogBean.setUserId(new SharedPreferencesManager(getActivity()).readString("UserID"));
        loveLogBean.setRecordFileName(audioRecorder.recordFileName);
        loveLogBean.setUpdateFlag(0);
//        loveLogBean.setSexFrameState(sexFrameState);
        loveLogBean.setSexTime(new Timestamp(loveLogBean.getSexEndTime().getTime()-loveLogBean.getSexStartTime().getTime()));
        loveLogBean.setSexHighTime(new Timestamp(0));
        loveLogBean.setSexSubjectiveScore(0);
        loveLogBean.setSexObjectiveScore((int)(Math.random()*100));

        showDialog();
    }

    public void recordingPause(){
        setRecordingState(PAUSE);
        changeBrightness(0);
//        ToastManager.toast(getActivity(),"录音暂停中");
        audioRecorder.isGetVoiceRun = false;
    }

    /**
     * 线程间的通信,接收消息,刷新主线程UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int lightLevel = (Integer) msg.obj;
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
        final TextView sexSubjectiveScore = (TextView)evaluateDialogView.findViewById(R.id.sexSubjectiveScore);

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
//                        ToastManager.toast(getActivity(),sexSubjectiveScore.getText()+"");
                        loveLogBean.setSexSubjectiveScore(Integer.parseInt(sexSubjectiveScore.getText()+""));
                        SqlLiteManager sqlLiteManager = new SqlLiteManager(getActivity());
                        sqlLiteManager.updateOrInsertLoveLog(loveLogBean);
                        loveLogService = new LoveLogService();
                        loveLogService.update(loveLogBean,getActivity());
                    }
                })
                .show();

        /**
         * 设置滚动条的监听
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sexSubjectiveScore.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}