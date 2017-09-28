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
 * Created by ninedau_zheng on 2017/8/14.
 */

public class SignView extends View {
    private Paint unSignPaint;
    private Paint signPaint;
    private int numSign = 3;
    private int gap;
    private int paintWidth;

    public SignView(Context context) {
        this(context, null);
    }

    public SignView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        signPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        signPaint.setColor(getResources().getColor(R.color.sign));
        unSignPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unSignPaint.setColor(getResources().getColor(R.color.un_sign));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        int width = getMeasuredWidth();
         paintWidth = ScreenParams.getInstance(getContext()).getFormatPX(3);
         gap = (width - paintWidth * 5) / 4;
        signPaint.setStrokeWidth(paintWidth);
        unSignPaint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0;i<5;i++){
            int startX = i*gap+(i>0?(i-1)*paintWidth:0)+paintWidth/2;
            int startY = getMeasuredHeight();
            int finishX = startX;
            int finishY = getMeasuredHeight()*(6-i-1)/6;
            canvas.drawLine(startX,startY,finishX,finishY,((i+1)<=numSign?signPaint:unSignPaint));
        }
    }

    public void setNumSign(int num) {
        this.numSign = num;
        invalidate();
    }
}
