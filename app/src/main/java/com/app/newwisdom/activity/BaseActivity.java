package com.app.newwisdom.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.app.newwisdom.R;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.view.view.CommonDialog;
import com.app.newwisdom.view.view.LoadingDialog;

import static com.app.newwisdom.receiver.BroadcastInfo.DATA_CHANGE;
import static com.app.newwisdom.receiver.BroadcastInfo.WLAN_CHANGE;

/**
 * Created by ninedau_zheng on 2017/8/3.
 */

public abstract class BaseActivity extends Activity {


    private LoadingDialog dialog;
    private DataChangeBroadcastReciver mReciver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReciver = new DataChangeBroadcastReciver();
        registerReceiver(mReciver, new IntentFilter(DATA_CHANGE));
    }

    public void registerReceiver(IntentFilter filter) {
        registerReceiver(mReciver, filter);
    }

    class DataChangeBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            dismissDialog();
            if (intent.getAction().equals(DATA_CHANGE)) {
                updateData();
            } else if (intent.getAction().equals(WLAN_CHANGE)) {
                DeviceManager.getInstance().setConnectTrueRouter(false);
                wlanChanges();
            }

        }
    }

    public abstract void updateData();


    public abstract void wlanChanges();


    public void showLoading() {
        showLoading("正在加载中...");
    }

    public void showLoading(String value) {
        dialog = new LoadingDialog(this).setContentValue(value);
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciver);
    }

    public void showNoWlan(){
        CommonDialog dialog = new CommonDialog(this).setCommonNotice(getResources().getString(R.string.error_wlan));
        dialog.show();
    }
}


