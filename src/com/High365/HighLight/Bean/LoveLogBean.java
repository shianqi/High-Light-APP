package com.High365.HighLight.Bean;

import android.content.Intent;

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

    public String getSexDateToString(){
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date(sexStartTime.getTime()))+"";
    }

    public String getSexStartTimeToString(){
        DateFormat format=new SimpleDateFormat("HH:mm");
        return format.format(new Date(sexStartTime.getTime()))+"";
    }

    public String getSexTimeToString(){
        DateFormat format=new SimpleDateFormat("mm′ss″");
        return format.format(new Date(sexTime.getTime()))+"";
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

    public int getSexFrameStateSize(){
        return sexFrameState.length()/2;
    }

    public int getSexFrameStateByNumber(int i){
        return Integer.parseInt(sexFrameState.substring(i*2,i*2+2));
    }
}
