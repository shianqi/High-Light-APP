package com.High365.HighLight.UI;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.High365.HighLight.Interface.GetListListener;
import com.High365.HighLight.Interface.OnRefreshListener;
import com.High365.HighLight.R;
import com.High365.HighLight.Service.FriendCircleService;
import com.High365.HighLight.Util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private MyAdapter listAdatper;

    private FriendCircleService friendCircleService;

    public class MyAdapter extends SimpleAdapter{

        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = super.getView(position, convertView, parent);

            ImageView btn=(ImageView)v.findViewById(R.id.glorification);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    HashMap<String, Object> map = listItem.get(position);
                    map.put("discovery_icon",R.drawable.glorification_true);
                    listItem.set(position,map);
                    ToastManager.toast(getActivity(),"点击"+position+"个");
                    listAdatper.notifyDataSetChanged();
                }
            });
            return v;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fourth_fragment, container, false);
        Log.e("加载界面4","");

        friendCircleService = new FriendCircleService();

        rListView = (RefreshListView)view.findViewById(R.id.refreshlistview);
        listItem = new ArrayList<HashMap<String, Object>>();



        listAdatper = new MyAdapter(getActivity(),listItem,
                R.layout.list_item_discovery,
                new String[]{
                        "discovery_icon",
                        "discovery_username",
                        "list_item_beginning_time",
                        "list_item_time",
                        "list_item_score"
                },
                new int[]{
                        R.id.glorification,
                        R.id.list_item_date,
                        R.id.list_item_beginning_time,
                        R.id.list_item_time,
                        R.id.list_item_score
                });


        for(int i = 0; i<10;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("discovery_icon",R.drawable.glorification_false);
            map.put("discovery_username","信息1");
            map.put("list_item_main","信息2");
            map.put("list_item_time"," "+"20分钟前");
            map.put("glorification_state","信息4");
            listItem.add(map);
        }

//        //获取下拉刷新的内容
//
//        friendCircleService.getDownPullList(getActivity(), new GetListListener() {
//            @Override
//            public void onSuccess(List list) {
//                for (int i=0;i<list.size();i++)  {
//
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//
//            }
//        });



        rListView.setAdapter(listAdatper);
        rListView.setOnRefreshListener(this);

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CommentaryActivity.class);
                getActivity().startActivity(intent);
            }
        });

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
                SystemClock.sleep(2000);

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
