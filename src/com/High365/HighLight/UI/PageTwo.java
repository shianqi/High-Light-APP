package com.High365.HighLight.UI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.High365.HighLight.*;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Service.LoveLogService;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.High365.HighLight.Util.ToastManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * 日志页面，记录用户性生活情况
 * @author shianqi@imudges.com
 * @version 1.0
 */
public class PageTwo extends Fragment{

    /**
     * 获取到的用户日志
     */
    private List<LoveLogBean> listdata;
    /**
     * sharedPreferences管理封装类实例
     * @see SharedPreferencesManager
     */
    private SharedPreferencesManager sharedPreferencesManager;
    /**
     * 用户ID
     */
    private String UserID;
    /**
     * 数据库管理封装实例
     * @see SqlLiteManager
     */
    private SqlLiteManager sqlLiteManager;
    /**
     * 用于显示列表的ListView
     */
    private ListView listView;
    /**
     * 用于将数据绑定到ListView的适配器
     */
    private SimpleAdapter listAdatper;
    /**
     * ListView数据
     */
    private ArrayList<HashMap<String,Object>> listItem;
    private LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    /**
     * 初始化过程，负责类的实例化和组件的绑定
     */
    public void init(){
        listView = (ListView)getActivity().findViewById(R.id.ListView);
        listItem = new ArrayList<HashMap<String, Object>>();
        listAdatper = new SimpleAdapter(getActivity(),listItem,
                R.layout.list_items,
                new String[]{
                        "list_item_date",
                        "list_item_beginning_time",
                        "list_item_time",
                        "list_item_score"
                },
                new int[]{
                        R.id.list_item_date,
                        R.id.list_item_beginning_time,
                        R.id.list_item_time,
                        R.id.list_item_score
                });
        sharedPreferencesManager=new SharedPreferencesManager(getActivity());
        sqlLiteManager = new SqlLiteManager(getActivity());
        listdata = getListItem();
        paintGraph();
    }

    /**
     * 绘制图像
     */
    public void paintGraph(){
        Log.i("日志数量：",""+listdata.size());

        for (int i = 0; i < listdata.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("list_item_date",listdata.get(i).getSexDateToString());
            map.put("list_item_beginning_time"," "+listdata.get(i).getSexStartTimeToString());
            map.put("list_item_time"," "+listdata.get(i).getSexTimeToString());
            map.put("list_item_score",listdata.get(i).getSexObjectiveScore()+"");
            listItem.add(map);
        }

        listView.setAdapter(listAdatper);
        /**
         * 设置点击事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastManager.toast(getActivity(),"点击第:"+position+"个");

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View evaluateDialogView = inflater.inflate(R.layout.line_chart_dialog,null);


                mChart = (LineChart)evaluateDialogView.findViewById(R.id.list_item_chart);
                mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry entry, int i, Highlight highlight) {

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                // no description text
                mChart.setDescription("");
                mChart.setNoDataTextDescription("You need to provide data for the chart.");

                // enable touch gestures
                mChart.setTouchEnabled(true);

                mChart.setDragDecelerationFrictionCoef(0.9f);

                // enable scaling and dragging
                mChart.setDragEnabled(true);
                mChart.setScaleEnabled(true);
                mChart.setDrawGridBackground(false);
                mChart.setHighlightPerDragEnabled(true);

                // if disabled, scaling can be done on x- and y-axis separately
                mChart.setPinchZoom(true);

                // set an alternative background color
                mChart.setBackgroundColor(Color.LTGRAY);

                // add data
                setData(20, 30);

                mChart.animateX(2500);

                // get the legend (only possible after setting data)
                Legend l = mChart.getLegend();

                // modify the legend ...
                // l.setPosition(LegendPosition.LEFT_OF_CHART);
                l.setForm(Legend.LegendForm.LINE);

                l.setTextSize(11f);
                l.setTextColor(Color.WHITE);
                l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setYOffset(11f);

                XAxis xAxis = mChart.getXAxis();

                xAxis.setTextSize(12f);
                xAxis.setTextColor(Color.WHITE);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setSpaceBetweenLabels(1);

                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setTextColor(ColorTemplate.getHoloBlue());
                leftAxis.setAxisMaxValue(100f);
                leftAxis.setDrawGridLines(true);

                YAxis rightAxis = mChart.getAxisRight();
                rightAxis.setTextColor(ColorTemplate.getHoloBlue());
                rightAxis.setAxisMaxValue(100);
                rightAxis.setStartAtZero(false);
                rightAxis.setDrawGridLines(false);

                new AlertDialog.Builder(getActivity())
                        .setTitle("成绩曲线")
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
        });

        /**
         * 设置长按响应事件
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("操作")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastManager.toast(getActivity(),"请申请合适的api来实现分享");
                                //没有申请合适的api来实现分享
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    /**
     * 获取当前登陆用户的声音数据信息
     * @return 一个声音数据的List
     */
    public List<LoveLogBean> getListItem(){
        return new LoveLogService().getLoveLogList(getActivity());
    }

    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = range / 2f;
            float val = (float) (Math.random() * mult) + 50;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals1.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        //set1.setFillFormatter(new MyFillFormatter(0f));
//        set1.setDrawHorizontalHighlightIndicator(false);
//        set1.setVisible(false);
//        set1.setCircleHoleColor(Color.WHITE);


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
    }
}
