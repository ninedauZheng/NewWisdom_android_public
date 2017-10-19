package com.app.newwisdom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.LoginItemCallback;
import com.app.newwisdom.callback.OkHttpCallBack;
import com.app.newwisdom.manager.HttpManager;
import com.app.newwisdom.model.entity.User;
import com.app.newwisdom.util.AppUtils;
import com.app.newwisdom.view.viewGourp.LoginItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ninedau_zheng on 2017/8/28.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private LoginItemView loginView;
    private LoginItemView codeView;
    private Button btnGetCode;
    private Button btnLogin;
    private HttpManager mHttpManager;
    private final int ERROR_WLAN = 1;
    private final int CODE_SEND_SUSS = 2;
    private final int SERVICE_DEFINED = 3;
    private final int LOGIN_SUSS = 4;
    private final int UNKNOW_ERROR = 5;
    private String phone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void wlanChanges() {

    }

    private void initView() {
        loginView = (LoginItemView) findViewById(R.id.item_view_login);
        codeView = (LoginItemView) findViewById(R.id.item_view_code);
        btnGetCode = (Button) findViewById(R.id.btn_get_verify_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        mHttpManager = HttpManager.getInstance(LoginActivity.this);
        loginView.setCallback(new LoginItemCallback() {
            @Override
            public void animationFinish() {
                btnLogin.setVisibility(View.VISIBLE);
            }
        });
        codeView.setCallback(new LoginItemCallback() {
            @Override
            public void animationFinish() {
                btnGetCode.setVisibility(View.VISIBLE);
            }

        });

        btnGetCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_get_verify_code:
                getVerifyNum();
                break;

        }
    }

    public void Login() {
        phone = loginView.getEditText();
        String code = codeView.getEditText();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入正确的手机号和验证码", Toast.LENGTH_LONG).show();
            return;
        }
        if (!AppUtils.isMobile(phone)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
            return;
        }
        mHttpManager.login(this, phone, code, new OkHttpCallBack() {
            @Override
            public void onFailure(Call call, IOException exception) {
                handler.sendEmptyMessage(ERROR_WLAN);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String value = response.body().string();
                    Message msg = new Message();
                    msg.obj = value;
                    msg.what = LOGIN_SUSS;
                    handler.sendMessage(msg);
                } else if (response.code() == 400) {
                    String value = response.body().string();
                    Message msg = new Message();
                    msg.obj = value;
                    msg.what = SERVICE_DEFINED;
                    handler.sendMessage(msg);
                } else {
                    if(response.code() == 500){
                        Message msg = new Message();
                        msg.obj = "\"code\":\"500\",\"message\":\"服务器异常\"";
                        msg.what = SERVICE_DEFINED;
                        handler.sendMessage(msg);
                    }else{
                        handler.sendEmptyMessage(UNKNOW_ERROR);
                    }
                }
            }
        });
    }

    public void getVerifyNum() {
        String phone = loginView.getEditText();
        Log.i("ninedau", "phone = " + phone);
        if (!AppUtils.isMobile(phone)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
            return;
        }
        mHttpManager.sendCode(this, phone, new OkHttpCallBack() {
            @Override
            public void onFailure(Call call, IOException exception) {
                handler.sendEmptyMessage(ERROR_WLAN);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    handler.sendEmptyMessage(CODE_SEND_SUSS);
                } else if (response.code() == 400) {

                    String value = response.body().string();
                    Message msg = new Message();
                    msg.obj = value;
                    msg.what = SERVICE_DEFINED;
                    handler.sendMessage(msg);
                } else {
                    handler.sendEmptyMessage(SERVICE_DEFINED);
                }
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            switch (what) {
                case ERROR_WLAN:
                    showNoWlan();
                    break;
                case CODE_SEND_SUSS:
                    Toast.makeText(LoginActivity.this, "验证码发送成功，请尽快登录...", Toast.LENGTH_LONG).show();
                    break;
                case SERVICE_DEFINED:
                    int error = 0;
                    String text = null;
                    try {
                            String value = (String) msg.obj;
                        if (value.length() == 0) {
                            throw new NullPointerException("the value's length was 0.");
                        } else {
                            JSONObject obj = new JSONObject(value);
                            error = obj.getInt("code");
                            text = obj.getString("message");
                        }
                    } catch (JSONException e) {
                        error = 999999;
                        text = "未可知错误";
                    } catch (NullPointerException e) {
                        error = 000000;
                        text = "空指针错误";
                    }
                    Toast.makeText(LoginActivity.this, "错误码:" + error + "," + text, Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_SUSS:
                    String loginValue = (String) msg.obj;
                    User user = new User();
                    Log.i("ninedau", "value = " + loginValue);

                    try {
                        JSONObject obj = new JSONObject(loginValue);
                        user.setExpiredTime(obj.getString("expired_at"));
                        user.setToken(obj.getString("token"));
                        user.setUserId(obj.getInt("user_id"));
                        user.setUserIP(obj.getString("user_ip"));
                        user.setPhoneNum(phone);
                        AppUtils.setObjectToShare(LoginActivity.this, user, "user_info");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } catch (JSONException e) {
                    }
                    break;
            }
        }
    };


}
