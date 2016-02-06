package com.High365.HighLight;

import java.util.Date;

/**
 * @author HUPENG
 * @version 1.0
 * 这个JavaBean对应本地数据库的UserInfo表
 */
public class UserInfoBean {
    private String userId;
    private String userPwd;
    private String userGesturePwd;
    private String userName;
    private String userEmail;
    private Date userBirthDay;
    private String userPhoto;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
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

    public UserInfoBean(){
        userGender = -1;
    }

}
