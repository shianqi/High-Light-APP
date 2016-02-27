package com.High365.HighLight.Bean;

/**
 * 此内部类为一个JavaBean
 * 为登录结果Json字符串所对应的bean
 * */
public class LoginModel{
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
    /**
     * status:表示返回的状态码,1:登录成功,0:登录失败
     * errorInfo:当登录失败的返回的失败信息
     * secretKey:
     * */
    private  Integer status;
    private String secretKey;
    private String errorInfo;
}