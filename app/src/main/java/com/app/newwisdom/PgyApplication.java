package com.app.newwisdom;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.bugly.Bugly;

public class PgyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
//        CrashReport.initCrashReport(getApplicationContext());
        Bugly.init(getApplicationContext(),"717801d77a",true);
        PgyCrashManager.register(this);
    }

}