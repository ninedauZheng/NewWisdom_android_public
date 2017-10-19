package com.app.newwisdom.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.app.newwisdom.R;
import com.app.newwisdom.activity.BaseActivity;
import com.app.newwisdom.callback.DataUpdateCallback;
import com.app.newwisdom.callback.OkHttpCallBack;
import com.app.newwisdom.http.HttpClient;
import com.app.newwisdom.http.HttpConfig;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.model.parse.DeviceParse;
import com.app.newwisdom.util.AppUtils;
import com.app.newwisdom.util.MD5;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.app.newwisdom.http.HttpConfig.AD;
import static com.app.newwisdom.http.HttpConfig.BANNER;
import static com.app.newwisdom.http.HttpConfig.COMMON_CODE_SEND;
import static com.app.newwisdom.http.HttpConfig.COMMON_LOGIN_URL;
import static com.app.newwisdom.http.HttpConfig.COMMON_URL;
import static com.app.newwisdom.http.HttpConfig.INFO;
import static com.app.newwisdom.http.HttpConfig.RESTART;
import static com.app.newwisdom.http.HttpConfig.RESTORE;
import static com.app.newwisdom.http.HttpConfig.SET_SSID;
import static com.app.newwisdom.http.HttpConfig.SHUTDOWN;
import static com.app.newwisdom.http.HttpConfig.URL_INFO_BY_ICCID;
import static com.app.newwisdom.http.HttpConfig.VERIFY;

/**
 * Created by ninedau_zheng on 2017/7/30.
 */

public class HttpManager {

    private static HttpClient mHttpClient;
    private static DeviceManager mDeviceManager;
    private Context context;
    private static HttpManager mInstance;

    public static String ROUTER_INFO_URL = ".1:8023/v1/devices/";


    private HttpManager(Context context) {
        this.context = context;
    }

