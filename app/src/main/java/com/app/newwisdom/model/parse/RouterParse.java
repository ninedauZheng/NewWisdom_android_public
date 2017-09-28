package com.app.newwisdom.model.parse;

import android.util.Log;

import com.app.newwisdom.model.entity.ConnectedDevice;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.model.entity.SimBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/8/2.
 */

public class RouterParse {
    /*
{
  "battery_charging": true,           正在充电（是否）
  "battery_vol_percent": 100,         电量百分比
  "card_status": 5,                   卡状态，这个有延时
  "card_used": 628.533,               路由的使用总流量  单位M
  "connected_at": "2017-07-28 14:58:00",     本次路由设备的连接时间
  "created_at": "2017-07-28 14:58:00",      设备本次开启时间
  "download": 0,                          实时下载速度
  "enable_limit": false,                是否限速
  "firmware_version": "",               固件版本
  "hardware_version": "",               硬件版本
  "iccid": "89860316432028899461",       卡的iccid
  "id": 1,
  "imei": "861179030343495",            卡1的imei，就是以这个为主key
  "imei2": "000000000000000",            卡2的imei
  "imsi": "",                       sim卡识别码
  "is_online": true,                是否当前在线（设备）
  "key": "88888888",                路由密码
  "lan_ip": "",                     路由本地
  "lan_mac": "38:1C:23:00:A5:0A",   路由mac
  "max_connections": 8,             当前连接的数量
  "network_provider": "\u4e2d\u56fd\u7535\u4fe1",     网络运营商
  "network_type": "CDMA - 1xRTT",       网络制式
  "onlines": [                      接入路由器的设备列表,这个并不是很准确，需要通过station_mac筛选
    {
      "hostname": "zwc-naifei-pc",
      "ip": "192.168.0.106",
      "mac": "12:22:67:b5:bd:f2"
    }
  ],
  "rate": 0,                  当前限制网速 Kb = KB / 8
  "signal_level": 3,        信号强度,总共５格
  "sim": {                sim卡信息
    "currentPlanExpireTime": "2017-07-31 00:00:00",   卡套餐截止时间
    "currentPlanName": "",                卡套餐名称
    "currentPlanStartTime": "2017-07-22 10:30:14", 卡套餐开始时间

    "status": 5,                卡状态， （５激活，６关机）
    "totalFlowSize": 660.6455078125,      总流量
    "usedFlowSize": 628.693359375           已使用流量
  },
  "ssid": "4G MiFi A50A",       wifi名称
  "station_mac": "",          如果有通过ｗｉｆｉ连接的设备，设备的ｍａｃ地址，以;分割
  "status": "Connected",      连接状态
  "updated_at": "2017-07-30 00:24:00",  固件上次升级时间
  "upload": 0,          上传速度
  "wan_ip": null
}


{
    "iccid": "89860316442023072220",
    "imsi": "",
    "imei": "861179030343362",
    "imei2": "000000000000000",
    "lan_ip": "",
    "lan_mac": "38:1C:23:00:A5:04",
    "ssid": "4G MiFi A504",
    "key": "88888888",
    "max_connections": 8,
    "status": "Connected",
    "station_count": 3,
    "station_mac": "b4:0b:44:a2:48:5c;f4:5c:89:97:c0:4b;20:55:31:58:19:47",
    "signal_level": 3,
    "network_type": "CDMA - eHRPD",
    "network_provider": "中国电信",
    "battery_charging": false,
    "battery_vol_percent": 64,
    "upload": 0,
    "download": 0,
    "firmware_version": "",
    "hardware_version": "",
    "enable_limit": false,
    "rate": 0,
    "onlines": "[{\"ip\":\"192.168.1.200\",\"mac\":\"f4:5c:89:97:c0:4b\",\"hostname\":\"Mac-Pro\"},{\"ip\":\"192.168.1.17\",\"mac\":\"b4:0b:44:a2:48:5c\",\"hostname\":\"Smartisan-U2-Pro\"},{\"ip\":\"192.168.1.251\",\"mac\":\"20:55:31:58:19:47\",\"hostname\":\"android-54951da556ff3651\"}]"
}
*/
    public Router parseRoute(String json, boolean isTrueRouter) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Router router = new Router();
        ArrayList<ConnectedDevice> connectedDevicesList = new ArrayList<>();
        router.firmwareVersion = obj.getString("firmware_version");
        router.hardwareVersion = obj.getString("hardware_version");
        router.iccid = obj.getString("iccid");
//        router.iccid = "89860316432028899461";
        router.imei = obj.getString("imei");
        router.imei2 = obj.getString("imei2");
        router.imsi = obj.getString("imsi");
        router.key = obj.getString("key");
        router.lanIP = obj.getString("lan_ip");
        router.lanMac = obj.getString("lan_mac");
        router.networkProvider = obj.getString("network_provider");
        router.networkType = obj.getString("network_type");
        router.ssid = obj.getString("ssid");
        router.stationMac = obj.getString("station_mac");
        router.status = obj.getString("status");

