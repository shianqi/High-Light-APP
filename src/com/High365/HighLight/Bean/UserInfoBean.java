package com.High365.HighLight.Bean;

import java.util.Date;

/**
 * @author HUPENG
 * @version 1.0
 * 这个JavaBean对应本地数据库的UserInfo表
 */
public class UserInfoBean {
    /**
     * 用户名,用户的唯一标识
     * */
    private String userId;
    /**
     * 用户的密码,计算后保存在本地数据库以及在线数据库中
     * */
    private String userPwd;
    /**
     * 用户的手势密码
     * */
    private String userGesturePwd;
    /**
     * 用户昵称
     * */
    private String userName;
    /**
     * 用户邮箱
     * */
    private String userEmail;
    /**
     * 用户的生日
     * */
    private Date userBirthDay;
    /**
     * 用户的照片,将用户的照片的bitmap文件经过base64计算后的值,计算方法见ImageEncodeUtil.class
     * */
    private String userPhoto;
    /**
     * 用户的性别
     * 1.男
     * 0.女
     * 默认值.-1
     * 当值为-1时显示为男
     * */
    private Integer userGender;
    /**
     * 用户生理期开始时间
     * */
    private Date userSphysiologicalDay;
    /**
     * 用户生理期结束时间
     * */
    private Date userEphysiologicalDay;
    /**
     * 用户的手机号码
     * */
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
