package com.High365.HighLight.UI;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.High365.HighLight.Interface.OnRefreshListener;
import com.High365.HighLight.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发现界面搭建
 * Created by shianqi@imudges.com on 2016/3/23.
 */
public class PageFour extends Fragment implements OnRefreshListener {
    /**
     * 发现界面view
     */
    private View view;

    private ArrayList<HashMap<String,Object>> listItem;
    private RefreshListView rListView;
    /**
     * 用于将数据绑定到ListView的适配器
     */
    private SimpleAdapter listAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        Log.e("加载界面4","");

        rListView = (RefreshListView)view.findViewById(R.id.refreshlistview);
        listItem = new ArrayList<HashMap<String, Object>>();

        listAdatper = new SimpleAdapter(getActivity(),listItem,
                R.layout.list_item_discovery,
                new String[]{
                        "discovery_username",
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


        for(int i = 0; i<10;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("list_item_date","信息1");
            map.put("list_item_beginning_time","信息2");
            map.put("list_item_time"," "+"信息3");
            map.put("list_item_score","信息4");
            listItem.add(map);
        }
        rListView.setAdapter(listAdatper);
        rListView.setOnRefreshListener(this);
        return view;
    }





    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                for (int i = 0; i < 2; i++) {
                    //textList.add(0, "这是下拉刷新出来的数据" + i);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listAdatper.notifyDataSetChanged();
                rListView.hideHeaderView();
            }
        }.execute(new Void[] {});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(5000);

                //textList.add("这是加载更多出来的数据1");
                //textList.add("这是加载更多出来的数据2");
                //textList.add("这是加载更多出来的数据3");
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listAdatper.notifyDataSetChanged();

                // 控制脚布局隐藏
                rListView.hideFooterView();
            }
        }.execute(new Void[] {});
    }
}
