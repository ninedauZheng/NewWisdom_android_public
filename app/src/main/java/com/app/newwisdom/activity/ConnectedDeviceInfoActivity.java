package com.app.newwisdom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.ConnectedDevice;
import com.app.newwisdom.view.view.CommonTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/8/9.
 */

public class ConnectedDeviceInfoActivity extends BaseActivity implements CommonTitleCallback {

    private CommonTitleView ct;
    private ListView lv;
    private MyDeviceConnectedInfoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);
        initView();
    }

    private void initView() {
        ct = (CommonTitleView) findViewById(R.id.common_title);
        ct.setCallback(this);
        lv = (ListView) findViewById(R.id.lv);
        List list = getDeviceList();
        mAdapter = new MyDeviceConnectedInfoAdapter(list,ConnectedDeviceInfoActivity.this,0);
        lv.setAdapter(mAdapter);
    }

    private List getDeviceList(){
        DeviceManager dm = DeviceManager.getInstance();
        List<ConnectedDevice> devicesList = dm.getRouter().getConnectedDevicesByStationMac();
        if(devicesList==null){
            devicesList = new ArrayList<>();
        }
        return devicesList;
    }


    class MyDeviceConnectedInfoAdapter extends ArrayAdapter<ConnectedDevice> {

        private List<ConnectedDevice> mList;

        public MyDeviceConnectedInfoAdapter(List<ConnectedDevice> list, @NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Nullable
        @Override
        public ConnectedDevice getItem(int position) {
            return mList.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_device_connected, null);
                holder.tvMac = (TextView)convertView.findViewById(R.id.tv_mac);
                holder.tvIP = (TextView)convertView.findViewById(R.id.tv_ip);
                holder.tvDevice = (TextView)convertView.findViewById(R.id.tv_name);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            ConnectedDevice item = getItem(position);
            if(item.mac!=null){
                holder.tvMac.setText(item.mac);
                holder.tvIP.setText(TextUtils.isEmpty(item.ip)?getResources().getString(R.string.no_know):item.ip);
                holder.tvDevice.setText(TextUtils.isEmpty(item.hostName)?getResources().getString(R.string.no_know):item.hostName);
            }
            convertView.setTag(holder);
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvMac;
        TextView tvIP;
        TextView tvDevice;
    }

    @Override
    public void updateData() {

    }

    @Override
    public void wlanChanges() {

    }

    @Override
    public void clickLeftView() {
        finish();
    }

    @Override
    public void clickRightView() {

    }
}
