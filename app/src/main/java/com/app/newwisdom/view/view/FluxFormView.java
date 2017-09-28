package com.app.newwisdom.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.app.newwisdom.R;
import com.app.newwisdom.model.entity.BaseFluxBean;
import com.app.newwisdom.model.entity.DayFluxBean;
import com.app.newwisdom.util.ScreenParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/7/28.
 */

public class FluxFormView extends View {
    private Paint mPaint;
    private ScreenParams sp;

    private int bottomColor = getResources().getColor(R.color.colorPrimaryDark);
    private int bottomHeight = 100;
    private int textSize = 32;
    private List<BaseFluxBean> mlist;
    private Paint mTextPaint;
    private long mMaxValue;

    private int selectedItem = 0;
    private float mItemWidth;

    private Paint mLinePaint;
    private int mLineColor = getResources().getColor(R.color.color_white);
    private int lineWidth = 1;
    private int mStartIndex;
    private int mFinishIndex;
    private float unitLength;

    private Path mPath;
    private Paint mNumLinePaint;
    private int mNumLineWidth = 8;
    private ArrayList<Point> mPointList;


    public FluxFormView(Context context) {
        this(context, null);
    }

    public FluxFormView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FluxFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        sp = ScreenParams.getInstance(getContext());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(bottomColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(sp.getFormatHeight(textSize));
        mTextPaint.setColor(getResources().getColor(R.color.color_white));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(sp.getFormatPX(1));
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(mLineColor);

        mNumLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumLinePaint.setStyle(Paint.Style.STROKE);
        mNumLineWidth = initValue(mNumLineWidth);
        mNumLinePaint.setColor(mLineColor);
        mNumLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mNumLinePaint.setStrokeWidth(mNumLineWidth);

        mItemWidth = sp.getScreenWidth() / 7f;

        bottomHeight = initValue(bottomHeight);
    }

    private int initValue(int value) {
        return sp.getFormatHeight(value);
    }

    public void setFluxList(List<BaseFluxBean> list) {
        this.mlist = list;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("ninedau", "width  = " + sp.getScreenWidth() + "height = " + MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(sp.getScreenWidth(), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBottomItem(canvas);
        canvas.translate(locationX, 0);
        canvas.save();
        computationalItemsInScreen();
        drawTimeText(canvas);
        drawLine(canvas);
        drawValueLine(canvas);
        drawArc(canvas);
    }

    private void drawValueLine(Canvas canvas) {

    }

    private void drawLine(Canvas canvas) {
        if (mlist != null && mlist.size() != 0) {
            checkUtilLength();
        }

    }

    private void computationalItemsInScreen() {
        float length = -locationX;

        mStartIndex = (int) Math.floor(length / mItemWidth);

        mFinishIndex = (int) Math.ceil((length + sp.getScreenWidth()) / mItemWidth);
        Log.i("ninedau", "start Index= " + mStartIndex + " ,finish Index= " + mFinishIndex);
    }


    //获取最大的flux数值 并设定单位长度；
    private void checkUtilLength() {
        long v = 0;
        for (BaseFluxBean bean : mlist) {
            if (bean.getFluxValue() > v) {
                v = bean.getFluxValue();
            }
        }
        unitLength = (getMeasuredHeight() - bottomHeight) * 0.9f / v;
    }


    private void drawArc(Canvas canvas) {
//        Path path = new Path();
//        path.moveTo();
    }

    private void drawTimeText(Canvas canvas) {
        if (mlist == null) return;
        int gapWidth = sp.getFormatPX(102);
        float gapHeight = getTextTopLocation();
        switch (mlist.get(0).getStyle()) {
            case BaseFluxBean.DAY_MODE:
                Path path = new Path();
                int s = (mStartIndex <= mlist.size() ? mStartIndex : mlist.size() - 1);
                float cX = 0;
                float cY = 0;
                mPointList = new ArrayList<>();
                for (int i = s; i <= (mFinishIndex < mlist.size() ? mFinishIndex : mlist.size() - 1); i++) {
                    String value = ((DayFluxBean) mlist.get(i)).getDateText();
                    long flux = mlist.get(i).getFluxValue();
                    int left = (int) ((gapWidth - getTextWidth(value)) / 2);
//
                    mLinePaint.setAlpha(getLineAlpha(i * gapWidth + gapWidth / 2 + locationX));
                    float sX = i * gapWidth + gapWidth / 2;
                    float sY = getMeasuredHeight() - bottomHeight - 1;
                    float fX = i * gapWidth + gapWidth / 2;
                    float fY = getMeasuredHeight() - bottomHeight - 1 - (flux * unitLength);
                    canvas.drawLine(sX, sY, fX, fY, mLinePaint);
                    mPointList.add(new Point((int)fX,(int)fY));
//                    if (i == s) {
//                        path.moveTo(fX, fY);
//                    } else if (i > s) {
//                        path.quadTo((cX+fX)/2, (cY+fY)/3,fX,fY);
//                    }
//                    cX = fX;
//                    cY = fY;
                    canvas.drawText(value, i * gapWidth + left, gapHeight, mTextPaint);
                }
                drawScrollLine(canvas);

                break;
            case BaseFluxBean.MONTH_MODE:
                break;
            case BaseFluxBean.YEAR_MODE:
                break;
        }
    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPointList.size()-1; i++)
        {
            startp = mPointList.get(i);
            endp = mPointList.get(i+1);
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path,mNumLinePaint);
        }
    }


    private int getLineAlpha(float locationX) {
        float alpha = 1 - Math.abs(locationX - sp.getScreenWidth() / 2) / (sp.getScreenWidth() / 2);
        return (int) (alpha * 255);
    }

    private float getTextTopLocation() {
        int t = (int) ((bottomHeight - mTextPaint.getTextSize()) / 2 + mTextPaint.getTextSize() + (getMeasuredHeight() - bottomHeight));
        return t;
    }

    private float getTextWidth(String value) {
        float[] all = new float[value.length()];
        mTextPaint.getTextWidths(value, 0, value.length(), all);
        float sum = 0;
        for (float f : all) {
            sum += f;
        }
        return sum;
    }


    private void drawBottomItem(Canvas canvas) {
        Rect rect = new Rect(0, getMeasuredHeight() - bottomHeight, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(rect, mPaint);
    }

    float locationX = 0;

    float x = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float journey = x - event.getX();
                x = event.getX();
                moveAnimation(journey);
                break;
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x = 0f;
                break;
        }
        return true;
    }

    private void moveAnimation(final float journey) {
        locationX = locationX - journey;

        if (locationX > 0) {
            locationX = 0;
        }
        invalidate();
//        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "translationX", locationX,  -journey*0.1f);
//        oa.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
////                locationX = locationX +journey;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        oa.setDuration(100).start();
    }
}
