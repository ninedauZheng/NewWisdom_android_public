package com.app.newwisdom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.newwisdom.R;
import com.app.newwisdom.adapter.DeviceAdapter;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.model.item.DeviceItem;
import com.app.newwisdom.view.view.CommonTitleView;

import java.util.ArrayList;

import static com.app.newwisdom.adapter.DeviceAdapter.COMMON_PARENT_VIEW;
import static com.app.newwisdom.adapter.DeviceAdapter.TITLE_VIEW_BIG;
import static com.app.newwisdom.adapter.DeviceAdapter.TITLE_VIEW_SMALL;

/**
 * Created by ninedau_zheng on 2017/7/24.
 */

public class DeviceActivity extends BaseActivity implements View.OnClickListener , CommonTitleCallback{

    private ArrayList<DeviceItem> list;
    private DeviceAdapter adapter;

    private CommonTitleView ct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        initView();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void wlanChanges() {

    }

    private void initView() {
        ct = (CommonTitleView)findViewById(R.id.common_title);
        ct.setCallback(this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(DeviceActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        list = new ArrayList<DeviceItem>();
        String[] res = getResources().getStringArray(R.array.setting_device);
        for (int i = 0; i < res.length; i++) {
            if (i == 0) {
                list.add(new DeviceItem(TITLE_VIEW_SMALL, res[i]));
            } else if (i == 6) {
                list.add(new DeviceItem(TITLE_VIEW_BIG, res[i]));
            } else {
                list.add(new DeviceItem(COMMON_PARENT_VIEW, res[i]));

            }
        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new DeviceAdapter(DeviceActivity.this,list);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void clickLeftView() {
        finish();
    }


    @Override
    public void clickRightView() {

    }





}
