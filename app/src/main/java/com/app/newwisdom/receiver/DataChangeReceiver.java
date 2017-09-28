package com.app.newwisdom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.app.newwisdom.manager.HttpManager;

import static com.app.newwisdom.receiver.BroadcastInfo.WLAN_CHANGE;

/**
 * Created by ninedau_zheng on 2017/8/3.
 */

public class DataChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"网络改变",Toast.LENGTH_LONG).show();
        HttpManager.getInstance(context).clearHttp();
        Intent mIntent = new Intent(WLAN_CHANGE);
        context.sendBroadcast(mIntent);
    }
}
