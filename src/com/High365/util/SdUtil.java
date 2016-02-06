package com.High365.util;

import android.os.Environment;
/**
 * 此类是监测是否有SD卡<br>
 * Create at 2012-8-17 上午10:14:40
 * @author XuZhiwei (xuzw13@gmail.com)
 */
public class SdUtil {
    /**
     * 检查是否存在SDCard
     * @return SDCard是否存在
     */
    public static boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
}
