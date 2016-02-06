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
     */
    public SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 向SharedPreferences中写入String类型的值
     * @param key 所写入值的key
     * @param value 所写入的值
     */
    public void writeString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 向SharedPreferences中写入Integer类型的值
     * @param key 所写入值的key
     * @param value 所写入的值
     */
    public void writeInteger(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    /**
     * 从SharedPreferences中读取String类型的值
     * @param key 所读取值的key
     * @return 所要读取的值，如果没有该值则返回空
     */
    public String readString(String key){
        return sharedPreferences.getString(key,null);
    }

    /**
     * 从SharedPreferences中读取Integer类型的值
     * @param key 所读取值的key
     * @return 所要读取的值，如果没有该值则返回空
     */
    public int readInteger(String key){
        return sharedPreferences.getInt(key,-1);
    }

    /**
     * 清除用户信息
     */
    public void logout(){
        editor.putString("WP",null);
        editor.commit();
    }
}
