package com.app.newwisdom.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.DataUpdateCallback;
import com.app.newwisdom.callback.OnDialogButtonClickCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.manager.HttpManager;
import com.app.newwisdom.model.entity.User;
import com.app.newwisdom.service.AppService;
import com.app.newwisdom.util.AppUtils;
import com.app.newwisdom.view.view.CommonDialog;

/**
 * Created by ninedau_zheng on 2017/7/31.
 */

public class IndexActivity extends AppCompatActivity implements DataUpdateCallback {

    private ImageView imgLogo;
    private ObjectAnimator va;
    private boolean haveResult = false;
    private boolean animationResult = false;
    private boolean goLogin = true;
    private User user;
    private String valuePer;
    private String[] permissions = {"android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private boolean needPermisson = false;
    private CommonDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initData();
        checkPermissionForSDK_M();
    }

    private void checkPermissionForSDK_M() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission_phone_state = ContextCompat.checkSelfPermission(this, permissions[0]);
            int permission_storage = ContextCompat.checkSelfPermission(this, permissions[1]);
            if (permission_phone_state + permission_storage != 0) {
                showDialogTipUserRequestPermission(permission_phone_state, permission_storage);
            } else {
                initView();
            }
        }else{
            initView();
        }
    }

    private void showDialogTipUserRequestPermission(int permission_phone_state, int permission_storage) {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("权限不可用");
        dialog.setContentValue("由于需要使用存储权限和手机状态权限，为您存储设备信息与监听网络变化；否则您将无法正常使用");
        dialog.setPositiveValue("立即开启");
        dialog.setNegativeValue("残忍拒绝");
        dialog.setCallback(new OnDialogButtonClickCallback() {
            @Override
            public void onDialogCallback(boolean isPositive) {
                if (isPositive) startRequestPermission();
            }
        });
        dialog.show();
    }

    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
    }

    private void initData() {
        HttpManager manager = HttpManager.getInstance(IndexActivity.this);
        user = (User) AppUtils.getObjectFromShare(IndexActivity.this, "user_info");
        if (user != null) {
            if (user.getExpiredTime() - System.currentTimeMillis() > 0) {
                goLogin = false;
            }
        }
        manager.getAdInfo(null, this);
    }

    private void initView() {
        imgLogo = (ImageView) findViewById(R.id.img);
        startService(new Intent(IndexActivity.this, AppService.class));
        va = ObjectAnimator.ofFloat(imgLogo, "alpha", 0f, 1f);

        va.setDuration(3000);
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationResult = true;
                checkStartActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

    private void checkStartActivity() {
        if (animationResult && haveResult) {
            goActivity();
        }
    }


    public void showDialogTipUserGoToAppSetting() {
        dialog = new CommonDialog(this);
        dialog.setTitle("权限不可用");
        dialog.setCommonNotice("请在-应用设置-权限中，允许新智汇使用存储和状态权限来获取和保存用户数据..").setCallback(new OnDialogButtonClickCallback() {
            @Override
            public void onDialogCallback(boolean isPositive) {
                if (isPositive) {
                    goToAppSetting();
                } else {
                    finish();
                }
            }
        });
        dialog.show();
    }

    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUES_SETTINGS);
    }

    private static final int REQUES_SETTINGS = 123;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 321;

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void goActivity() {
        if (goLogin) {
            startActivity(new Intent(IndexActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(IndexActivity.this, MainActivity.class));
        }

        if (DeviceManager.getInstance().getAd() != null && !TextUtils.isEmpty(DeviceManager.getInstance().getAd().pic)) {
            startActivity(new Intent(IndexActivity.this, ADActivity.class));
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUES_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                int i1 = ContextCompat.checkSelfPermission(this, permissions[1]);
                if (i + i1 != PackageManager.PERMISSION_GRANTED) {
                    showDialogTipUserGoToAppSetting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    initView();
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        boolean permission_state_b = shouldShowRequestPermissionRationale(permissions[0]);
                        boolean permission_store_b = shouldShowRequestPermissionRationale(permissions[1]);
                        if (!permission_state_b || !permission_store_b) {
                            showDialogTipUserGoToAppSetting();
                        } else {
                            finish();
                        }
                    } else {
                        initView();
                        Toast.makeText(IndexActivity.this, "授权成功", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void connecteRouter(boolean succ) {

    }

    @Override
    public void updateData(boolean succ, String string) {

    }

    @Override
    public void updateAd(boolean succ) {
        Log.i("ninedau", "updateAd");
        haveResult = true;
        checkStartActivity();
    }

    @Override
    public void updateBanner(boolean succ) {

    }
}
