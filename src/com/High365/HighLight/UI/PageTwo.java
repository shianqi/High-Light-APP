package com.High365.HighLight.UI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.High365.HighLight.*;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Service.FriendCircleService;
import com.High365.HighLight.Service.LoveLogService;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.High365.HighLight.Util.ToastManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.*;


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

    private EditText discoveryEditText;
    private TextView discoveryTextView;

    private List<LoveLogBean> listDataMain;
    /**
     * sharedPreferences管理封装类实例
     * @see SharedPreferencesManager
     */
    private SharedPreferencesManager sharedPreferencesManager;
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
    /**
     * 朋友圈业务逻辑类
     * */
    FriendCircleService friendCircleService;
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
        friendCircleService = new FriendCircleService();
        sqlLiteManager = new SqlLiteManager(getActivity());
        listdata = getListItem();
        paintGraph();
    }


    public void addListDate(){
        int nowListNumber = listItem.size();
        for(int i = nowListNumber; i<nowListNumber+5;i++){
            if(listItem.size()>=listdata.size()){
                //ToastManager.toast(getActivity(),"已经浏览到最后啦");
                break;
            }else {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("list_item_date",listdata.get(i).getSexDateToString());
                map.put("list_item_beginning_time"," "+listdata.get(i).getSexStartTimeToString());
                map.put("list_item_time"," "+listdata.get(i).getSexTimeToString());
                map.put("list_item_score",listdata.get(i).getSexObjectiveScore()+"");
                listItem.add(map);
            }
        }
    }
    /**
     * 绘制图像
     */
    public void paintGraph(){
        Log.i("日志数量：",""+listdata.size());

        addListDate();

        listView.setAdapter(listAdatper);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            ToastManager.toast(getActivity(),"滚动到最底部");
                            addListDate();
                            listAdatper.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        /**
         * 设置点击事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                mChart.setNoDataTextDescription("分数曲线走丢了::>_<::");

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
                setData(position);

                mChart.animateX(2500);

                // get the legend (only possible after setting data)
                Legend l = mChart.getLegend();

                // modify the legend ...
                // l.setPosition(LegendPosition.LEFT_OF_CHART);
                l.setForm(Legend.LegendForm.LINE);

                l.setTextSize(11f);
                l.setTextColor(Color.WHITE);
                l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

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
                        .setNegativeButton("分享",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //ToastManager.toast(getActivity(),  "分享未开放");
                                final View discoveryDialogView = inflater.inflate(R.layout.discovery_dialog,null);
                                discoveryEditText = (EditText)discoveryDialogView.findViewById(R.id.discovery_text);
                                discoveryTextView = (TextView)discoveryDialogView.findViewById(R.id.discovery_text_size);
                                discoveryEditText.setText("今天嗨了"+listdata.get(position).getSexObjectiveScore()+"分！");
                                discoveryTextView.setText("还可以输入："+(140-discoveryEditText.length()));
                                discoveryEditText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        discoveryTextView.setText("还可以输入："+(140-discoveryEditText.length()));
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("分享")
                                        .setView(discoveryDialogView)
                                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("发布", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                friendCircleService.shareToFriendCircle(getActivity(), friendCircleService.fromLoveLog(""+discoveryEditText.getText(),listdata.get(position)), new Listener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        ToastManager.toast(getActivity(),"分享成功！");
                                                    }

                                                    @Override
                                                    public void onFailure(String msg) {
                                                        ToastManager.toast(getActivity(),"分享失败："+msg);
                                                    }
                                                });
                                            }
                                        })
                                        .show();
                                //执行分享逻辑

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
    }

    /**
     * 获取当前登陆用户的声音数据信息
     * @return 一个声音数据的List
     */
    public List<LoveLogBean> getListItem(){
        return new LoveLogService().getLoveLogList(getActivity());
    }

    private void setData(int position) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < listdata.get(position).getSexFrameStateSize(); i++) {
            xVals.add(i + "");
        }

        Log.i("listdata:("+position+")",listdata.get(position).getSexFrameStateSize()+"");

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < listdata.get(position).getSexFrameStateSize(); i++) {
            yVals1.add(new Entry(listdata.get(position).getSexFrameStateByNumber(i), i));
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
