package com.High365.HighLight.Bean;

/**
 * Created by HUPENG on 2/13/16.
 * 这个是朋友圈的JavaBean对象
 */

import java.sql.Timestamp;

/**
 * Friendcircle entity. @author MyEclipse Persistence Tools
 */

public class FriendCircleModel implements java.io.Serializable {

    // Fields
    /**
     * 在数据库中的唯一标识符
     * */
    private Integer circleId;
    /**
     * 用户的ID,是用户的唯一凭证
     * */
    private String userId;
    /**
     * 性的时间,是开始时间与结束时间的间隔
     * */
    private Timestamp sexTime;
    /**
     * 性的主观分数
     * */
    private Integer sexSubjectiveScore;
    /**
     * 性的客观得分
     * */
    private Integer sexObjectiveScore;
    /**
     * 性的状态帧
     * */
    private String sexFrameState;
    /**
     * 城市,以ip来获取,在服务器端进行获取,不在Android端获取
     * */
    private String city;

    // Constructors

    /** default constructor */
    public FriendCircleModel() {
    }

    /** full constructor */
    public FriendCircleModel(String userId, Timestamp sexTime,
                        Integer sexSubjectiveScore, Integer sexObjectiveScore,
                        String sexFrameState, String city) {
        this.userId = userId;
        this.sexTime = sexTime;
        this.sexSubjectiveScore = sexSubjectiveScore;
        this.sexObjectiveScore = sexObjectiveScore;
        this.sexFrameState = sexFrameState;
        this.city = city;
    }

    // Property accessors

    public Integer getCircleId() {
        return this.circleId;
    }

    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getSexTime() {
        return this.sexTime;
    }

    public void setSexTime(Timestamp sexTime) {
        this.sexTime = sexTime;
    }

    public Integer getSexSubjectiveScore() {
        return this.sexSubjectiveScore;
    }

    public void setSexSubjectiveScore(Integer sexSubjectiveScore) {
        this.sexSubjectiveScore = sexSubjectiveScore;
    }

    public Integer getSexObjectiveScore() {
        return this.sexObjectiveScore;
    }

    public void setSexObjectiveScore(Integer sexObjectiveScore) {
        this.sexObjectiveScore = sexObjectiveScore;
    }

    public String getSexFrameState() {
        return this.sexFrameState;
    }

    public void setSexFrameState(String sexFrameState) {
        this.sexFrameState = sexFrameState;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}