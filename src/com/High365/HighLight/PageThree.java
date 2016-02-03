package com.High365.HighLight;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import io.realm.Case;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 史安琪
 * 此处为排行榜页面
 */
public class PageThree extends Fragment{

    private BarChart rankMe;
    private BarChart rankWorld;
    private RadioGroup radioGroup;
    private LoveLogService loveLogService;

    final private int SUCCESS=1;
    final private int FAILURE=0;

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
    public void init(){
        rankMe = (BarChart)getActivity().findViewById(R.id.rankMe);
        rankWorld = (BarChart)getActivity().findViewById(R.id.rankWorld);
        radioGroup =(RadioGroup)getActivity().findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        break;
                    case R.id.radioButton2:
                        break;
                    case R.id.radioButton3:
                        break;
                }
            }
        });

        setPattern();
        setData();
    }

    /**
     * 设置排行榜图表样式
     */
    public void setPattern(){
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


        /**
         * 榜单top10
         */
        //添加描述
        rankWorld.setDescription("我的排行");
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
     * 从LoveLogService中获取各种用户排行榜的数量
     */
    public void setData(){
        //未完成
        List<LoveLogService.RankModel> rankList =new ArrayList<LoveLogService.RankModel>();
        loveLogService.getRankModelList(11, getActivity(), new GetRankListener() {
            @Override
            public void onSuccess(List list) {
                Message message = new Message();
                message.what=SUCCESS;
                message.obj="获取成功";
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(String msg) {
                Message message = new Message();
                message.what=FAILURE;
                message.obj="获取失败，失败原因："+msg;
                handler.sendMessage(message);
            }
        });

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 10; i++) {
            float mult = (10);
            float val1 = (float) (Math.random() * mult) + mult / 3;
            yVals1.add(new BarEntry((int) val1, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            xVals.add((int) yVals1.get(i).getVal() + "");
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        rankMe.setData(data);
        rankMe.invalidate();

        rankWorld.setData(data);
        rankWorld.invalidate();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    break;
                case FAILURE:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
