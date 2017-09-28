package com.app.newwisdom.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

import com.app.newwisdom.util.ScreenParams;

/**
 * Created by ninedau_zheng on 2017/7/21.
 */

public class CustomVideoView extends VideoView {

    private Paint paint;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setStrokeWidth(5);
//        paint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = getDefaultSize(0, widthMeasureSpec);
//        int height = getDefaultSize(0, heightMeasureSpec);
//        setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        Log.i("ninedau","screen = "+ ScreenParams.getInstance(getContext()).getScreenWidth()+",width = "+MeasureSpec.getSize(widthMeasureSpec)+" height = "+MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(10,10,5,paint);
    }
}
