package com.app.newwisdom.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.model.entity.Banner;
import com.app.newwisdom.view.view.CommonTitleView;

/**
 * Created by ninedau_zheng on 2017/8/14.
 */

public class WebViewActivity extends BaseActivity implements CommonTitleCallback {

    private WebView web;
    private int webPostion;
    private Banner banner;
    private CommonTitleView commonTitle;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        init();
        initWeb();
    }

    private void init() {
        web = (WebView)findViewById(R.id.web);
        pb = (ProgressBar) findViewById(R.id.pb);
        banner = (Banner)getIntent().getSerializableExtra(MainActivity.WEB_URL);
        commonTitle = (CommonTitleView)findViewById(R.id.common_title);
        commonTitle.setTitleValue(banner.title);
        commonTitle.setCallback(this);
    }

    private void initWeb(){
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.loadUrl(banner.link);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });


        web.setWebChromeClient(new WebChromeClient() {


            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.i("ninedau","标题在这里" + title);
//                mtitle.setText(title);
            }


            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i("ninedau","newProgress="+newProgress);
                if (newProgress < 100) {
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                } else if (newProgress == 100) {
                    pb.setProgress(newProgress);
                    handler.sendEmptyMessageAtTime(1,1000);
                }
            }
        });

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb.setVisibility(View.GONE);
        }
    };

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
