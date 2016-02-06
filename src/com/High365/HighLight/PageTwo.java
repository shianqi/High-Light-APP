package com.High365.HighLight;

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
import java.util.List;


/**
 * 日志页面，记录用户性生活情况
 * @author shianqi@imudges.com
 */
public class PageTwo extends Fragment{

    /**
     * 获取到的用户日志
     */
    private List<LoveLogBean> listItem;
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
     * 测试用函数
     */
    public void test(){
        Log.i("音频数量",listItem.size()+"");
        for(int i=0;i<listItem.size();i++){
            Log.i("音频数据有：",""+listItem.get(i).getSexFrameState());
        }
    }

    /**
     * 初始化过程，负责类的实例化和组件的绑定
     */
    public void init(){
        sharedPreferencesManager=new SharedPreferencesManager(getActivity());
        sqlLiteManager = new SqlLiteManager(getActivity());
        listItem = getListItem();
        paintGraph();
    }

    /**
     * 绘制图像
     */
    public void paintGraph(){
        ListView listView = (ListView)getActivity().findViewById(R.id.ListView);

        ArrayList<BarData> list = new ArrayList<BarData>();
        Log.i("日志数量：",""+listItem.size());

        for (int i = 0; i < listItem.size(); i++) {
            list.add(generateData(i));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
        listView.setAdapter(cda);
        /**
         * 设置点击事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastManager.toast(getActivity(),"点击第:"+position+"个");
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
                return false;
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

    private class ChartDataAdapter extends ArrayAdapter<BarData> {
        public ChartDataAdapter(Context context, List<BarData> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BarData data = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_items, null);
                holder.chart = (BarChart) convertView.findViewById(R.id.chart);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // apply styling
            data.setValueTextColor(Color.BLACK);
            holder.chart.setDescription("");
            holder.chart.setDrawGridBackground(false);

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            YAxis leftAxis = holder.chart.getAxisLeft();
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = holder.chart.getAxisRight();
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);

            // set data
            holder.chart.setData(data);

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            holder.chart.animateY(700, Easing.EasingOption.EaseInCubic);

            return convertView;
        }

        private class ViewHolder {

            BarChart chart;
        }
    }

    /**
     * 将数据添加到表格中
     * @return BarDate数据
     */
    private BarData generateData(int cnt) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        Log.i("SexFrameStateSize：",listItem.get(cnt).getSexFrameStateSize()+"");
        for (int i = 0; i < listItem.get(cnt).getSexFrameStateSize(); i++) {
            Log.i("数量",i+"");
            entries.add(new BarEntry(listItem.get(cnt).getSexFrameStateByNumber(i), i));
        }

        BarDataSet d = new BarDataSet(entries, "Data: " + listItem.get(cnt).getSexEndTime());
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setBarShadowColor(Color.rgb(203, 203, 203));

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(d);

        BarData cd = new BarData(getMonths(cnt), sets);
        return cd;
    }

    private ArrayList<String> getMonths(int cnt) {

        ArrayList<String> m = new ArrayList<String>();
        for(int i=0;i<listItem.get(cnt).getSexFrameStateSize();i++){
            m.add(listItem.get(cnt).getSexFrameStateByNumber(i)+"");
        }
        return m;
    }
}
