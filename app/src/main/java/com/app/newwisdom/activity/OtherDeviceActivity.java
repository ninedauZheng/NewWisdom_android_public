package com.app.newwisdom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.model.entity.SimBean;
import com.app.newwisdom.view.view.CommonTitleView;

import java.util.List;

/**
 * Created by ninedau_zheng on 2017/8/14.
 */

public class OtherDeviceActivity extends BaseActivity implements CommonTitleCallback{


    private LinearLayout ll;
    private LayoutInflater inflater;
    private CommonTitleView cv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_device);
        initView();
    }

    private void initView() {
        ll = (LinearLayout) findViewById(R.id.ll);
        cv = (CommonTitleView)findViewById(R.id.common_title);
        cv.setCallback(this);
//        Bundle bundle = getIntent().getExtras();
//        String value = bundle.getString(BUNDLE_KEY);
//        Router router = null;
//        try {
//            router = new RouterParse().parseRoute(value);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        addView(null);

    }

    private void addView(Router router) {
        if(router==null){
            router = DeviceManager.getInstance().getRouter();
        }

        ll.addView(getNewView("设备名称",router.ssid));
        ll.addView(getNewView("设备IMEI",router.imei));
        List<SimBean> simList = router.simList;
        if(simList!=null){
            for(int i = 0;i<simList.size();i++){
                SimBean bean  =simList.get(i);
                ll.addView(getNewView((i+1)+"号Sim卡ICCID",bean.iccid));
                ll.addView(getNewView((i+1)+"号Sim卡MSISDN",bean.msisdn));
            }
        }
//        ll.addView(getNewView("开机时间",router.createAt));

    }

    private View getNewView(String title,String value) {
        if (inflater == null) {
            inflater = LayoutInflater.from(OtherDeviceActivity.this);
        }
        View view = inflater.inflate(R.layout.item_search_child, null);
        ((TextView)view.findViewById(R.id.tv_title)).setText(title);
        if(value!=null){
            ((TextView)view.findViewById(R.id.tv_text)).setText(value);
        }
        return view;
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
