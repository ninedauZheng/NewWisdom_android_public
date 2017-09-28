package com.app.newwisdom.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.CommonTitleCallback;

/**
 * Created by ninedau_zheng on 2017/7/16.
 */

public class CommonTitleView extends RelativeLayout implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView btnRight;
    private ImageView btnLeft;

    private int noImg = 0;
    private int imgLeftRes;
    private int imgRightRes;
    private int imgCenterRes;
    private String textCenter;
    private ImageView imgCenter;
    private CommonTitleCallback mCommonTitleCallback;

    public CommonTitleView(Context context) {
        this(context, null);
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_common_title, this);
        btnLeft = (ImageView) findViewById(R.id.img_left);
        btnRight = (ImageView) findViewById(R.id.img_right);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgCenter = (ImageView)findViewById(R.id.img_center);
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.titleView);
        imgLeftRes = arr.getResourceId(R.styleable.titleView_left_img, R.drawable.ic_tuichu);
        imgRightRes = arr.getResourceId(R.styleable.titleView_right_img, noImg);
        imgCenterRes = arr.getResourceId(R.styleable.titleView_center_img, noImg);
        textCenter = arr.getString(R.styleable.titleView_center_text);
        if(imgRightRes!= noImg){
            btnRight.setImageResource(imgRightRes);
        }
        btnLeft.setImageResource(imgLeftRes);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        if(imgCenterRes!=noImg){
            imgCenter.setVisibility(View.VISIBLE);
            imgCenter.setImageResource(imgCenterRes);
            tvTitle.setVisibility(View.GONE);
        }
        if(textCenter!= null){
            tvTitle.setText(textCenter);
        }
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setTitleValue(String value) {
        tvTitle.setText(value);
    }

    public void setImgLeft(int imgRes) {
        btnLeft.setBackgroundResource(imgRes);
    }

    public void setImgLeftVisible(boolean visible){
        if(visible){
            btnLeft.setVisibility(View.VISIBLE);
        }else{
            btnLeft.setVisibility(View.INVISIBLE);
        }
    }

    public void setImgRight(int imgRes) {
        btnRight.setBackgroundResource(imgRes);
    }

    public void setCallback(CommonTitleCallback callback){
        if (callback != null) {
            this.mCommonTitleCallback = callback;
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("ninedau","onClick");
        if (mCommonTitleCallback != null) {
            switch (v.getId()) {
                case R.id.img_left:
                    mCommonTitleCallback.clickLeftView();
                    break;
                case R.id.img_right:
                    mCommonTitleCallback.clickRightView();
                    break;
            }
        }
    }
}
