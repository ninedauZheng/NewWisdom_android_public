package com.app.newwisdom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.callback.DataUpdateCallback;
import com.app.newwisdom.manager.HttpManager;
import com.app.newwisdom.view.view.CommonTitleView;

/**
 * Created by ninedau_zheng on 2017/8/14.
 */

public class SearchDeviceActivity extends BaseActivity implements CommonTitleCallback, View.OnClickListener, DataUpdateCallback {

    private EditText etIMEI;
    private Button btnSearch;
    private HttpManager mHttpManager;
    private CommonTitleView cv;
    public static final String BUNDLE_KEY = "SEARCH_RESULT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

    }

    private void initView() {
        cv = (CommonTitleView)findViewById(R.id.common_title);
        etIMEI = (EditText) findViewById(R.id.ed_imei);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        cv.setCallback(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                String targetIMEI = etIMEI.getText().toString().trim();
                if(isImei(targetIMEI)){
                    if(mHttpManager == null){
                        mHttpManager = HttpManager.getInstance(SearchDeviceActivity.this);
                    }
                    mHttpManager.getInfoData(SearchDeviceActivity.this,targetIMEI,this);
                }
                break;
        }
    }

    private boolean isImei(String value){
        if(value!=null&&value.length()==15){
            return true;
        }
        Toast.makeText(SearchDeviceActivity.this,"IMEI输入有误，请重新输入",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void connecteRouter(boolean succ) {

    }

    @Override
    public void updateData(boolean succ, String string) {
        if(succ){
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_KEY,string);
            Intent intent = new Intent(SearchDeviceActivity.this,OtherDeviceActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void updateAd(boolean succ) {

    }

    @Override
    public void updateBanner(boolean succ) {

    }
}
