package com.High365.HighLight.Bean;

/**
 * Created by HUPENG on 2/13/16.
 * 这个是朋友圈的JavaBean对象
 */

import java.sql.Timestamp;

/**
 * Friendcircle entity. @author MyEclipse Persistence Tools
 */

public class FriendCircleModel {

    // Fields

    private Integer circleId;
    private String userId;
    private Timestamp sexTime;
    private Integer sexSubjectiveScore;
    private Integer sexObjectiveScore;
    private String sexFrameState;
    private String city;
    private String shareText;
    private String upvoteText;
    private boolean upvoteFlag;
    // Constructors

    /** default constructor */
    public FriendCircleModel() {
    }

    /** full constructor */
    public FriendCircleModel(String userId, Timestamp sexTime,
                        Integer sexSubjectiveScore, Integer sexObjectiveScore,
                        String sexFrameState, String city, String shareText) {
        this.userId = userId;
        this.sexTime = sexTime;
        this.sexSubjectiveScore = sexSubjectiveScore;
        this.sexObjectiveScore = sexObjectiveScore;
        this.sexFrameState = sexFrameState;
        this.city = city;
        this.shareText = shareText;
    }

    // Property accessors

    public Integer getCircleId() {
        return this.circleId;
    }

    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public String getUserId() {
        return this.userId;
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

    public String getShareText() {
        return this.shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getUpvoteText() {
        return upvoteText;
    }

    public void setUpvoteText(String upvoteText) {
        this.upvoteText = upvoteText;
    }

    public boolean isUpvoteFlag() {
        return upvoteFlag;
    }

    public void setUpvoteFlag(boolean upvoteFlag) {
        this.upvoteFlag = upvoteFlag;
    }
}