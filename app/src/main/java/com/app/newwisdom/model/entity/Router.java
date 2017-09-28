package com.app.newwisdom.model.entity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/7/30.
 */

public class Router {

    //是否充电
    public boolean batteryCharging = false;
    //电量百分比
    public int batteryVolPercent = 0;
    //卡状态
    public int cardStatus = 5;
    //使用总流量
    public double cardUsed =0f;
    //本次设备连接连接时间
    public String connectAt  ;
//    设备开启时间
    public String createAt;
//    实时下载速度
    public double download ;
//    是否限速
    public boolean enableLimit;
    //固件版本👌
    public String firmwareVersion;
    //硬件版本
    public String hardwareVersion;
    //卡的iccid:
    public String iccid;

    public long id;
    //卡的imei 主key
    public String imei;
    //卡2的imei
    public String imei2;
    //sim卡的识别码
    public String imsi;
    //是否在线
    public boolean isOnline;
    //路由密码
    public String key;
    //路由ip
    public String lanIP;
    //路由mac
    public String lanMac ;
    //当前连接数量
    public int maxConnections;
    //网络运营商
    public String networkProvider;
    //网络制式
    public String networkType;
    //连接设备列表
    public List<ConnectedDevice> onLineConnectedDevices;
    //当前限制网速kb= KB/8
    public double rate;
    //信号强度 共5格
    public int signalLevel;
    //sim卡信息的列表
    public List<SimBean> simList;
    //WiFi名称
    public String ssid;
    //如果通过wifi连接的设备，设备的mac地址以;分割
    public String stationMac;
    //路由连接状态
    public String status ;
    //固件上次更新时间
    public String updatedTime;
    //上传速度
    public double upload;

    public String wan_ip;



    public String getWifiInfo(Context c) {
        TelephonyManager tp = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);

        WifiManager wifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        WifiInfo in = wifi.getConnectionInfo();
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();

        return "mac：" + maxText + "\n\r"
                + "ip：" + ipText + "\n\r"
                + "wifi status :" + status + "\n\r"
                + "ssid :" + ssid + "\n\r"
                + "net work id :" + networkID + "\n\r"
                + "connection speed:" + speed + "\n\r"
                ;
    }

    public String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }

    public List<ConnectedDevice> getConnectedDevicesByStationMac(){
        if(stationMac==null||stationMac.equals(""))return null;
        String[] stationMacList = stationMac.split(";");
        List<ConnectedDevice> result = new ArrayList<>() ;
        for(String mac :stationMacList){
            ConnectedDevice re = new ConnectedDevice();
            re.mac = mac;
            for(ConnectedDevice device:onLineConnectedDevices){
                if(device.mac.equalsIgnoreCase(mac)){
                    re.ip = device.ip;
                    re.hostName = device.hostName;

                    break;
                }
            }
            result.add(re);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Router{" +
                "batteryCharging=" + batteryCharging +
                ", batteryVolPercent=" + batteryVolPercent +
                ", cardStatus=" + cardStatus +
                ", cardUsed=" + cardUsed +
                ", connectAt='" + connectAt + '\'' +
                ", createAt='" + createAt + '\'' +
                ", download=" + download +
                ", enableLimit=" + enableLimit +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", hardwareVersion='" + hardwareVersion + '\'' +
                ", iccid='" + iccid + '\'' +
                ", id=" + id +
                ", imei='" + imei + '\'' +
                ", imei2='" + imei2 + '\'' +
                ", imsi='" + imsi + '\'' +
                ", isOnline=" + isOnline +
                ", key='" + key + '\'' +
                ", lanIP='" + lanIP + '\'' +
                ", lanMac='" + lanMac + '\'' +
                ", maxConnections=" + maxConnections +
                ", networkProvider='" + networkProvider + '\'' +
                ", networkType='" + networkType + '\'' +
                ", onLineConnectedDevices=" + onLineConnectedDevices +
                ", rate=" + rate +
                ", signalLevel=" + signalLevel +
                ", simList=" + simList +
                ", ssid='" + ssid + '\'' +
                ", stationMac='" + stationMac + '\'' +
                ", status='" + status + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", upload=" + upload +
                ", wan_ip='" + wan_ip + '\'' +
                '}';
    }
}
