package com.app.newwisdom.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.newwisdom.R;

/**
 * Created by ninedau_zheng on 2017/7/19.
 */

public class DetailView extends RelativeLayout {
    private boolean isBattery = false;
    private String mText;
    private int mImgRes;
    private String mTitle;
    private ImageView img;
    private TextView tvTitle;
    private TextView tvContent;
    private BatteryView batteryView;
    private SignView signView;
    private Type mType = Type.BATTERY;
    enum Type{
        BATTERY,FLUX,DEVICE
    }


    public DetailView(Context context) {
        this(context, null);
    }

    public DetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.detailView, defStyleAttr, 0);
        mTitle = a.getString(R.styleable.detailView_title);
        mImgRes = a.getResourceId(R.styleable.detailView_img_resource, 0);
        mText = a.getString(R.styleable.detailView_context);
        int style = a.getInt(R.styleable.detailView_style, 0);
        a.recycle();
        initView(style);
    }

    private void initView(int style) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_detail, this);
        img = (ImageView) findViewById(R.id.img_detail);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        batteryView = (BatteryView) findViewById(R.id.batteryView);
        signView = (SignView) findViewById(R.id.signView);
        switch (style){
            case 0:
                mType =  Type.BATTERY;
                batteryView.setVisibility(View.VISIBLE);

                break;
            case 1:
                mType = Type.FLUX;
                signView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mType = Type.DEVICE;
                img.setVisibility(View.VISIBLE);
                img.setImageResource(mImgRes);
                break;
        }
        tvContent.setText(mText);
        tvTitle.setText(mTitle);
    }


    public void setValue(String value) {
        if (value != null) {
            tvTitle.setText(value);
        } else {
            tvTitle.setText(getResources().getString(R.string.getting));
        }
    }

    public String getValue(){
        String title = tvTitle.getText().toString();
        return title;
    }

    public void setBattery(int value) {
        if (batteryView != null) {
            batteryView.setValue(value);
        }
    }

    public void setNumSign(int value){
        if(signView!=null){
            signView.setNumSign(value);
        }
    }

    public void setOnclickListener(OnClickListener listener) {
        this.setOnClickListener(listener);
    }


}
