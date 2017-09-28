package com.app.newwisdom.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.app.newwisdom.model.entity.AD;
import com.app.newwisdom.model.entity.Banner;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.model.parse.BannerParse;
import com.app.newwisdom.model.parse.RouterParse;
import com.app.newwisdom.util.AppUtils;

import org.json.JSONException;

import java.util.List;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */

public class DeviceManager {

    private static DeviceManager manager;
    private Router router;
    private List<Banner> bannerList;
    private Banner ad;
    private String IMEI;
    private boolean isConnectTrueRouter = false;
    private long mTimeGap = 0 ;

    private DeviceManager() {

    }

    public synchronized static DeviceManager getInstance() {
        if (manager == null) {
            manager = new DeviceManager();
        }
        return manager;
    }

    public void setRouter(Router bean) {
        if (bean != null) {
            this.router = bean;
        }
    }

    public void clearRouter() {
        this.router = null;
        this.IMEI = null;
        this.isConnectTrueRouter = false;
    }

    public Router getRouter() {
        return router;
    }

    public List<Banner> getBannerList() {
        if (bannerList != null) {
            return bannerList;
        }
        return null;
    }

    public void setBannerList(List<Banner> list) {
        if (list != null) {
            this.bannerList = list;
        }
    }

    public void setBanner(String bannerValue) throws JSONException {
        BannerParse parse = new BannerParse();
        bannerList = parse.parseBannerList(bannerValue);
    }


    public void setAD(String abValue) throws JSONException {
        BannerParse parse = new BannerParse();
        ad = parse.parseJson(abValue);
    }

    public void setIMEI(String value) {
        if (!TextUtils.isEmpty(value)) {
            this.IMEI = value;

        }
    }


    public void setCardInfo(String value, boolean isConnectTrueRouter) throws JSONException {
        this.router = new RouterParse().parseRoute(value, isConnectTrueRouter);
    }

    public void addCardInfo(String value)throws JSONException{
        new RouterParse().parseCardInfo(value ,this.router);
    }

    public boolean checkCard() {
        Log.i("ninedeu", "checkCard = " + (router == null) + ",isEmpty =" + (TextUtils.isEmpty(router.iccid)));
        if (router == null || TextUtils.isEmpty(router.iccid)) {
            return false;
        }
        return true;
    }




    public String getIMEI() {
        return IMEI;
    }

    public boolean isConnectTrueRouter() {
        return isConnectTrueRouter;
    }

    public void setConnectTrueRouter(boolean connectTrueRouter) {
        isConnectTrueRouter = connectTrueRouter;
    }

    public Banner getAd() {
        return ad;
    }

    public void setAd(AD ad) {
        this.ad = ad;
    }

    public boolean checkAd() {
        if (ad == null) {
            return false;
        } else if (TextUtils.isEmpty(ad.pic)) {
            return false;
        }
        return true;
    }

    public boolean checkBanner() {
        if (bannerList == null || bannerList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkRouterImei(Context context) {
        if (IMEI == null) {
            return false;
        }
        return true;
    }

    public boolean checkAllIMEI(Context context) {
        if (IMEI == null && TextUtils.isEmpty(AppUtils.getSPIMEI(context))) {
            return false;
        }
        return true;

    }


    public void saveImei(Context context) {
        if (!TextUtils.isEmpty(manager.getIMEI())&& manager.getIMEI()!=AppUtils.getSPIMEI(context)) {
            AppUtils.setSPIMEI(context, manager.getIMEI());
        }
    }

    public void setTimeGap(long timestamp) {
        long second = System.currentTimeMillis() / 1000;

        Log.i("timestamp","timestamp="+timestamp +" , second = "+second +",gap = "+( timestamp - second) );
        this.mTimeGap = timestamp - second;
    }

    public void setGapCut(int value){
        this.mTimeGap+=value;
    }

    public long getmTimeGap(){
        return mTimeGap;
    }
}
