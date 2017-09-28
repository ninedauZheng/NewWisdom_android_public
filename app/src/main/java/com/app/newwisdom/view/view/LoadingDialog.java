package com.app.newwisdom.view.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.OutTimeDialogCallback;

import static com.app.newwisdom.R.id.img;

/**
 * Created by ninedau_zheng on 2017/7/31.
 */

public class LoadingDialog extends Dialog {

    private String content;
    private ImageView imgLoad;
    private TextView tvLoadContent;
    private OutTimeDialogCallback callback;
    private int outTime = 3000;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    public LoadingDialog setContentValue(String content) {
        this.content = content;
        return this;
    }

    public LoadingDialog setOutTimeCallback(OutTimeDialogCallback callback) {
        if (callback != null) {
            this.callback = callback;
        }
        return this;
    }

    public LoadingDialog setOutTime(int outTime) {
        if (outTime >= 5000) {
            this.outTime = outTime;
        }
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        imgLoad = (ImageView) findViewById(img);

        tvLoadContent = (TextView) findViewById(R.id.tv_content);
        if (content != null) {
            tvLoadContent.setText(content);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable());
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(1000);
        imgLoad.setAnimation(ra);
        ra.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
                if (callback != null) {
                    callback.outTime();
                }
            }
        }, outTime);
    }


    private Handler handler = new Handler();


}
