package com.High365.HighLight.Bean;

/**
 * 此内部类为一个JavaBean
 * 为注册结果Json字符串所对应的bean
 * */
public class RegisterModel{

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    private  Integer status;
    private String secretKey;
    private String errorInfo;

}