package com.High365.HighLight;

/**
 * 此内部类为一个JavaBean
 * 为更新用户数据结果的Json对象所对应的bean
 */
class UpdateModel{
    private Integer status;
    private String errorInfo;
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getErrorInfo() {
        return errorInfo;
    }
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}