package com.app.newwisdom.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.callback.OkHttpCallBack;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.manager.HttpManager;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.view.view.CommonTitleView;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ninedau_zheng on 2017/7/31.
 */

public class WIFISettingActivity extends BaseActivity implements View.OnClickListener,CommonTitleCallback {

    private EditText et_ssid;
    private EditText et_psw;
    private Button btn;
    private DeviceManager dm;
    private Router router;
    private String psw;
    private String ssid;
    private CommonTitleView ct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initView();
        initData();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void wlanChanges() {

    }

    private void initData() {
        dm = DeviceManager.getInstance();
        router = dm.getRouter();
        psw = router.key;
        ssid = router.ssid;
        if(ssid!= null){
            et_ssid.setText(ssid);
        }

        if(psw != null){
            et_psw.setText(psw);
        }
    }

    private void initView() {
        et_ssid = (EditText)findViewById(R.id.et_ssid);
        et_psw = (EditText)findViewById(R.id.et_psw);
        btn = (Button)findViewById(R.id.btn_true);
        btn.setOnClickListener(this);

        ct = (CommonTitleView)findViewById(R.id.ct);
        ct.setCallback(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_true:
                psw = et_psw.getText().toString().trim();
                ssid = et_ssid.getText().toString().trim();
                HttpManager manager = HttpManager.getInstance(this);
                if(!checkPSWLength(psw)){
                    Toast.makeText(WIFISettingActivity.this,getResources().getString(R.string.error_psw),Toast.LENGTH_LONG).show();
                    return;
                }
                if(!checkSSID(ssid)){
                    Toast.makeText(WIFISettingActivity.this,getResources().getString(R.string.error_ssid),Toast.LENGTH_LONG).show();
                    return;
                }
                manager.setWifiName(this, dm.getIMEI(), ssid, psw, new OkHttpCallBack() {
                    @Override
                    public void onFailure(Call call, IOException exception) {
                        handler.sendEmptyMessage(ERROR);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                       handler.sendEmptyMessage(SUCCESS);

                    }
                });
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ERROR:
                    Toast.makeText(WIFISettingActivity.this,getResources().getString(R.string.error_wlan),Toast.LENGTH_LONG).show();

                    break;
                case SUCCESS:
                    Toast.makeText(WIFISettingActivity.this,getResources().getString(R.string.operator_ok),Toast.LENGTH_LONG).show();
                    if(dm.getRouter()!=null){
                        dm.getRouter().key = psw;
                        dm.getRouter().ssid = ssid;
                    }
                    finish();
                    break;

            }
        }
    };

    static final int ERROR = 1;
    static final int SUCCESS = 2;

    private boolean checkPSWLength(String value){
        if(value.length()<8||value.length()>12){
            return false;
        }
        return true;
    }

    private boolean checkSSID(String value){
        String regex = "([a-zA-Z0-9]{3,10})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    @Override
    public void clickLeftView() {
        finish();
    }

    @Override
    public void clickRightView() {

    }
}
