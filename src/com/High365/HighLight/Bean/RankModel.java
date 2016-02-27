package com.High365.HighLight.Bean;

/**
 * 排名model,将从服务器上获取的json字符串转换成实体类时所用的model
 * */
public class RankModel{
    private String userID;
    private Integer sexSubjectiveScore;
    private Integer sexObjectiveScore;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
}