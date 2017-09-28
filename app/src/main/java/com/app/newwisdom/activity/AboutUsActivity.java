package com.app.newwisdom.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.view.view.CommonTitleView;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */

public class AboutUsActivity extends Activity implements CommonTitleCallback {


    private CommonTitleView commonTitle;
    private TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        commonTitle = (CommonTitleView) findViewById(R.id.common_title);

        tvContent = (TextView) findViewById(R.id.tv_content);
        commonTitle.setCallback(this);
        getCodeName();
    }

    private void getCodeName() {
        try {
            String pkName = this.getPackageName();
            String versionName = getPackageManager().getPackageInfo(pkName, 0).versionName;

            tvContent.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void clickLeftView() {
        finish();
    }

    @Override
    public void clickRightView() {

    }
}
