package com.app.newwisdom.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.Router;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */

public class AppUtils {
    //判断网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {

        } else {
            if (cm.getActiveNetworkInfo() == null) {

            } else {
                cm.getActiveNetworkInfo().isAvailable();

                NetworkInfo[] info = cm.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    //判断wifi是否脸上
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return wifi == NetworkInfo.State.CONNECTING;
    }

    //判断是否为wifi连接
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWorkInfo = cm.getActiveNetworkInfo();
        return activeNetWorkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static int getDeviceNum(String value) {
        Log.i("ninedau", "value = " + value);
        if (TextUtils.isEmpty(value)) return 0;
        String[] results = value.split(";");
        return results.length;
    }

    private static String IMEI = "imei";
    private static String CARD_NAME = "card_name";
    private static String USER_INFO = "user_info";

    public static void setSPIMEI(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences(IMEI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(IMEI, value);
        editor.commit();
    }

    public static String getSPIMEI(Context context) {
        SharedPreferences sp = context.getSharedPreferences(IMEI, Context.MODE_PRIVATE);
        String imei = sp.getString(IMEI, null);
        return imei;
    }

    public static void removeSPIMEI(Context context) {
        SharedPreferences sp = context.getSharedPreferences(IMEI, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(IMEI);
        edit.commit();
    }

    public static void setIccid(Context context, String key, String name) {
        SharedPreferences sp = context.getSharedPreferences(CARD_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, name);
        editor.commit();
    }

    public static void removeIccid(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CARD_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getIccidName(Context context, String key) {
        if (key == null) return null;
        SharedPreferences sp = context.getSharedPreferences(CARD_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static boolean setObjectToShare(Context context, Object object,
                                           String key) {
        SharedPreferences share = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
// 将对象放到OutputStream中
// 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(),
                Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
// 将编码后的字符串写到base64.xml文件中
        Log.i("ninedau", "objectsTr=" + objectStr);
        editor.putString(key, objectStr);
        return editor.commit();
    }

    public static Object getObjectFromShare(Context context, String key) {
        SharedPreferences sharePre = PreferenceManager
                .getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
        // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkUseRouter() {
        DeviceManager dm = DeviceManager.getInstance();
        Router router = dm.getRouter();
        if (router != null) {
            return true;
        }
        return false;
    }

    public static boolean checkUseCard() {
        DeviceManager dm = DeviceManager.getInstance();
        Router router = dm.getRouter();
        if (router.iccid == null || router.equals("")) return false;
        return true;
    }

    public static boolean checkConnected() {
        if (!AppUtils.checkUseRouter()) {
            return false;
        } else if (!AppUtils.checkUseCard()) {
            return false;
        }
        return true;
    }

    public static String getBDName(String name, String engName) {
        String result = "";
        engName = engName.toUpperCase();

        if (name.contains("电信")) {
            result += "电信";
        } else if (name.contains("移动")) {
            result += "移动";
        } else if (name.contains("联通")) {
            result += "联通";
        } else {
            result += "未知";
        }
        if (engName.contains("GPRS") || engName.contains("EDGE") || engName.equals("CDMA") || engName.contains("RTT") || engName.contains("DEN") || engName.contains("GSM")) {
            result += "2G";
        } else if (engName.contains("UMTS") || engName.contains("REV") || (engName.contains("HS") && engName.contains("PA")) || engName.contains("EHRPD") || engName.contains("_SCDMA")) {
            result += "3G";
        } else if (engName.contains("LTE") || engName.contains("IWLAN")) {
            result += "4G";
        }
        return result;
    }

    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186 170
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8或7，其他位置的可以为0-9
    */
        String num = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    public static String getIp(final Context context) {
        String ip = null;
        try {
            ConnectivityManager conMan = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // mobile 3G Data Network
            android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();
            // wifi
            android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
            // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
            if (mobile == android.net.NetworkInfo.State.CONNECTED
                    || mobile == android.net.NetworkInfo.State.CONNECTING) {
                ip = getLocalIpAddress();
            }
            if (wifi == android.net.NetworkInfo.State.CONNECTED
                    || wifi == android.net.NetworkInfo.State.CONNECTING) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                ip = (ipAddress & 0xFF) + "." +
                        ((ipAddress >> 8) & 0xFF) + "." +
                        ((ipAddress >> 16) & 0xFF) + "." +
                        (ipAddress >> 24 & 0xFF);
            }
        } catch (Exception e) {

        }
        return ip;
    }


    private static String getLocalIpAddress() {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return null;
    }

}
