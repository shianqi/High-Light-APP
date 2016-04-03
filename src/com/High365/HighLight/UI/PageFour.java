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
import com.High365.HighLight.Bean.FriendCircleModel;
import com.High365.HighLight.Interface.GetListListener;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Interface.OnRefreshListener;
import com.High365.HighLight.R;
import com.High365.HighLight.Service.FriendCircleService;
import com.High365.HighLight.Service.TimeService;
import com.High365.HighLight.Service.UpvoteService;
import com.High365.HighLight.Util.BitmapUtil;
import com.High365.HighLight.Util.ImageEncodeUtil;
import com.High365.HighLight.Util.ToastManager;

import java.util.*;

/**
 * 发现界面搭建
 * Created by shianqi@imudges.com on 2016/3/23.
 */
public class PageFour extends Fragment implements OnRefreshListener {
    /**
     * 发现界面view
     */
    private View view;

    /**
     * 存放在界面显示的列表
     * */
    List<FriendCircleModel>dateList;

    private ArrayList<HashMap<String,Object>> listItem;
    private RefreshListView rListView;
    /**
     * 用于将数据绑定到ListView的适配器
     */
    private MyAdapter listAdatper;

    /**
     * 朋友圈业务逻辑类
     * */
    private FriendCircleService friendCircleService;

    /**
     * 投票业务逻辑
     * */
    private UpvoteService upvoteService;
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

                    FriendCircleModel friendCircleModel = dateList.get(position);


                    //判断是否已经点赞过
                    if (friendCircleModel.getUpvoteFlag() == 1){
                        return;
                    }

                    map.put("glorification_state",friendCircleModel.getAddUserVoteText(getActivity()));

                    //此处将点赞的动作发到服务器，将值重新设置
                    upvoteService.addUpvote(dateList.get(position).getCircleId(), getActivity(), new Listener() {
                        @Override
                        public void onSuccess() {
                            map.put("discovery_icon",R.drawable.glorification_true);
                            listItem.set(position,map);
                            friendCircleModel.setUpvoteFlag(1);
                            //ToastManager.toast(getActivity(),"点击"+position+"个");
                            listAdatper.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String msg) {
                            ToastManager.toast(getActivity(),msg);
                        }
                    });



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
        upvoteService = new UpvoteService();

        rListView = (RefreshListView)view.findViewById(R.id.refreshlistview);
        listItem = new ArrayList<HashMap<String, Object>>();

        listAdatper = new MyAdapter(getActivity(),listItem,
                R.layout.list_item_discovery,
                new String[]{
                        "user_img",
                        "discovery_icon",
                        "discovery_username",
                        "list_item_main",
                        "list_item_time",
                        "glorification_state"
                },
                new int[]{
                        R.id.user_img,
                        R.id.glorification,
                        R.id.discovery_username,
                        R.id.list_item_main,
                        R.id.list_item_time,
                        R.id.glorification_state
                });
        getData();
        rListView.setAdapter(listAdatper);
        rListView.setOnRefreshListener(this);
        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastManager.toast(getActivity(),position+"");
                Intent intent = new Intent(getActivity(), CommentaryActivity.class);
                intent.putExtra("circleId",dateList.get(position-1).getCircleId());
                intent.putExtra("sexFrameState",dateList.get(position-1).getSexFrameState());
                getActivity().startActivity(intent);
            }
        });

        return view;
    }


    public void getData(){
        friendCircleService.getDownPullList(getActivity(), new GetListListener() {
            @Override
            public void onSuccess(List list) {
                Log.i("数量",""+list.size());
                for (int i=0;i<list.size();i++)  {
                    dateList  = list;
                    FriendCircleModel friendCircleModel = (FriendCircleModel) list.get(i);
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    try{
                        map.put("user_img",(ImageEncodeUtil.base64ToBitmap(friendCircleModel.getUserPhoto())));
                    }catch (Exception e){
                        map.put("user_img",R.drawable.ic_launcher);
                        e.printStackTrace();
                    }

                    //加载图片
                    listAdatper.setViewBinder(new SimpleAdapter.ViewBinder() {
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



                    map.put("list_item_main",friendCircleModel.getShareText());
                    map.put("list_item_time", TimeService.getIntervalTime(friendCircleModel.getShareTime()));
                    map.put("glorification_state",friendCircleModel.getProcessVoteText());

                    //设置点赞按钮的图标
                    if (friendCircleModel.getUpvoteFlag() == null || friendCircleModel.getUpvoteFlag() == 0){
                        map.put("discovery_icon",R.drawable.glorification_false);
                    }
                    else{
                        map.put("discovery_icon",R.drawable.glorification_true);
                    }

                    listItem.add(map);
                }
            }

            @Override
            public void onFailure(String msg) {
                //ToastManager.toast(getActivity(),msg);
            }
        });
    }



    @Override
    public void onDownPullRefresh() {
        rListView.setEnabled(false);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                getData();
                listAdatper.notifyDataSetChanged();
                rListView.setEnabled(true);
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
