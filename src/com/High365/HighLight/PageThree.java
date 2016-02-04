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
 *         此处为排行榜页面
 */
public class PageThree extends Fragment {

    private BarChart rankMe;
    private BarChart rankWorld;
    private RadioGroup radioGroup;
    private LoveLogService loveLogService;
    private ArrayList<BarEntry> yVals1;
    private ArrayList<String> xVals;

    final private int SUCCESS = 1;
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
        loveLogService = new LoveLogService();

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
     * 从LoveLogService中获取各种用户排行榜的数量
     *
     * @param oper 获取值的类型
     */
    public void getData(int oper) {
        loveLogService.getRankModelList(oper, getActivity(), new GetRankListener() {
            @Override
            public void onSuccess(List list) {
                Message message = new Message();

                yVals1 = new ArrayList<BarEntry>();
                for (int i = 0; i < list.size(); i++) {
                    LoveLogService.RankModel model = (LoveLogService.RankModel)list.get(i);
                    int num = model.getSexObjectiveScore();
                    yVals1.add(new BarEntry(num, i));
                }

                xVals = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    LoveLogService.RankModel model = (LoveLogService.RankModel)list.get(i);
                    String name = model.getUserID();
                    xVals.add(name);
                }

                if (oper < 20) {
                    message.arg1 = 1;
                } else {
                    message.arg1 = 2;
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
     * 刷新UI
     * @param oper 刷新部分
     */
    private void RefreshUI(int oper) {
        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        if (oper == 1) {
            rankMe.setData(data);
            rankMe.invalidate();
            rankMe.animateY(3000);

        } else {
            rankWorld.setData(data);
            rankWorld.invalidate();
            rankWorld.animateY(3000);
        }
    }

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