    public static synchronized HttpManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpManager(context);
            mHttpClient = HttpClient.getInstance(context);
            mDeviceManager = DeviceManager.getInstance();
        }
        return mInstance;
    }

    public void clearHttp() {
        ROUTER_INFO_URL = ".1:8023/v1/devices/";
    }

    private void getCardInfo(BaseActivity activity, boolean isRouter, String imei, OkHttpCallBack callback) {
        String url = "";
        Request request = null;
        if (isRouter) {
            request = mHttpClient.setParams4Get(ROUTER_INFO_URL + INFO + "?key=" + MD5.getVerificationCode());
        } else {
            request = mHttpClient.setParams4GetAndHead(COMMON_URL + HttpConfig.SHOW + "imei=" + imei);
        }
        mHttpClient.sendRequest(activity, request, callback);
    }

    private void getBannerImages(BaseActivity activity, OkHttpCallBack callback) {
        mHttpClient.sendRequest(activity, mHttpClient.setParams4GetAndHead(COMMON_URL + BANNER), callback);
    }

    private void getAD(BaseActivity activity, OkHttpCallBack callback) {
        mHttpClient.sendRequest(activity, mHttpClient.setParams4GetAndHead(COMMON_URL + AD), callback);
    }

    private void getIMEI(Context activity, OkHttpCallBack callBack) {
        String ip = AppUtils.getIp(activity);
        Log.i("ninedau", "ip == " + ip);
        if(ip == null){
            return;
        }
        String[] arr = ip.split("\\.");
        if (!ROUTER_INFO_URL.startsWith("http:")) {
            if ((null == arr || arr.length == 0)) {
                ROUTER_INFO_URL = "http://192.168.199" + ROUTER_INFO_URL;
            } else {
                ROUTER_INFO_URL = "http://192.168." + arr[2] + ROUTER_INFO_URL;
            }
        }
        Log.i("ninedau", "ipLocation = " + ROUTER_INFO_URL);

        if (activity instanceof BaseActivity) {
            mHttpClient.sendRequest((BaseActivity) activity, mHttpClient.setParams4Get(ROUTER_INFO_URL + VERIFY), callBack);
        } else {
            mHttpClient.sendRequest(null, mHttpClient.setParams4Get(ROUTER_INFO_URL + VERIFY), callBack);

        }
    }

    public void getRouterInfo(final Context context, final DataUpdateCallback callback) {
        Log.i("ninedau", "获取路由信息");
        if (context instanceof BaseActivity && !AppUtils.isNetworkAvailable(context)) {
            ((BaseActivity) context).showNoWlan();
            return;
        }
        getIMEI(context, new OkHttpCallBack() {
            @Override
            public void onFailure(Call call, IOException exception) {
                Log.i("ninedau", "IMEI 获取失败 onResponse error");
                mDeviceManager.setConnectTrueRouter(false);
                if (callback != null) {
                    callback.connecteRouter(false);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i("ninedau", "IMEI 获取成功 onResponse " + response.code());
                boolean result = false;
                if (response.code() == 200) {
                    try {
                        String value = response.body().string();
                        DeviceParse dp = new DeviceParse();
                        dp.parseJson(value);
                        if (!TextUtils.isEmpty(mDeviceManager.getIMEI())) {
                            mDeviceManager.setConnectTrueRouter(true);
                            result = true;
                        } else {
                            mDeviceManager.setConnectTrueRouter(false);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (callback != null) {
                        callback.connecteRouter(result);
                    }
                }
            }
        });
    }

    public void getAdInfo(final BaseActivity activity, final DataUpdateCallback callback) {
        if (!AppUtils.isNetworkAvailable(activity) && activity != null) {
            activity.showNoWlan();
            return;
        }
        getAD(activity, new OkHttpCallBack() {
            @Override
            public void onFailure(Call call, IOException exception) {
                if (callback != null)
                    callback.updateAd(false);
            }

            @Override
            public void onResponse(Call call, Response response) {
                boolean result = false;
                if (response.code() == 200) {
                    try {
                        String value = response.body().string();
                        mDeviceManager.setAD(value);
                        if (mDeviceManager.checkAd()) {
                            result = true;
                        }
                        Log.i("ninedau", "banner = " + value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (callback != null) {
                    callback.updateAd(result);
                }
                if (activity != null) {
                    activity.dismissDialog();
                }
            }
        });
    }


    public void getBannerInfo(final BaseActivity activity, final DataUpdateCallback callback) {
        if (!AppUtils.isNetworkAvailable(activity)) {
            activity.showNoWlan();
            return;
        }
        getBannerImages(activity, new OkHttpCallBack() {
            @Override
            public void onFailure(Call call, IOException exception) {
                Log.i("ninedau", "onFailure 333");
                if (activity != null) {
                    activity.dismissDialog();
                }
                if (callback != null) {
                    callback.updateBanner(false);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i("ninedau", "onResponse" + response.code());
                boolean result = false;
                if (response.code() == 200) {
                    try {
                        String value = response.body().string();
                        DeviceManager manager = DeviceManager.getInstance();
                        manager.setBanner(value);
                        Log.i("ninedau", "banner = " + value);
                        if (manager.checkBanner()) {
                            result = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (callback != null) {
                    callback.updateBanner(result);
                }
                if (activity != null) {
                    activity.dismissDialog();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final DataUpdateCallback callback = (DataUpdateCallback) msg.obj;
            Log.i("ninedau", "回调 -------- callbacke = " + callback);
            getCardInfoByIccid(null, mDeviceManager.getRouter().iccid, new OkHttpCallBack() {
                @Override
                public void onFailure(Call call, IOException exception) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        String value = response.body().string();
                        Log.i("ninedau", "onResponse value " + value);
                        boolean result = false;
                        String text = null;
                        try {
                            mDeviceManager.addCardInfo(value);
                            result = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            result = false;
                            text = "解析数据错误";
                        }
                        if (callback != null) {
                            callback.updateData(result, text);
                        }
                    }
                }
            });
        }
    };

    int requestTimes = 1;

    public void getInfoData(final BaseActivity activity, final String targetIMEI, final DataUpdateCallback callback) {
        if (!AppUtils.isNetworkAvailable(activity)) {
            activity.showNoWlan();
            return;
        }
        String imei = (targetIMEI == null ? AppUtils.getSPIMEI(context) : targetIMEI);
        final boolean isTrueRouter = mDeviceManager.isConnectTrueRouter();
        getCardInfo(activity, isTrueRouter, imei, new OkHttpCallBack() {

            @Override
            public void onFailure(Call call, IOException exception) {
                if (activity != null) {
                    activity.dismissDialog();
                }
                if (callback != null) {
                    callback.updateData(false, context.getResources().getString(R.string.error_wlan));
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                boolean result = false;
                String value = null;
                Log.i("ninedau", "数据详情 获取" + response.code());
                if (response.code() == 400 && isTrueRouter && requestTimes <= 3) {
                    Log.i("timestamp", "value=" + (int) (Math.pow(-1, requestTimes) * 30 * requestTimes));
                    mDeviceManager.setGapCut((int) (Math.pow(-1, requestTimes) * 30 * requestTimes));
                    requestTimes++;
                    getInfoData(activity, targetIMEI, callback);
                    return;
                }
                if (requestTimes == 4) {
                    requestTimes = 1;
                }
                if (response.code() == 200) {
                    try {
                        requestTimes = 1;
                        value = response.body().string();
                        Log.i("ninedau", "数据详情获取值 value = " + value);
                        DeviceManager manager = DeviceManager.getInstance();
                        Router router = null;
                        if (!TextUtils.isEmpty(targetIMEI)) {
                            manager.setCardInfo(value, isTrueRouter);
                            if (!TextUtils.isEmpty(manager.getRouter().iccid) && isTrueRouter) {
                                Message msg = new Message();
                                msg.obj = callback;
                                handler.sendMessage(msg);
                                return;
                            }
                            Log.i("ninedau", "获取用户信息" + manager.getRouter().toString());
                            if (manager.checkCard()) {
                                result = true;
                            }
                        } else {
                            if (!TextUtils.isEmpty(value)) {
                                result = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (callback != null) {
                    callback.updateData(result, value);
                }
                if (activity != null) {
                    activity.dismissDialog();
                }
            }
        });
    }


    public void getInfoData(final BaseActivity activity, final DataUpdateCallback callback) {
        getInfoData(activity, AppUtils.getSPIMEI(context), callback);
    }


    public void resetDevice(BaseActivity activity, String imei, OkHttpCallBack callback) {
        if (mDeviceManager.isConnectTrueRouter()) {
            resetLocalDevice(activity, callback);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("imei", imei);
        Request request = mHttpClient.setParams4PostAndHead(COMMON_URL + RESTORE, map);
        mHttpClient.sendRequest(activity, request, callback);
    }

    public void resetLocalDevice(BaseActivity activity, OkHttpCallBack callBack) {
        Request request = mHttpClient.setParams4Post(ROUTER_INFO_URL + RESTORE + "?key=" + MD5.getVerificationCode(), null);
        mHttpClient.sendRequest(activity, request, callBack);
    }

    public void shutDownDevice(BaseActivity activity, String imei, OkHttpCallBack callback) {
        if (mDeviceManager.isConnectTrueRouter()) {
            shutDownLocalDevice(activity, callback);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("imei", imei);
        Request request = mHttpClient.setParams4Post(COMMON_URL + SHUTDOWN, map);
        mHttpClient.sendRequest(activity, request, callback);
    }

    public void shutDownLocalDevice(BaseActivity activity, OkHttpCallBack callBack) {
        Request request = mHttpClient.setParams4Post(ROUTER_INFO_URL + SHUTDOWN + "?key=" + MD5.getVerificationCode(), null);
        mHttpClient.sendRequest(activity, request, callBack);
    }

    public void restartDevice(BaseActivity activity, String imei, OkHttpCallBack callBack) {
        if (mDeviceManager.isConnectTrueRouter()) {
            restartLocalDevice(activity, callBack);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("imei", imei);
        Request request = mHttpClient.setParams4PostAndHead(COMMON_URL + RESTART, map);
        mHttpClient.sendRequest(activity, request, callBack);
    }

    public void restartLocalDevice(BaseActivity activity, OkHttpCallBack callBack) {
        Request request = mHttpClient.setParams4Post(ROUTER_INFO_URL + RESTART + "?key=" + MD5.getVerificationCode(), null);
        mHttpClient.sendRequest(activity, request, callBack);
    }


    public void getCardInfoByIccid(BaseActivity activity, String iccid, OkHttpCallBack callback) {
        Request request = mHttpClient.setParams4GetAndHead(URL_INFO_BY_ICCID + iccid);
        mHttpClient.sendRequest(activity, request, callback);
    }

    public void setWifiName(BaseActivity activity, String imei, String name, String psw, OkHttpCallBack callBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("imei", imei);
        map.put("ssid", name);
        map.put("password", psw);
        Request request = mHttpClient.setParams4PostAndHead(COMMON_URL + SET_SSID, map );
        mHttpClient.sendRequest(activity, request, callBack);
    }

    public void sendCode(BaseActivity activity, String phoneNUm, OkHttpCallBack callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phoneNUm);

        Request request = mHttpClient.setParams4PostAndHead(COMMON_CODE_SEND, map);

        mHttpClient.sendRequest(activity, request, callback);
    }

    public void login(BaseActivity activity, String phoneNum, String code, OkHttpCallBack callBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phoneNum);
        map.put("code", code);
        Request request = mHttpClient.setParams4PostAndHead(COMMON_LOGIN_URL, map );
        mHttpClient.sendRequest(activity, request, callBack);
    }



}
