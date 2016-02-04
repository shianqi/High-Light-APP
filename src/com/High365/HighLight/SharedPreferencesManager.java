package com.High365.HighLight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author HUPENG
 * @version 1.0
 * 本页主要做本地非数据库文件的本地存储
 */
public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * 初始化
     * */
    public SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 写入String型的K,V对
     * */
    public void writeString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 写入Int型的K,V对
     * */
    public void writeInteger(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public String readString(String key){
        return sharedPreferences.getString(key,null);
    }

    public int readInteger(String key){
        return sharedPreferences.getInt(key,-1);
    }

    public void logout(){

    }
}
