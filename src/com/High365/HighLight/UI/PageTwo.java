package com.High365.HighLight.UI;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
    /**
     * 分享按钮
     */

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
        UserID = sharedPreferencesManager.readString("UserID");
        Log.i("UserID:", ""+UserID);
        return sqlLiteManager.getLoveLogsByUserID(UserID);
    }

//    /**
//     * 将数据添加到表格中
//     * @return BarDate数据
//     */
//    private BarData generateData(int cnt) {
//        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
//        Log.i("SexFrameStateSize：",listItem.get(cnt).getSexFrameStateSize()+"");
//        for (int i = 0; i < listItem.get(cnt).getSexFrameStateSize(); i++) {
//            Log.i("数量",i+"");
//            entries.add(new BarEntry(listItem.get(cnt).getSexFrameStateByNumber(i), i));
//        }
//
//        BarDataSet d = new BarDataSet(entries, "Data: " + listItem.get(cnt).getSexEndTime());
//        d.setBarSpacePercent(20f);
//        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        d.setBarShadowColor(Color.rgb(203, 203, 203));
//
//        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
//        sets.add(d);
//
//        BarData cd = new BarData(getMonths(cnt), sets);
//        return cd;
//    }
//
//    private ArrayList<String> getMonths(int cnt) {
//
//        ArrayList<String> m = new ArrayList<String>();
//        for(int i=0;i<listItem.get(cnt).getSexFrameStateSize();i++){
//            m.add(listItem.get(cnt).getSexFrameStateByNumber(i)+"");
//        }
//        return m;
//    }
}
