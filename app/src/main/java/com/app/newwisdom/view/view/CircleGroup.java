package com.app.newwisdom.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.app.newwisdom.R;
import com.app.newwisdom.util.ScreenParams;

/**
 * Created by ninedau_zheng on 2017/7/16.
 */

public class CircleGroup extends View {
    private Paint mPaint;
    private int circleDiameter = 10;
    private int gap = 10;
    private ScreenParams sp;
    private int mSelectedIndex = 0;
    private int totalNum;
    private Paint mSelectPaint;


    public CircleGroup(Context context) {
        this(context, null);
    }

    public CircleGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_grey));
        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(getResources().getColor(R.color.color_white));

        sp = ScreenParams.getInstance(getContext());
        circleDiameter = sp.getFormatPX(circleDiameter);
        gap = ScreenParams.getInstance(getContext()).getFormatPX(gap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int total = circleDiameter * totalNum + gap * (totalNum - 1);
        int startX = (sp.getScreenWidth() - total) / 2;
        for (int i = 0; i < totalNum; i++) {
            canvas.drawCircle(startX + circleDiameter / 2 + (circleDiameter + gap) * i, circleDiameter / 2, circleDiameter / 2, i == mSelectedIndex ? mSelectPaint : mPaint);
        }
    }

    public void setSelectedItem(int index) {
        this.mSelectedIndex = index;
        invalidate();
    }

    public void setTotalNum(int total) {
        this.totalNum = total;
        invalidate();
    }
}
