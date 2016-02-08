package com.High365.HighLight;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜页面，用于展示当前用户和所有用户一段时间内的最好成绩
 * (这是一个Fragment页面)
 * @author shianqi@imudges.com
 * @version 1.0
 */
public class PageThree extends Fragment {

    /**
     * 当前用户排行榜,此处使用的控件为GitHub开源图表库MPAndroidChart<br>
     * 详见<a href="https://github.com/PhilJay/MPAndroidChart">MPAndroidChart</a>
     */
    private BarChart rankMe;
    /**
     * 所有用户总排行榜,此处使用的控件为GitHub开源图表库MPAndroidChart<br>
     * 详见<a href="https://github.com/PhilJay/MPAndroidChart">MPAndroidChart</a>
     */
    private BarChart rankWorld;
    /**
     * 一个按钮组，用于让用户选择查看什么时间段的排名
     */
    private RadioGroup radioGroup;
    /**
     * 个人排行榜图像y轴数据
     */
    private ArrayList<BarEntry> yVals1;
    /**
     * 个人排行榜图像x轴数据
     */
    private ArrayList<String> xVals1;
    /**
     * 总排行榜图像y轴数据
     */
    private ArrayList<BarEntry> yVals2;
    /**
     * 总排行榜图像x轴数据
     */
    private ArrayList<String> xVals2;
    /**
     * 状态码，表示成功
     */
    final private int SUCCESS = 1;
    /**
     * 状态码，表示失败
     */
    final private int FAILURE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    /**
     * 初始化各个组件
     */
    public void init() {
        rankMe = (BarChart) getActivity().findViewById(R.id.rankMe);
        rankWorld = (BarChart) getActivity().findViewById(R.id.rankWorld);
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
        //loveLogService = new LoveLogService();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        getData(11);
                        getData(21);
                        break;
                    case R.id.radioButton2:
                        getData(12);
                        getData(22);
                        break;
                    case R.id.radioButton3:
                        getData(13);
                        getData(23);
                        break;
                }
            }
        });
        setPattern();
        getData(11);
        getData(21);
    }

    /**
     * 设置排行榜图表样式
     * 详见<a href="https://github.com/PhilJay/MPAndroidChart">MPAndroidChart</a>
     */
    public void setPattern() {
        //添加描述
        rankMe.setDescription("我的排行");
        //设置最大可见数量
        rankMe.setMaxVisibleValueCount(60);
        rankMe.setPinchZoom(false);
        rankMe.setDrawBarShadow(false);
        rankMe.setDrawGridBackground(false);

        XAxis xAxis = rankMe.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        rankMe.getAxisLeft().setDrawGridLines(false);
        // add a nice and smooth animation
        rankMe.animateY(2500);
        rankMe.getLegend().setEnabled(false);


        //添加描述
        rankWorld.setDescription("全国排行");
        //设置最大可见数量
        rankWorld.setMaxVisibleValueCount(60);
        rankWorld.setPinchZoom(false);
        rankWorld.setDrawBarShadow(false);
        rankWorld.setDrawGridBackground(false);

        XAxis xAxis2 = rankWorld.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setSpaceBetweenLabels(0);
        xAxis2.setDrawGridLines(false);

        rankWorld.getAxisLeft().setDrawGridLines(false);
        // add a nice and smooth animation
        rankWorld.animateY(2500);
        rankWorld.getLegend().setEnabled(false);
    }

    /**
     * 从LoveLogService中获取各种用户排行榜的数量,并处理数据
     * @param oper 获取数据的类型
     * oper=11:个人周排行榜<br>
     * oper=12:个人月排行榜<br>
     * oper=13:个人年排行榜<br>
     * oper=21:全部用户周排行榜<br>
     * oper=22:全部用户月排行榜<br>
     * oper=23:全部用户年排行榜<br>
     */
    public void getData(final int oper) {
        LoveLogService loveLogService = new LoveLogService();
        loveLogService.getRankModelList(oper, getActivity(), new GetRankListener() {
            @Override
            public void onSuccess(List list) {
                Message message = new Message();
                if(oper<20){
                    yVals1 = new ArrayList<BarEntry>();
                    for (int i = 0; i < list.size(); i++) {
                        LoveLogService.RankModel model = (LoveLogService.RankModel)list.get(i);
                        int num = model.getSexObjectiveScore();
                        yVals1.add(new BarEntry(num, i));
                    }
                    xVals1 = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        LoveLogService.RankModel model = (LoveLogService.RankModel)list.get(i);
                        String name = model.getUserID();
                        xVals1.add(name);
                        message.arg1 = 1;
                    }
                }
                else{
                    yVals2 = new ArrayList<BarEntry>();
                    for (int i = 0; i < list.size(); i++) {
                        LoveLogService.RankModel model = (LoveLogService.RankModel)list.get(i);
                        int num = model.getSexObjectiveScore();
                        yVals2.add(new BarEntry(num, i));
                    }
                    xVals2 = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        LoveLogService.RankModel model = (LoveLogService.RankModel)list.get(i);
                        String name = model.getUserID();
                        xVals2.add(name);
                        message.arg1 = 2;
                    }
                }
                message.what = SUCCESS;
                message.obj = "获取成功";
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(String msg) {
                Message message = new Message();
                message.what = FAILURE;
                message.obj = "获取失败，失败原因：" + msg;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 将数据绑定到图像上，并刷新UI
     * @param oper 刷新部分
     */
    private void RefreshUI(int oper) {
        BarData data;
        if (oper == 1) {
            BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            data = new BarData(xVals1, dataSets);
            rankMe.setData(data);
            rankMe.invalidate();
            rankMe.animateY(3000);
        } else {
            BarDataSet set1 = new BarDataSet(yVals2, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            data = new BarData(xVals2, dataSets);
            rankWorld.setData(data);
            rankWorld.invalidate();
            rankWorld.animateY(3000);
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
                    RefreshUI(msg.arg1);
                    break;
                case FAILURE:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
