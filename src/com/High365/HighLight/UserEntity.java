package com.High365.HighLight;

/**
 * 用户类
 */
public class UserEntity {
    private String userName;
    private int userSex;
    private int userPhone;
    private String graphPassword;
    private String password;
    private String emailAddress;
    private String phoneNumber;
    private boolean fixAble=false;

    public boolean isFixAble() {
        return fixAble;
    }

    public void setFixAble(boolean fixAble) {
        this.fixAble = fixAble;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public int getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(int userPhone) {
        this.userPhone = userPhone;
    }

    public String getGraphPassword() {
        return graphPassword;
    }

    public void setGraphPassword(String graphPassword) {
        this.graphPassword = graphPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
