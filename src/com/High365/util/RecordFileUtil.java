package com.High365.util;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 录音文件处理工具类
 * @author HUPENG
 * @version 1.0
 */
public class RecordFileUtil {
    //文件路径对象
    private File fpath;

    //loveLogID
    private int loveLogID;

    //流对象
    private DataOutputStream dos = null;

    //文件名
    private String fileName;

    //录音文件对象
    private File audioFile;

    //初始化值
    public void init(int loveLogID){
        //文件路径
        String pathStr = Environment.getExternalStorageDirectory().getAbsolutePath()+"/data/highLightRecordFile/";
        fpath = new File(pathStr);
        //判断文件夹是否存在,若不存在则创建
        if (!fpath.exists()){
            fpath.mkdirs();
        }
        this.loveLogID = loveLogID;
    }

    public RecordFileUtil(int loveLogID){
        //每个loveLog对应一个录音文件
        init(loveLogID);
    }

    /**
     * 写入buffer
     * */
    public void writeBuffer(short buffer){
        if (dos==null){
            try {
                audioFile = File.createTempFile("file_" + loveLogID+"", ".pcm", fpath);
                dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioFile)));
                fileName = audioFile.getName();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            dos.writeShort(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 停止写入buffer
     * */
    public void stopWriteBuffer(){
        if (dos!=null){
            try{
                dos.close();
                dos = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        stopWriteBuffer();
        super.finalize();
    }

    /**
     * 得到文件名
     * */
    public String getFileName(){
        return fileName;
    }
}
