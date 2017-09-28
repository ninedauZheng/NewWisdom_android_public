package com.app.newwisdom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.app.newwisdom.R;
import com.app.newwisdom.model.entity.BaseFluxBean;
import com.app.newwisdom.model.entity.DayFluxBean;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.http.HttpClient;
import com.app.newwisdom.view.view.CommonTitleView;
import com.app.newwisdom.view.view.FluxFormView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/7/27.
 */

public class FlowDetailsActivity extends Activity implements CommonTitleCallback{

    private ViewPager vp;
    private FluxFormView fluxForm;
    private HttpClient client;
    private CommonTitleView ct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_detail);
        init();
    }

    private void init() {
        vp = (ViewPager)findViewById(R.id.vp);
        fluxForm = (FluxFormView)findViewById(R.id.flux_form);
        ct = (CommonTitleView)findViewById(R.id.common_title);
        ct.setCallback(this);
        test();

    }

    private void test(){
        List<BaseFluxBean>  testList = new ArrayList<>();

        for(int i = 0;i<100;i++){
            DayFluxBean bean  = new DayFluxBean((long) (100000*(Math.random())),System.currentTimeMillis()- i *24*60*60*1000);
            testList.add(bean);
        }
        fluxForm.setFluxList(testList);

    }

    @Override
    public void clickLeftView() {
        finish();
    }

    @Override
    public void clickRightView() {

    }
}
