package com.High365.HighLight;

import java.util.Date;

/**
 * @author HUPENG
 * @version 1.0
 * 这个JavaBean对应本地数据库的UserInfo表
 */
public class UserInfoBean {
    private String userID;
    private String userPWD;
    private String userGesturePWD;
    private String username;
    private String userEmail;
    private Date userBirthDay;
    private byte[] userPhoto;
    private int userGender;
    private Date userSPhysiologicalDay;
    private Date userEPhysiologicalDay;
    private String userPhone;

    /**
     * 判断用户信息当前是否可修改
     */
    private boolean fixAble = false;

    public boolean isFixAble() {
        return fixAble;
    }

    public void setFixAble(boolean fixAble) {
        this.fixAble = fixAble;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPWD() {
        return userPWD;
    }

    public void setUserPWD(String userPWD) {
        this.userPWD = userPWD;
    }

    public String getUserGesturePWD() {
        return userGesturePWD;
    }

    public void setUserGesturePWD(String userGesturePWD) {
        this.userGesturePWD = userGesturePWD;
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

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public Date getUserSPhysiologicalDay() {
        return userSPhysiologicalDay;
    }

    public void setUserSPhysiologicalDay(Date userSPhysiologicalDay) {
        this.userSPhysiologicalDay = userSPhysiologicalDay;
    }

    public Date getUserEPhysiologicalDay() {
        return userEPhysiologicalDay;
    }

    public void setUserEPhysiologicalDay(Date userEPhysiologicalDay) {
        this.userEPhysiologicalDay = userEPhysiologicalDay;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
