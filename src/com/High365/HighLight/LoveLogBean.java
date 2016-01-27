package com.High365.HighLight;

import java.sql.Timestamp;

/**
 * @author HUPENG
 * @version 1.0
 * 本JavaBean对应本地数据库的LovaLog表
 */
public class LoveLogBean {
    private int logID;
    private String userID;
    private Timestamp sexStartTime;
    private Timestamp sexEndTime;
    private Timestamp sexTime;
    private Timestamp sexHighTime;
    private int sexSubjectiveScore;
    private int sexObjectiveScore;
    private String sexFrameState;

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public int getSexSubjectiveScore() {
        return sexSubjectiveScore;
    }

    public void setSexSubjectiveScore(int sexSubjectiveScore) {
        this.sexSubjectiveScore = sexSubjectiveScore;
    }

    public int getSexObjectiveScore() {
        return sexObjectiveScore;
    }

    public void setSexObjectiveScore(int sexObjectiveScore) {
        this.sexObjectiveScore = sexObjectiveScore;
    }

    public String getSexFrameState() {
        return sexFrameState;
    }

    public void setSexFrameState(String sexFrameState) {
        this.sexFrameState = sexFrameState;
    }
}
