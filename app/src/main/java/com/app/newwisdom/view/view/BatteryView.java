package com.app.newwisdom.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.app.newwisdom.R;

/**
 * Created by ninedau_zheng on 2017/8/14.
 */

public class BatteryView extends View {
    private Bitmap mBitmap;
    private Rect msrcRect;
    private Rect mDesRect;
    private Paint mBitPaint;
    private Paint mBatteryPaint;
    private int batteryValue = 89;

    public BatteryView(Context context) {
        this(context, null);

    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.ic_dianchi)).getBitmap();


        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);

        mBatteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatteryPaint.setColor(0xff3CB371);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        msrcRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mDesRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, msrcRect, mDesRect, mBitPaint);
        canvas.drawRect(getRect(),mBatteryPaint);
    }

    private Rect getRect() {
        int left = (int) (getMeasuredWidth()*0.06f);
        int top =(int) (getMeasuredWidth()*0.06f);
        int right = (int) (((int) (getMeasuredWidth()*0.86f)-left)*batteryValue/100f)+left;
        int bottom = getMeasuredHeight() - top;
        Rect rect= new Rect(left,top,right,bottom);
        return rect;
    }

    public void setValue(int value) {
        value = (value==-1?89:value);
        this.batteryValue = value;
        if(value<=20){
            mBatteryPaint.setColor(Color.RED);
        }else{
            mBatteryPaint.setColor(Color.GREEN);
        }
        invalidate();
    }

}
