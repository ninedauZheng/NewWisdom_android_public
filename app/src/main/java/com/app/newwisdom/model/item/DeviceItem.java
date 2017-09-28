package com.app.newwisdom.model.item;

/**
 * Created by ninedau_zheng on 2017/7/24.
 */

public class DeviceItem{

    public int id;
    public int type;
    public String title;
    public DeviceItem childItem;
    public boolean isOpen = false;

    public DeviceItem(int type ,String title) {
        this.type = type;
        this.title = title;
    }

    public DeviceItem(int id,int type ,String title){
        this.id = id;
        this.type= type;
        this.title = title;

    }
}
