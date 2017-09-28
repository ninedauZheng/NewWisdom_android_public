package com.app.newwisdom.view.viewGourp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.OnFlowDetailsTitleClickListener;
import com.app.newwisdom.util.ScreenParams;

/**
 * Created by ninedau_zheng on 2017/7/27.
 */

public class FlowDetailsTitle extends RelativeLayout implements View.OnTouchListener {

    private TextView tvDay;
    private TextView tvMonth;
    private TextView tvY;
    private View line;
    private int colorDefault = getResources().getColor(R.color.color_grey);
    private int colorSelected = getResources().getColor(R.color.color_white);
    private OnFlowDetailsTitleClickListener mListener;
    private int selectItem;
    private boolean animRunning = false;
    private ScreenParams sp;


    public FlowDetailsTitle(@NonNull Context context) {
        super(context);
        initView();
    }

    public FlowDetailsTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlowDetailsTitle(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        sp = ScreenParams.getInstance(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.flux_common_title, this);
        tvDay = (TextView) findViewById(R.id.tv_day);
        tvDay.setTextColor(colorSelected);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvMonth.setTextColor(colorDefault);
        tvY = (TextView) findViewById(R.id.tv_year);
        tvY.setTextColor(colorDefault);
        line = findViewById(R.id.line);
        tvDay.setOnTouchListener(this);
        tvMonth.setOnTouchListener(this);
        tvY.setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewGroup childViewGroup = ((ViewGroup) getChildAt(0));
        int childTotal = ((ViewGroup) getChildAt(0)).getChildCount();

        View v = childViewGroup.getChildAt(childTotal - 1);
        View v1 = ((ViewGroup) childViewGroup.getChildAt(0)).getChildAt(1);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
        int t = v1.getMeasuredHeight();
        ScreenParams sp = ScreenParams.getInstance(getContext());
        int width = sp.getScreenWidth() / 3;
        int w = v.getMeasuredWidth();
        v.layout((width - w) / 2, t , (width + w) / 2,   t + v.getMeasuredHeight());
        selectItem = tvDay.getId();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (animRunning) {
            return true;
        }
        if (selectItem == v.getId()) {
            return true;
        }
        switch (v.getId()) {
            case R.id.tv_day:
                setSelected(0, v);
                break;
            case R.id.tv_month:
                setSelected(1, v);
                break;
            case R.id.tv_year:
                setSelected(2, v);
                break;
        }
        ((TextView) findViewById(selectItem)).setTextColor(colorDefault);
        selectItem = v.getId();
        return false;
    }

    public void setSelected(final int index, final View v) {
        final int oldIndex = getSelectItem();
        int diff = index - oldIndex;
        PropertyValuesHolder va = PropertyValuesHolder.ofFloat("scaleX", 1f, Math.abs(5f * diff), 1f);
        PropertyValuesHolder ta = PropertyValuesHolder.ofFloat("translationX", line.getX() - line.getMeasuredWidth(), line.getX() + ScreenParams.getInstance(getContext()).getScreenWidth() / 3 * diff - line.getMeasuredWidth());
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(line, va, ta);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animRunning = true;
                if(mListener!=null){
                    mListener.onSelected(index);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((TextView)v).setTextColor(colorSelected);
                animRunning = false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(1000).start();

    }


    private int getSelectItem() {
        int index = -1;
        switch (selectItem) {
            case R.id.tv_day:
                index = 0;
                break;
            case R.id.tv_month:
                index = 1;
                break;
            case R.id.tv_year:
                index = 2;
                break;
        }
        return index;
    }

    public void setListener(OnFlowDetailsTitleClickListener listener) {
        this.mListener = listener;
    }
}
