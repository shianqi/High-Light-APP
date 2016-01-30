package com.High365.HighLight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *  @author HUPENG
 *  将图片与base64相互装换的工具类
 */
public class ImageEncodeUtil {

    public static String bitmapToBase64(Bitmap bitmap){
        String result="";
        ByteArrayOutputStream bos=null;
        try {
            if(null!=bitmap){
                bos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将bitmap放入字节数组流中

                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte []bitmapByte=bos.toByteArray();
                result=Base64.encodeToString(bitmapByte, Base64.NO_CLOSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    /*
     *bitmap转base64
     */
    public static Bitmap base64ToBitmap(String base64String){
        byte[] bytes;
        bytes = Base64.decode(base64String, Base64.NO_CLOSE);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }




}
