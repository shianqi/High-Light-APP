package com.High365.HighLight.Bean;

/**
 * 返回给客户端的升级功能的bean对象
 * */
public class VersionModel {
    private Integer state;
    private String url;
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}