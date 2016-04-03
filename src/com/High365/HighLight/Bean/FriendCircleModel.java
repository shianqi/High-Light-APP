package com.High365.HighLight.Bean;

/**
 * Created by HUPENG on 2/13/16.
 * 这个是朋友圈的JavaBean对象
 */

import android.content.Context;
import com.High365.HighLight.Util.SharedPreferencesManager;

import java.sql.Struct;
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
    private Timestamp shareTime;
    private String userPhoto;
    private Integer upvoteFlag;
    private String voteText;

    // Constructors

    /** default constructor */
    public FriendCircleModel() {
    }

    /** full constructor */
    public FriendCircleModel(String userId, Timestamp sexTime,
                        Integer sexSubjectiveScore, Integer sexObjectiveScore,
                        String sexFrameState, String city, String shareText,
                        Timestamp shareTime, String userPhoto, Integer upvoteFlag,
                        String voteText) {
        this.userId = userId;
        this.sexTime = sexTime;
        this.sexSubjectiveScore = sexSubjectiveScore;
        this.sexObjectiveScore = sexObjectiveScore;
        this.sexFrameState = sexFrameState;
        this.city = city;
        this.shareText = shareText;
        this.shareTime = shareTime;
        this.userPhoto = userPhoto;
        this.upvoteFlag = upvoteFlag;
        this.voteText = voteText;
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

    public Timestamp getShareTime() {
        return this.shareTime;
    }

    public void setShareTime(Timestamp shareTime) {
        this.shareTime = shareTime;
    }

    public String getUserPhoto() {
        return this.userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Integer getUpvoteFlag() {
        return this.upvoteFlag;
    }

    public void setUpvoteFlag(Integer upvoteFlag) {
        this.upvoteFlag = upvoteFlag;
    }

    public String getVoteText() {
        return this.voteText;
    }

    public void setVoteText(String voteText) {
        this.voteText = voteText;
    }

    public String getProcessVoteText(){
        if (Integer.parseInt(getVoteText().split(":")[1])>5){
           return  getVoteText().split(":")[0] + "等" + Integer.parseInt(getVoteText().split(":")[1]) + "觉得很赞";
        }else{
            if (Integer.parseInt(getVoteText().split(":")[1]) == 0){
                return  "成为第一个点赞的人吧";
            }else{
                return  getVoteText().split(":")[0] +  "觉得很赞";
            }
        }
    }

    public String getAddUserVoteText(Context context){
        userId = new SharedPreferencesManager(context).readString("UserID");
        Integer num = Integer.parseInt(getVoteText().split(":")[1]);
        String text = getVoteText().split(":")[0];
        if (num == 0){
            text = userId  + text;
        }else{
            text = userId  + "、" + text;
        }
        num ++;

        this.setVoteText(text + ":" + num);
        return getProcessVoteText();
    }
}