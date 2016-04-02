package com.High365.HighLight.Bean;

import android.content.Intent;
import android.util.Log;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author HUPENG
 * @version 1.0
 * 本JavaBean对应本地数据库的LovaLog表
 */
public class LoveLogBean {
    /**
     * 日志ID
     * */
    private Integer logID;
    /**
     * 用户名,用户的唯一标识
     * */
    private String userId;
    /**
     * 性的开始时间
     * */
    private Timestamp sexStartTime;
    /**
     * 性的结束时间
     * */
    private Timestamp sexEndTime;
    /**
     * 性的持续时间
     * */
    private Timestamp sexTime;
    /**
     * 性高潮时间
     * */
    private Timestamp sexHighTime;
    /**
     * 主观评分,由用户给出
     * */
    private Integer sexSubjectiveScore;
    /**
     * 客观评分,由计算所得
     * */
    private Integer sexObjectiveScore;
    /**
     * 性的状态帧
     * */
    private String sexFrameState;
    /**
     * 保存在本地的录音文件的文件名
     * */
    private String recordFileName;
    /**
     * 上传成功标识,未上传成功值为0,上传成功以后为非零值
     * */
    private Integer updateFlag;

    /**
     * 简易状态帧
     * */
    private String simpleSexFrameState;

    /**
     * 构造函数
     * */
    public LoveLogBean(){
        if (simpleSexFrameState == null && sexFrameState != null){
            getSimpleState();
        }
        int i;
    }

    public String getSexDateToString(){
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date(sexStartTime.getTime()))+"";
    }

    public String getSexStartTimeToString(){
        DateFormat format=new SimpleDateFormat("HH:mm");
        return format.format(new Date(sexStartTime.getTime()))+"";
    }

    public String getSexTimeToString(){
        try{
            DateFormat format=new SimpleDateFormat("mm′ss″");
            return format.format(new Date(sexTime.getTime()))+"";
        }catch (Exception e){
            return "0'00''";
        }

    }



    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getSexStartTime() {
        return sexStartTime;
    }

    public void setSexStartTime(Timestamp sexStartTime) {
        this.sexStartTime = sexStartTime;
    }

    public Timestamp getSexEndTime() {
        return sexEndTime;
    }

    public void setSexEndTime(Timestamp sexEndTime) {
        this.sexEndTime = sexEndTime;
    }

    public Timestamp getSexTime() {
        return sexTime;
    }

    public void setSexTime(Timestamp sexTime) {
        this.sexTime = sexTime;
    }

    public Timestamp getSexHighTime() {
        return sexHighTime;
    }

    public void setSexHighTime(Timestamp sexHighTime) {
        this.sexHighTime = sexHighTime;
    }

    public Integer getSexSubjectiveScore() {
        return sexSubjectiveScore;
    }

    public void setSexSubjectiveScore(Integer sexSubjectiveScore) {
        this.sexSubjectiveScore = sexSubjectiveScore;
    }

    public Integer getSexObjectiveScore() {
        return sexObjectiveScore;
    }

    public void setSexObjectiveScore(Integer sexObjectiveScore) {
        this.sexObjectiveScore = sexObjectiveScore;
    }

    public String getSexFrameState() {
        return sexFrameState;
    }

    public void setSexFrameState(String sexFrameState) {
        this.sexFrameState = sexFrameState;
    }

    public String getRecordFileName() {
        return recordFileName;
    }

    public void setRecordFileName(String recordFileName) {
        this.recordFileName = recordFileName;
    }

    public Integer getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Integer updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getSimpleSexFrameState() {
        if (simpleSexFrameState==null){
            simpleSexFrameState = getSimpleState();
        }
        return simpleSexFrameState;
    }

    public void setSimpleSexFrameState(String simpleSexFrameState) {
        this.simpleSexFrameState = simpleSexFrameState;
    }

    public int getSexFrameStateSize(){
        return getSimpleState().length()/2;
    }

    public int getSexFrameStateByNumber(int i){
            return  Integer.parseInt(getSimpleState().substring(i*2,i*2+2));
    }

    /**
     * 加入到性的状态帧
     * */
    public void addToSexFrameState(double v){


        // 加入到正常的状态帧

        if (sexFrameState == null){
            sexFrameState = "";
            sexFrameState += v;
        }else{
            sexFrameState += ":";
            sexFrameState += v;
        }


    }

    private String getSimpleState(){
        if (simpleSexFrameState!=null){
            return simpleSexFrameState;
        }else {
            String sexState = sexFrameState;
            String sexStateTemp = sexState;
            sexState = "";
            //处理
            for (int k=0;k<sexStateTemp.split(":").length;k++){
                double mean = Double.parseDouble(sexStateTemp.split(":")[k]);
                double volume = 10 * Math.log10(mean);
                int volmeT = (int)volume;
                if (volmeT > 99){
                    volmeT = 99;
                }
                if (volmeT < 10){
                    sexState += '0';
                }
                sexState += volmeT;
            }


            int index = 0;
            int n = 0;
            int sum = 0;
            String temp = "";
            String tmp = "";
            if (sexState.length() > 100) {
                for (int j = 0; j < sexState.length(); j = j + 2) {

                    if (index == j * 51 / sexState.length()) {
                        tmp = "";
                        tmp = tmp + sexState.charAt(j) + sexState.charAt(j + 1);
                        sum += Integer.parseInt(tmp);
                        n++;
                    } else {
                        index = j * 51 / sexState.length();
                        if (sum / n < 10) {
                            temp += "0" + sum / n;
                        } else {
                            temp += sum / n;
                        }
                        sum = 0;
                        n = 0;

                        tmp = "";
                        tmp = tmp + sexState.charAt(j) + sexState.charAt(j + 1);
                        sum += Integer.parseInt(tmp);
                        n++;
                    }
                }
                //loveLogBean.setSexFrameState(temp);
                simpleSexFrameState = temp;
            }else{
//                loveLogBean.setSexFrameState(sexState);
                simpleSexFrameState = sexState;
            }
        }
        return  simpleSexFrameState;
    }


}
