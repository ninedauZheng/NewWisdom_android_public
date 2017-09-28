package com.app.newwisdom.model.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ninedau_zheng on 2017/8/28.
 */

public class User implements Serializable{

    private long expiredTime;
    private String token;
    private long userId;
    private String userIP;
    private String phoneNum;


    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = myFmt.parse(expiredTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.expiredTime = date.getTime();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "User{" +
                "expiredTime=" + expiredTime +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", userIP='" + userIP + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
