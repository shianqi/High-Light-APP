package com.High365.HighLight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author 史安琪
 * 这是第二个页面
 */
public class PageTwo extends Fragment{

    private ListView list;
    private List<LoveLogBean> listItem;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String,Object>> itemArrayList;
    private SharedPreferencesManager sharedPreferencesManager;
    private String UserID;
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
     * 获得每一个Item的信息
     */
    public void getItemInformation(){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("TIME","2016-01-10");
        for(int i=0;i<10;i++){
            itemArrayList.add(map);
        }

    }

    /**
     * 初始化过程，负责类的实例化和组件的绑定
     */
    public void init(){
        itemArrayList = new ArrayList<HashMap<String, Object>>();
        sharedPreferencesManager=new SharedPreferencesManager(getActivity());
        sqlLiteManager = new SqlLiteManager(getActivity());
        list = (ListView)getActivity().findViewById(R.id.ListView);
        /**
         * 适配器
         */
        simpleAdapter = new SimpleAdapter(getActivity(),
                itemArrayList,
                R.layout.list_items,
                new String[]{"TIME"},
                new int[]{R.id.list_item_title});

        listItem = getListItem();
        getItemInformation();
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastManager.toast(getActivity(),"点击第:"+position+"个");
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("操作")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
}
