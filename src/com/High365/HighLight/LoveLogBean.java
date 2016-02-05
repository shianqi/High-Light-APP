package com.High365.HighLight;

import android.content.Intent;

import java.sql.Timestamp;

/**
 * @author HUPENG
 * @version 1.0
 * 本JavaBean对应本地数据库的LovaLog表
 */
public class LoveLogBean {
    private Integer logID;
    private String userId;
    private Timestamp sexStartTime;
    private Timestamp sexEndTime;
    private Timestamp sexTime;
    private Timestamp sexHighTime;
    private Integer sexSubjectiveScore;
    private Integer sexObjectiveScore;
    private String sexFrameState;
    private String recordFileName;
    private Integer updateFlag;

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
}
