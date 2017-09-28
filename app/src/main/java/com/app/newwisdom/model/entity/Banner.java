package com.app.newwisdom.model.entity;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */

public class Banner implements Serializable{

    public String created_at;
    public int id;
    public String link;
    public String pic;
    //  状态 1为打开，0为关闭
    public int status;
    public String title;
    public int type;
    public String updated_at;

    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 0;

}
