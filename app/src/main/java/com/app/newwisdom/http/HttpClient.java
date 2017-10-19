package com.app.newwisdom.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.app.newwisdom.activity.BaseActivity;
import com.app.newwisdom.callback.OkHttpCallBack;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.model.entity.User;
import com.app.newwisdom.util.AppUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ninedau_zheng on 2017/7/30.
 */

public class HttpClient {

    public static OkHttpClient mClient;
    private Context mContext;
    private static HttpClient mInstance;

    protected HttpClient(Context context) {
        this.mContext = context;
        mClient = new OkHttpClient();
    }

    public static synchronized HttpClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpClient(context);
        }
        return mInstance;
    }

    private Map<String,String> getRequestHead(Context context){
        HashMap<String ,String> headMap = new HashMap<>();
        headMap.put("x-mifi-from","xinzhihui");
        DeviceManager manager = DeviceManager.getInstance();
        Router router = manager.getRouter();
        User user = (User) AppUtils.getObjectFromShare(context ,"user_info");
        headMap.put("Content-Type","application/json");
        headMap.put("x-mifi-token",((user==null|| TextUtils.isEmpty(user.getToken()))?"":user.getToken()));
        headMap.put("x-mifi-imei",((router==null||TextUtils.isEmpty(router.imei))?"":router.imei));
        headMap.put("x-mifi-iccid", ((router==null||TextUtils.isEmpty(router.iccid))?"":router.iccid));
        return headMap;
    }



    public void sendRequest(final BaseActivity activity, Request request, final OkHttpCallBack callback) {
        if (activity != null && activity instanceof BaseActivity) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !activity.isDestroyed()) {
                        activity.showLoading();
                    }
                }
            });
        }
        Call mCall = mClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(call, e);
                            activity.dismissDialog();
                        }
                    });
                } else {
                    callback.onFailure(call, e);
                }


            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (activity != null) {
                    activity.dismissDialog();
                    callback.onResponse(call, response);

                } else {
                    callback.onResponse(call, response);
                }
            }
        });
    }

    public void sendRequest(Request request, final OkHttpCallBack callback) {
        sendRequest(null, request, callback);
    }



    public Request setParams4Get(String url) {
        Log.i("ninedau", "get 请求地址= " + url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET", null);

        return requestBuilder.build();
    }

    public Request setParams4Post(String url, Map<String, String> map) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (map != null && map.size() != 0) {
            for (String key : map.keySet()) {
                formBody.add(key, map.get(key));
            }
        }
        return new Request.Builder().url(url).post(formBody.build()).build();
    }

    public Request setParams4PostAndHead(String url , Map<String ,String> map ){
        FormBody.Builder formBody = new FormBody.Builder();
        if (map != null && map.size() != 0) {
            for (String key : map.keySet()) {
                formBody.add(key, map.get(key));
            }
        }
        Request.Builder builder = new Request.Builder();
        HashMap<String , String> headMap = (HashMap<String, String>) getRequestHead(mContext);
        if(headMap!=null&&headMap.size()!=0){
            for(String key: headMap.keySet()){
                builder.header(key,headMap.get(key));
            }
        }
        return builder.url(url).post(formBody.build()).build();
    }

    public Request setParams4GetAndHead(String url){
        Request.Builder builder = new Request.Builder();
        HashMap<String , String> headMap = (HashMap<String ,String>)getRequestHead(mContext);
        if(headMap !=null&&headMap.size()!=0){
            for(String key: headMap.keySet()){
                builder.header(key,headMap.get(key));
            }
        }
        return builder.url(url).get().build();
    }
}
