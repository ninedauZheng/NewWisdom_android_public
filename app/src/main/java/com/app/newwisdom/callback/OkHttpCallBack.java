package com.app.newwisdom.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ninedau_zheng on 2017/7/30.
 */

public interface OkHttpCallBack {

    public void onFailure(Call call, IOException exception);
    public void onResponse(Call call , Response response) throws IOException;
}
