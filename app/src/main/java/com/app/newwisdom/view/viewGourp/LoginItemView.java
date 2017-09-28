package com.app.newwisdom.view.viewGourp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.LoginItemCallback;
import com.app.newwisdom.util.ScreenParams;

/**
 * Created by ninedau_zheng on 2017/8/28.
 */

public class LoginItemView extends FrameLayout {
    private String text;

    private int height = ScreenParams.getInstance(getContext()).getFormatHeight(20);
    private int animationHeight;
    private EditText et;
    private int hint;
    private boolean initStatus = false;
    private LoginItemCallback callback;

    public LoginItemView(@NonNull Context context) {
        this(context, null);
    }

    public LoginItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
        addChildView();
    }

    private void addChildView() {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setTextSize(height);
        tv.setTextColor(getResources().getColor(R.color.color_white));
        addView(tv);
         et = new EditText(getContext());
        et.setBackground(null);
        et.setSingleLine();
        et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        addView(et);
    }

    private void initView(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.LoginItemView);
        text = attr.getString(R.styleable.LoginItemView_tv_text);
         hint = attr.getResourceId(R.styleable.LoginItemView_hint, R.string.pls_input_mobile_num);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        View child = getChildAt(0);
        ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, animationHeight * 0.8f);
        View child1 = getChildAt(1);
        int childTop = top + animationHeight + ((child1.getMeasuredHeight() - child.getMeasuredHeight()) / 2);
        child.layout(left, top + animationHeight + ((child1.getMeasuredHeight() - child.getMeasuredHeight()) / 2), left + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
        ((EditText) child1).setTextSize(TypedValue.COMPLEX_UNIT_PX, animationHeight * 0.8f);
        child1.layout(left, top + animationHeight, right, top + animationHeight + child1.getMeasuredHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewGroupHeight = MeasureSpec.getSize(heightMeasureSpec);
        int viewGroupWidth = MeasureSpec.getSize(widthMeasureSpec);
        animationHeight = viewGroupHeight / 3;
        height = viewGroupHeight / 3 * 2;
        ViewGroup.LayoutParams layoutParams = getChildAt(0).getLayoutParams();
        measureChild(getChildAt(0), layoutParams.width, layoutParams.height);

        ViewGroup.LayoutParams layoutParams1 = getChildAt(1).getLayoutParams();
        measureChild(getChildAt(1), MeasureSpec.getSize(widthMeasureSpec), layoutParams1.height);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        startAnimationText();
        return super.onInterceptTouchEvent(ev);
    }

    public void setCallback(LoginItemCallback callback){
        if (callback != null) {
            this.callback = callback;
        }
    }


    private void startAnimationText() {
        if(initStatus)return;
        TextView tv = (TextView) getChildAt(0);
        float translationY = tv.getTranslationY();
        ObjectAnimator anim = ObjectAnimator.ofFloat(tv, "translationY", translationY, -animationHeight);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(tv, "scaleX", 1f, 0.8f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(tv, "scaleY", 1f, 0.8f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(anim1).with(anim2);
        animSet.setDuration(250);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                et.setBackgroundResource(R.drawable.line);
                et.setHint(hint);
                initStatus = true;
                if(callback!=null)callback.animationFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public String getEditText(){
       return et.getText().toString();
    }
}
