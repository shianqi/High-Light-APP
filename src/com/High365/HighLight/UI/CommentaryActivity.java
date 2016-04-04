package com.High365.HighLight.UI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.High365.HighLight.Bean.CommentModel;
import com.High365.HighLight.Interface.GetListListener;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.R;
import com.High365.HighLight.Service.CommentService;
import com.High365.HighLight.Service.UserInfoService;
import com.High365.HighLight.Util.ImageEncodeUtil;
import com.High365.HighLight.Util.ToastManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 详情及评论界面
 * Created by killer on 2016/3/25.
 */
public class CommentaryActivity extends Activity {
    private LineChart mChart;
    private NoScrollListview listView;
    private ArrayList<HashMap<String,Object>> listItem;
    private SimpleAdapter listAdapter;
    private CommentService commentService;

    private Button button;
    private TextView textView;
    private EditText editText;

    private Integer circleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentary_activity);
        commentService = new CommentService();
        textView = (TextView)this.findViewById(R.id.tip_text);
        mChart = (LineChart)this.findViewById(R.id.commentary_chart);
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
        mChart.setBackgroundColor(Color.WHITE);

        // add data
        setData(1);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        l.setTextSize(11f);
        l.setTextColor(Color.GRAY);//
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis xAxis = mChart.getXAxis();

        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.GRAY);//
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


        listView = (NoScrollListview)this.findViewById(R.id.commentary_listView);
        listItem = new ArrayList<HashMap<String, Object>>();



        listAdapter = new SimpleAdapter(this,listItem,
                R.layout.list_item_commentary,
                new String[]{
                        "discovery_icon",
                        "discovery_username",
                        "list_item_main",
                },
                new int[]{
                        R.id.user_img,
                        R.id.discovery_username,
                        R.id.list_item_main,
                });


//        for(int i = 0; i<10;i++){
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("discovery_icon",R.drawable.ic_launcher);
//            map.put("discovery_username","信息1");
//            map.put("list_item_main","123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
//            listItem.add(map);
//        }

        //获得上一个状态帧的朋友圈id数据
        Bundle extras  = getIntent().getExtras();
        circleId = extras.getInt("circleId");
        if (circleId != null){
            commentService.getCommentList(circleId, this, new GetListListener() {
                @Override
                public void onSuccess(List list) {
                    if(list.size()==0){
                        textView.setText("暂时没有评论");
                    }else{
                        textView.setVisibility(View.INVISIBLE);
                    }
                    for(int i = 0; i<list.size();i++){
                        CommentModel commentModel = (CommentModel)list.get(i);
                        HashMap<String, Object> map = new HashMap<String, Object>();

                        map.put("discovery_username",commentModel.getUserId());
                        map.put("list_item_main",commentModel.getCommentText());
                        //加载图片
                        map.put("discovery_icon", ImageEncodeUtil.base64ToBitmap(commentModel.getUserPhoto()));
                        listAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Object data, String s) {
                                if(view instanceof ImageView && data instanceof Bitmap){
                                    ImageView i = (ImageView)view;
                                    i.setImageBitmap((Bitmap) data);
                                    return true;
                                }
                                return false;
                            }
                        });
                        listItem.add(map);
                    }
                    listView.setAdapter(listAdapter);
                }

                @Override
                public void onFailure(String msg) {
                    ToastManager.toast(CommentaryActivity.this,msg);
                }
            });
        }





        button = (Button)findViewById(R.id.sendButton);
        editText = (EditText)findViewById(R.id.commentary_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentaryText = editText.getText().toString();

                UserInfoService userInfoService = new UserInfoService();
                //提交评论
                commentService.addComment(circleId, commentaryText, CommentaryActivity.this, new Listener() {
                    @Override
                    public void onSuccess() {
                        HashMap<String, Object> map = new HashMap<String, Object>();

                        map.put("discovery_username",userInfoService.getCurrentUserId(CommentaryActivity.this));
                        map.put("list_item_main",commentaryText);
                        //加载图片
                        map.put("discovery_icon",userInfoService.getUserPhoto(CommentaryActivity.this));
                        listAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Object data, String s) {
                                if(view instanceof ImageView && data instanceof Bitmap){
                                    ImageView i = (ImageView)view;
                                    i.setImageBitmap((Bitmap) data);
                                    return true;
                                }
                                return false;
                            }
                        });
                        listItem.add(map);
                        listAdapter.notifyDataSetChanged();
                        ToastManager.toast(getApplication(),"评论成功");
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastManager.toast(getApplication(),"评论失败");
                    }
                });
                editText.setText("");
            }
        });
    }

    private void setData(int position) {
        ArrayList<String> xVals = new ArrayList<String>();

        //获得上一个activity的状态帧数据
        Bundle extras  = getIntent().getExtras();
        String sexFrameState = extras.getString("sexFrameState");

        for (int i = 0; i < sexFrameState.length()/2; i++) {
            xVals.add(i + "");
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();


        if (sexFrameState!=null){
            for (int i = 0; i < sexFrameState.length()/2; i++) {
                Log.i("+++++ " , sexFrameState.substring(i*2,i*2+2));
                yVals1.add(new Entry(Integer.parseInt(sexFrameState.substring(i*2,i*2+2)), i));
            }
        }



        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.rgb(0, 167, 227));
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
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
    }
}
