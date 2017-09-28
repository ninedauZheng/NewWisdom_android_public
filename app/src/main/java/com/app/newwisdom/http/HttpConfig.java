package com.app.newwisdom.http;

/**
 * Created by ninedau_zheng on 2017/7/30.
 */

public class HttpConfig {


    //测试url
    public static final String COMMON_URL = "http://www.dmifi.com/v1/devices/";

    public static final String URL_INFO_BY_ICCID = "http://www.dmifi.com/v1/cards/show?iccid=";

//    public static final String ROUTER_INFO_URL = ".1:8023/v1/devices/";

    public static final String COMMON_CODE_SEND  = "http://www.dmifi.com/v1/messages/send";

    public static final String COMMON_LOGIN_URL= "http://www.dmifi.com/v1/users/login";

    //获取本地路由信息
    public static final String INFO = "info";
    //获取本地路由IMEI
    public static final String VERIFY = "verify";
    //显示卡详情
    public static final String SHOW = "show?";
    //获取banners
    public static final String BANNER = "banners";
    //获取广告信息
    public static final String AD = "ad";
    //恢复出厂设置  imei
    public static final String RESTORE = "restore";
    //关机 设备
    public static final String SHUTDOWN = "shutdown";
    //重启 设备
    public static final String RESTART = "restart";
    //设置SSID imei ssid password
    public static final String SET_SSID = "set_ssid";

//    public static final String IMEI = "861179030343495";


    /*App测试反馈170807:
1.连接用户数更新慢，mifi断开后，没有用户已连接用户的时候，app显示出来的还是1个用户连接。
2.没有设备IMEI号查询
3.没有信号强度
4.“可用流量“改为 “已用流量”数值
5.电量改图标，现在的一直是充电状态，会误导用户
6.程序没有退出键，点刷新或其它图标，App会异常退出
7.没有设备工作时间显示，mifi屏幕上有该数值
8.web管理后台：没有限速设置界面；新增代理商后，没有显示出来；更换广告图片，开机图片，app不起效。*/

}
