package com.app.newwisdom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.util.AppUtils;
import com.app.newwisdom.view.view.CommonDialog;
import com.app.newwisdom.view.view.CommonTitleView;

/**
 * Created by ninedau_zheng on 2017/8/3.
 */

public class CardNameActivity extends Activity implements View.OnClickListener ,CommonTitleCallback{

    private EditText etIccid;
    private Button btnOk;
    private Router router;
    private CommonTitleView ct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        etIccid = (EditText) findViewById(R.id.et_iccid);
        ct = (CommonTitleView)findViewById(R.id.common_title);
        btnOk = (Button) findViewById(R.id.btn_true);

        btnOk.setOnClickListener(this);
        ct.setCallback(this);
        DeviceManager dm = DeviceManager.getInstance();
        router = dm.getRouter();
        if (router != null) {
            String name = null;
            if (router.iccid != null && !router.iccid.equals("")) {
                name = AppUtils.getIccidName(this, router.iccid);
            }

            etIccid.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        String value = etIccid.getText().toString().trim();
        if (value == null) return;
        if (value.length() > 5) {
            new CommonDialog(this).setCommonNotice(getResources().getString(R.string.pls_5_size)).show();
            return;
        }
        AppUtils.setIccid(this, router.iccid, value);
        setResult(1);
        finish();
    }


    @Override
    public void clickLeftView() {
        finish();
    }

    @Override
    public void clickRightView() {

    }
}
