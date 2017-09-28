package com.app.newwisdom.view.viewGourp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.app.newwisdom.R;
import com.app.newwisdom.util.ScreenParams;
import com.app.newwisdom.view.view.CustomVideoView;

/**
 * Created by ninedau_zheng on 2017/7/21.
 */

public class AnimationViewGroup extends FrameLayout implements View.OnTouchListener {

    private int width;
    private int height;
    private int h;
    private int w;
    private int id;

    public AnimationViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public AnimationViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        addView();
    }

    private void addView() {
        CustomVideoView view = new CustomVideoView(getContext());
        width = ScreenParams.getInstance(getContext()).getScreenWidth();
        height = ScreenParams.getInstance(getContext()).getScreenHeight();
        w = (int) (ScreenParams.getInstance(getContext()).getScreenWidth() * 0.6);
        h = (int) (ScreenParams.getInstance(getContext()).getScreenHeight() * 0.6);
        view.setLayoutParams(new LayoutParams(w, h));
        view.setOnTouchListener(this);
        view.setId(id);
        id++;
        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        if(getChildCount()>3){
        View view = getChildAt(0);
        view.setBackgroundColor(getResources().getColor(R.color.color_common_bg));
        view.layout((width - w) / 2, (height - h) / 2, w + (width - w) / 2, h + (height - h) / 2);
        startAnimation(view);


//        }

    }

    public void startAnimation(View view) {
        ValueAnimator va = new ValueAnimator();
        va.setDuration(500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void touchViewAnim() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("ninedau", "v.thing  = " + event.getX() + "，y=" + event.getY());
        return false;
    }
}
