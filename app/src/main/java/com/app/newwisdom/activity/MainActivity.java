package com.app.newwisdom.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;
import com.app.newwisdom.callback.DataUpdateCallback;
import com.app.newwisdom.callback.OnDialogButtonClickCallback;
import com.app.newwisdom.callback.OnViewPagerClickCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.manager.HttpManager;
import com.app.newwisdom.model.entity.Banner;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.receiver.DataChangeReceiver;
import com.app.newwisdom.service.AppService;
import com.app.newwisdom.util.AppUtils;
import com.app.newwisdom.view.view.BottomView;
import com.app.newwisdom.view.view.CommonDialog;
import com.app.newwisdom.view.view.CommonTitleView;
import com.app.newwisdom.view.view.DetailView;
import com.app.newwisdom.view.viewGourp.CommonViewPager;
import com.app.newwisdom.view.viewGourp.FluxCircleViewGroup;

import static com.app.newwisdom.R.id.btn_name;
import static com.app.newwisdom.receiver.BroadcastInfo.WLAN_CHANGE;
import static com.app.newwisdom.util.AppUtils.checkConnected;

public class MainActivity extends BaseActivity implements View.OnClickListener, CommonTitleCallback, DataUpdateCallback, OnViewPagerClickCallback {

    private CommonViewPager cv;
    private AppService mService;
    private BottomView btnFlux;
    private BottomView btnName;
    private CommonTitleView commonTitle;
    private DeviceManager manager;
    private DetailView ct1;
    private DetailView ct3;
    private DetailView ct2;
    private FluxCircleViewGroup fcvg;
    private HttpManager mHttpManager;
    private CommonViewPager cvp;
    private static final int UPDATE_BANNER = 1;
    private static final int UPDATE_AD = 2;
    public static final String WEB_URL = "web_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBroadcast();
        bindService(new Intent(MainActivity.this, AppService.class), sc, BIND_AUTO_CREATE);
        initView();
        updateLocationData();
        manager.saveImei(this);
        initBanner();
    }


    private void initBroadcast() {
        registerReceiver(new IntentFilter(WLAN_CHANGE));
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            DataChangeReceiver d = new DataChangeReceiver();
            registerReceiver(d,filter);
        }
    }

    private void initBanner() {
        mHttpManager.getBannerInfo(this, this);
    }

    private void initView() {
        cvp = (CommonViewPager) findViewById(R.id.cvp);

        fcvg = (FluxCircleViewGroup) findViewById(R.id.fcvg);
        fcvg.addListener(this);

        btnFlux = (BottomView) findViewById(R.id.btn_flux);
        btnName = (BottomView) findViewById(btn_name);


        commonTitle = (CommonTitleView) findViewById(R.id.common_title);
        commonTitle.setCallback(this);
        commonTitle.setImgLeftVisible(false);
        btnFlux.setOnClickListener(this);
        btnName.setOnClickListener(this);

        if (manager == null) {
            manager = DeviceManager.getInstance();
            mHttpManager = HttpManager.getInstance(MainActivity.this);
        }
        cv = (CommonViewPager) findViewById(R.id.cvp);
//        if(manager.getBannerList()!=null&&manager.getBannerList().size()!=0){
//            cv.updateData(manager.getBannerList());
//        }
        cv.setOnPagerClickCallback(this);
        ct1 = (DetailView) findViewById(R.id.ct1);
        ct2 = (DetailView) findViewById(R.id.ct2);
        ct3 = (DetailView) findViewById(R.id.ct3);
        fcvg.setOnClickListener(this);
        if (!manager.isConnectTrueRouter()) {
            showNoIMEIDialog();
        }
        mHttpManager.getBannerInfo(this, this);
    }


    private void updateLocationData() {
        Log.i("ninedau", "updateLocationData" + manager.getRouter());
        Router router = manager.getRouter();
        if (router == null) {
            Log.i("ninedau", "value" + ct1.getValue());
            if (ct1.getValue().equals(getResources().getString(R.string.no_know))) {
                return;
            }
        }
        ct1.setValue(router == null ? getResources().getString(R.string.no_have) : router.batteryVolPercent + "%");
        ct1.setBattery(router == null ? -1 : router.batteryVolPercent);
        ct2.setValue(router == null ? getResources().getString(R.string.no_have) : AppUtils.getBDName(router.networkProvider, router.networkType));
        ct2.setNumSign(router == null ? 3 : router.signalLevel);
        ct3.setValue(router == null ? getResources().getString(R.string.no_have) : AppUtils.getDeviceNum(router.stationMac) + "台");
        ct3.setOnClickListener(this);
        fcvg.updateValue();
        if (router != null && !TextUtils.isEmpty(router.iccid)) {
            String name = AppUtils.getIccidName(this, router.iccid);
            if (name != null) {
                btnName.setTitle(name);
            }
        } else {
            btnName.setTitle("名称");
        }
    }

    @Override
    public void updateData() {
        Log.i("ninedau", "3.回调更新数据" + AppUtils.isNetworkAvailable(this));
        if (AppUtils.isNetworkAvailable(this)) {
            updateLocationData();
        }
    }

    @Override
    public void wlanChanges() {
        if (AppUtils.isNetworkAvailable(this)) {
            Log.i("ninedau", "1.回调网络变更");
            mHttpManager.getRouterInfo(this, this);
        }
    }

    private void showNoIMEIDialog() {

        Toast.makeText(MainActivity.this,getResources().getString(R.string.now_not_connect_correct_device),Toast.LENGTH_SHORT  ).show();
        checkHasIMEI();
//        final CommonDialog dialog = new CommonDialog(this).setCommonNotice(getResources().getString(R.string.now_not_connect_correct_device));
//        dialog.setCallback(new OnDialogButtonClickCallback() {
//            @Override
//            public void onDialogCallback(boolean isPositive) {
//                if (isPositive) {
//                    checkHasIMEI();
//                }
//            }
//
//        });
//        dialog.show();
    }

    private void checkHasIMEI() {
        final String imei = AppUtils.getSPIMEI(this);
        if (imei != null) {
            manager.setIMEI(imei);
            mHttpManager.getInfoData(MainActivity.this, MainActivity.this);
//            final CommonDialog dialog = new CommonDialog(this).setCommonTips(getResources().getString(R.string.has_imei), getResources().getString(R.string.ok), getResources().getString(R.string.cancel));
//            dialog.setCallback(new OnDialogButtonClickCallback() {
//                @Override
//                public void onDialogCallback(boolean isPositive) {
//                    if (isPositive) {
//                        manager.setIMEI(imei);
//                        mHttpManager.getInfoData(MainActivity.this, MainActivity.this);
//                    }
//                }
//
//            });
//            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_flux:
                if (!AppUtils.checkConnected()) {
                    showNOCard();
                } else{
                    startActivity(new Intent(MainActivity.this, OtherDeviceActivity.class));
                }
                break;
            case btn_name:
                if (AppUtils.checkConnected()) {
                    startActivityForResult(new Intent(MainActivity.this, CardNameActivity.class), 1);
                } else {
                    CommonDialog dialog = new CommonDialog(MainActivity.this).setCommonNotice(getResources().getString(R.string.no_card));
                    dialog.show();
                }
                break;
            case R.id.fcvg:
                if (manager.checkRouterImei(this)) {
                    mHttpManager.getInfoData(MainActivity.this, MainActivity.this);
                } else {
                    CommonDialog dialog = new CommonDialog(this);
                    dialog.setCommonNotice(this.getResources().getString(R.string.no_card)).setCallback(new OnDialogButtonClickCallback() {
                        @Override
                        public void onDialogCallback(boolean isPositive) {
                            if (isPositive) {
                                if (manager.checkAllIMEI(MainActivity.this)) {
                                    mHttpManager.getInfoData(MainActivity.this, MainActivity.this);
                                }
                            }
                        }


                    });
                    dialog.show();
                }
                break;

            case R.id.ct3:
                if (manager.getRouter() != null) {
                    startActivity(new Intent(MainActivity.this, ConnectedDeviceInfoActivity.class));
                }
        }
    }

    @Override
    public void clickLeftView() {


    }

    private void showNOCard(){
        Toast.makeText(MainActivity.this,getResources().getString(R.string.no_card),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void clickRightView() {
        if (checkConnected()) {
            startActivity(new Intent(MainActivity.this, DeviceActivity.class));
        } else {
            showNOCard();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
        dismissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!AppUtils.checkUseRouter()) return;
            if (!AppUtils.checkUseCard()) return;
            String cardName = AppUtils.getIccidName(this, DeviceManager.getInstance().getRouter().iccid);
            if (cardName != null) {
                btnName.setTitle(cardName);
            }
        }
    }

    public ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((AppService.AppBinder) service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void connecteRouter(boolean succ) {
        Log.i("ninedau", "2.connecteRouter 回调成功 true = " + succ+" ,imei ="+manager.getIMEI());
        if (!succ) {
            handler.sendEmptyMessage(3);
        } else {

            manager.saveImei(this);
            mService.getAllData(MainActivity.this, MainActivity.this);
        }
    }

    @Override
    public void updateData(final boolean succ, final String string) {
        Log.i("ninedau", "3.更新数据 true=" + succ);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (succ) {
                    updateLocationData();
                } else {
                    if (string != null) {
                        showError(string);
                    }
                }
            }
        });
    }

    @Override
    public void updateAd(boolean succ) {
        if (succ) {
            Message msg = new Message();
            msg.what = UPDATE_AD;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void updateBanner(boolean succ) {
        if (succ) {
            Message msg = new Message();
            msg.what = UPDATE_BANNER;
            handler.sendMessage(msg);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_AD:

                    break;
                case UPDATE_BANNER:
                    cvp.updateData(manager.getBannerList());
                    break;
                case 3:
                    showNoIMEIDialog();
                    break;
            }
        }
    };

    CommonDialog error;

    private void showError(String value) {
        if (error == null) {
            error = new CommonDialog(MainActivity.this);
        }
        if (error.isShowing()) return;
        error.setCommonNotice(value);
        error.show();
    }

    @Override
    public void onPagerItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        Banner banner = DeviceManager.getInstance().getBannerList().get(position);
        if(banner==null)return;
        intent.putExtra(WEB_URL, banner);
        startActivity(intent);
    }
}
