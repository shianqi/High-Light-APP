package com.High365.HighLight;

import java.util.Date;

/**
 * @author HUPENG
 * @version 1.0
 * 这个JavaBean对应本地数据库的UserInfo表
 */
public class UserInfoBean {
    private String userID;
    private String userPwd;
    private String userGesturePwd;
    private String username;
    private String userEmail;
    private Date userBirthDay;
    private byte[] userPhoto;
    private Integer userGender;
    private Date userSphysiologicalDay;
    private Date userEphysiologicalDay;
    private String userPhone;

    /**
     * 判断用户信息当前是否可修改
     */
    private Boolean fixAble;

    public Boolean isFixAble() {
        return fixAble;
    }

    public void setFixAble(Boolean fixAble) {
        this.fixAble = fixAble;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserGesturePwd() {
        return userGesturePwd;
    }

    public void setUserGesturePwd(String userGesturePwd) {
        this.userGesturePwd = userGesturePwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getUserBirthDay() {
        return userBirthDay;
    }

    public void setUserBirthDay(Date userBirthDay) {
        this.userBirthDay = userBirthDay;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    public Date getUserEphysiologicalDay() {
        return userEphysiologicalDay;
    }

    public void setUserEphysiologicalDay(Date userEphysiologicalDay) {
        this.userEphysiologicalDay = userEphysiologicalDay;
    }

    public Date getUserSphysiologicalDay() {
        return userSphysiologicalDay;
    }

    public void setUserSphysiologicalDay(Date userSphysiologicalDay) {
        this.userSphysiologicalDay = userSphysiologicalDay;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}