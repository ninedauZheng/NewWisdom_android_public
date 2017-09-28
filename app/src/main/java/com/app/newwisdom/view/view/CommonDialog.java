package com.app.newwisdom.view.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.OnDialogButtonClickCallback;

/**
 * Created by ninedau_zheng on 2017/7/31.
 */

public class CommonDialog extends Dialog implements View.OnClickListener {

    private Button btnOk;
    private TextView tvTitle;
    private TextView tvContent;
    private Button btnCancel;
    private String title;
    private String content;
    private String positiveText;
    private String negativeText;
    private OnDialogButtonClickCallback callback;
    private ImageView imgCannel;

    public CommonDialog(@NonNull Context context) {
        super(context);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CommonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    public CommonDialog setCommonNotice(String value) {
        setTitle(getContext().getResources().getString(R.string.tips)).setContentValue(value)
                .setPositiveValue(getContext().getResources().getString(R.string.ok));
        return this;
    }

    public CommonDialog setCommonTips(String value) {
        setTitle(getContext().getResources().getString(R.string.tips)).setContentValue(value)
                .setPositiveValue(getContext().getResources().getString(R.string.ok))
                .setNegativeValue(getContext().getResources().getString(R.string.cancel));
        return this;
    }

    public CommonDialog setCommonTips(String content, String positiveText, String negativeText) {
        setTitle(getContext().getResources().getString(R.string.tips)).setContentValue(content).setPositiveValue(positiveText).setNegativeValue(negativeText);
        return this;
    }

    public CommonDialog setParams(String title, String content) {
        setContentValue(content).setTitle(title);
        return this;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog setContentValue(String content) {
        this.content = content;
        return this;
    }

    public CommonDialog setPositiveValue(String ok) {
        this.positiveText = ok;
        return this;
    }

    public CommonDialog setCallback(OnDialogButtonClickCallback callback) {
        if (callback != null) {
            this.callback = callback;
        }
        return this;
    }

    public CommonDialog setNegativeValue(String negative) {
        this.negativeText = negative;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        imgCannel = (ImageView) findViewById(R.id.img_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        imgCannel.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }

        if (!TextUtils.isEmpty(positiveText)) {
            btnOk.setText(positiveText);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            btnCancel.setText(negativeText);
        } else {
            btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (callback == null) return;
        switch (v.getId()) {
            case R.id.img_cancel:
            case R.id.btn_cancel:
                callback.onDialogCallback(false);
                break;
            case R.id.btn_ok:
                callback.onDialogCallback(true);
                break;

        }
    }



}
