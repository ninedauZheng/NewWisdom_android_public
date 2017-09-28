package com.app.newwisdom.callback;

/**
 * Created by ninedau_zheng on 2017/8/3.
 */

public interface DataUpdateCallback {

    public void connecteRouter(boolean succ);

    public void updateData(boolean succ, String string);

    public void updateAd(boolean succ);

    public void updateBanner(boolean succ);


}
