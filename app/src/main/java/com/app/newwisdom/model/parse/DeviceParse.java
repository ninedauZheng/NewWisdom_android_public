package com.app.newwisdom.model.parse;

import com.app.newwisdom.manager.DeviceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ninedau_zheng on 2017/8/3.
 */

public class DeviceParse {

    public void parseJson(String value) throws JSONException {
        JSONObject obj = new JSONObject(value);
        DeviceManager dm = DeviceManager.getInstance();
        dm.setIMEI(obj.getString("imei"));
        dm.setTimeGap( obj.getLong("timestamp"));
    }

}
