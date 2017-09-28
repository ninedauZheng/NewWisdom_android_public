package com.app.newwisdom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.DataUpdateCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.Banner;
import com.app.newwisdom.util.ImageLoaderUtils;

import static com.app.newwisdom.R.id.tv;
import static com.app.newwisdom.activity.MainActivity.WEB_URL;

/**
 * Created by ninedau_zheng on 2017/8/15.
 */

public class ADActivity extends BaseActivity implements DataUpdateCallback, View.OnClickListener {


    private TextView tv_delay;
    private int DELAY = 8;
    private String value;
    private ImageView imgAD;
    private ImageLoaderUtils utils;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DELAY--;
            setTV();
            if (DELAY > 0) {
                handler.sendEmptyMessageDelayed(0, 1000);
            } else if (DELAY == 0) {
                finish();
            }
        }
    };
    private Banner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        init();
        initImageView();
    }

    private void init() {
        tv_delay = (TextView) findViewById(tv);
        imgAD = (ImageView) findViewById(R.id.img_ad);
        value = getResources().getString(R.string.pass);
        utils = ImageLoaderUtils.getInstance(ADActivity.this);
        imgAD.setOnClickListener(this);
        tv_delay.setOnClickListener(this);
    }

    private void initImageView() {
        banner = DeviceManager.getInstance().getAd();
        utils.display(imgAD, DeviceManager.getInstance().getAd().pic, null);
        setTV();
        handler.sendEmptyMessageDelayed(0, 1000);
    }


    private void setTV() {
        tv_delay.setText(value + " " + DELAY);
    }

    @Override
    public void updateData() {

    }

    @Override
    public void wlanChanges() {

    }

    @Override
    public void connecteRouter(boolean succ) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_ad:
                Intent intent = new Intent(ADActivity.this, WebViewActivity.class);
                intent.putExtra(WEB_URL,banner);
                startActivity(intent);
                finish();
                break;
            case R.id.tv:
                finish();
                break;
        }


    }
}
