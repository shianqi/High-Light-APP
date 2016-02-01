package com.High365.HighLight;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.List;


/**
 * @author 史安琪
 * 这是第二个页面
 */
public class PageTwo extends Fragment{

    private ListView list;
    private List<LoveLogBean> listItem;
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
        listItem = getListItem();
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
        list = (ListView)getActivity().findViewById(R.id.ListView);
    }

    public List<LoveLogBean> getListItem(){
        UserID = sharedPreferencesManager.readString("UserID");
        Log.i("UserID:", ""+UserID);
        return sqlLiteManager.getLoveLogsByUserID(UserID);
    }
}
