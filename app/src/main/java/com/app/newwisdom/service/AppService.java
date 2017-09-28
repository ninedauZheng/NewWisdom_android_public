package com.app.newwisdom.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.app.newwisdom.activity.BaseActivity;
import com.app.newwisdom.callback.DataUpdateCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.manager.HttpManager;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */

public class AppService extends Service implements DataUpdateCallback{

    IBinder appBinder = new AppBinder();


    private HttpManager httpManager;
    private DeviceManager dm;

    @Override
    public void connecteRouter(boolean succ) {
        if(succ){
            dm.saveImei(this);
            httpManager.getInfoData(null, null);
        }
    }

    @Override
    public void updateData(boolean succ, String string) {

    }

    @Override
    public void updateAd(boolean succ) {

    }

    @Override
    public void updateBanner(boolean succ) {

    }

    public class AppBinder extends Binder {
        public AppService getService() {
            return AppService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dm = DeviceManager.getInstance();
        httpManager = HttpManager.getInstance(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return appBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getRouterData(getApplicationContext(),this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //获取设备详情

    public void getRouterData(Context context, DataUpdateCallback callback){
        httpManager.getRouterInfo(context,callback);
    }

    public void getAllData(BaseActivity activity,DataUpdateCallback callback ){
        httpManager.getInfoData(activity, callback);
    }
}