        router.batteryCharging = obj.getBoolean("battery_charging");
        router.enableLimit = obj.getBoolean("enable_limit");

        router.batteryVolPercent = obj.getInt("battery_vol_percent");
        router.maxConnections = obj.getInt("max_connections");
        router.signalLevel = obj.getInt("signal_level");

        router.upload = obj.getDouble("upload");
        router.download = obj.getDouble("download");
        router.rate = obj.getDouble("rate");
        JSONArray arrOnLines = null;

        if (isTrueRouter) {
            String value = obj.getString("onlines");
            value = value.replace("\\", "");
            value = "{\"onlines\":" + value + "}";
            Log.i("ninedau", "value = " + value);
            arrOnLines = new JSONObject(value).getJSONArray("onlines");
        } else {
            arrOnLines = obj.getJSONArray("onlines");
        }


        if (arrOnLines != null) {
            for (int i = 0; i < arrOnLines.length(); i++) {
                connectedDevicesList.add(parseConnectedDevice(arrOnLines.getJSONObject(i)));
            }
        }

        if (!isTrueRouter) {
            router.connectAt = obj.getString("connected_at");
            router.createAt = obj.getString("created_at");
            router.updatedTime = obj.getString("updated_at");
            router.wan_ip = obj.getString("wan_ip");

            router.isOnline = obj.getBoolean("is_online");

            router.id = obj.getInt("id");
            router.cardStatus = obj.getInt("card_status");

            router.cardUsed = obj.getDouble("card_used");

            ArrayList<SimBean> simBeansList = new ArrayList<>();
            try {
                JSONObject arrSim = obj.getJSONObject("sim");
                simBeansList.add(parseSimBean(arrSim));
            } catch (Exception e) {

            }
            router.simList = simBeansList;

        }
        router.onLineConnectedDevices = connectedDevicesList;

        return router;
    }

    public void parseCardInfo(String s, Router router) throws JSONException {
        Log.i("ninedau","-------iccid info ----->value = "+s);
        JSONObject obj = new JSONObject(s);
        List<SimBean> list = router.simList;
        if(list==null){
            list= new ArrayList<>();
        }
        list.add(parseSimBean(obj));
        router.simList = list;
    }

    private ConnectedDevice parseConnectedDevice(JSONObject obj) throws JSONException {
        ConnectedDevice device = new ConnectedDevice();
        device.hostName = obj.getString("hostname");
        device.ip = obj.getString("ip");
        device.mac = obj.getString("mac");
        return device;
    }

    private SimBean parseSimBean(JSONObject obj) {
        SimBean simBean = new SimBean();
        try {
            simBean.iccid = obj.getString("iccid");
            simBean.status = obj.getInt("status");
            simBean.usedFlowSize = obj.getDouble("usedFlowSize");
            simBean.msisdn = obj.getString("msisdn");
            simBean.totalFlowSize = obj.getDouble("totalFlowSize");
            simBean.currentPlanExpireTime = obj.getString("currentPlanExpireTime");
            simBean.currentPlanName = obj.getString("currentPlanName");
            simBean.currentPlanStartTime = obj.getString("currentPlanStartTime");
            simBean.currentPlanStartTime = obj.getString("currentPlanStartTime");
            simBean.isTargetCard = true;
        } catch (Exception e) {
            return simBean;
        }
        return simBean;
    }
}
