package com.app.newwisdom.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.app.newwisdom.R;
import com.app.newwisdom.util.ScreenParams;
import com.app.newwisdom.util.ViewUtils;

import java.math.BigDecimal;

/**
 * Created by ninedau_zheng on 2017/7/19.
 */

public class CirqueView extends View {

    public int height = ScreenParams.getInstance(getContext()).getFormatHeight(550);
    private Paint mPaint;
    private Paint mColorPaint;
    private String text ="0M";
    private Paint mTextPaint;

    private Paint mValuePaint;

    private int mPaintWidth = ScreenParams.getInstance(getContext()).getFormatPX(48);
    private double useFlux;
    private double totalFlux;

    public CirqueView(Context context) {
        this(context, null);
    }

    public CirqueView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CirqueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getContext().getResources().getColor(R.color.color_bg_grey));
        mPaint.setAlpha(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setStyle(Paint.Style.STROKE);
        mColorPaint.setStrokeCap(Paint.Cap.ROUND);
        mColorPaint.setStrokeWidth(mPaintWidth);
        int[] colors = {getResources().getColor(R.color.color_bg_start),getResources().getColor(R.color.color_bg_finish),getResources().getColor(R.color.color_bg_start)};
        float[] positions = {0f,0.8f,1f};
        SweepGradient sg = new SweepGradient(0,0,colors,positions);
        mColorPaint.setShader(sg);
        Matrix matrix = new Matrix();
        sg.setLocalMatrix(matrix);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(ScreenParams.pt2px(getContext(),26));
        mTextPaint.setColor(getResources().getColor(R.color.color_grey));

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setTextSize(ScreenParams.pt2px(getContext(),48));
        mValuePaint.setColor(getResources().getColor(R.color.color_white));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(ScreenParams.getInstance(getContext()).getScreenWidth(), (int) (height/2+height/2*Math.sin(15)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCirqueView(canvas);
        drawText(canvas);
    }


    private void drawCirqueView(Canvas canvas){
        canvas.save();
        canvas.translate(ScreenParams.getInstance(getContext()).getScreenWidth()/2,height/2);
        canvas.rotate(165);
        RectF rectf = new RectF(-(height) / 2 + mPaintWidth / 2, -(height) / 2 + mPaintWidth / 2, (height) / 2 - mPaintWidth / 2, height / 2 - mPaintWidth / 2);
        canvas.drawArc(rectf, 0,210, false, mPaint);
        canvas.drawArc(rectf,0,getFluxPercent(),false,mColorPaint);
        canvas.restore();
    }

    public void setValueUpdate(double useFlux,double totalFlux){
        this.useFlux = useFlux;
        this.totalFlux = totalFlux;
        invalidate();
    }

    private float getFluxPercent(){
        if(totalFlux < 0){
            return 0;
        }
        if(useFlux<100000){
            totalFlux = 100000;
        }else{
            totalFlux = useFlux+10000;
        }
//       return (float) ((totalFlux-useFlux)/totalFlux*210);
        return (float) (useFlux/totalFlux*210);
    }

    private String getFluxValue(){
        double hasFlux = useFlux;
        if(hasFlux>1000){
            double a = hasFlux / 1000;
            double b = a / 1.0;
            double c = (double) Math.round(b);
            BigDecimal bd = new BigDecimal(b);
            bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
            text = bd+"G";
        }else{
            BigDecimal bd = new BigDecimal(hasFlux);
            bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
            text = bd+"M";
        }
        return text;
    }


    private void drawText(Canvas canvas){
        int top = ScreenParams.getInstance(getContext()).getFormatHeight((int) (48+60+mTextPaint.getTextSize()*0.66));
        String couldFlux = getResources().getString(R.string.could_use_flux);
        int valueLength = ViewUtils.getTextLength(mTextPaint,couldFlux);
        int left = (ScreenParams.getInstance(getContext()).getScreenWidth()-valueLength)/2;
        canvas.drawText(couldFlux,left,top,mTextPaint);

        int topValue = ScreenParams.getInstance(getContext()).getFormatHeight((int) (48+60+10+mTextPaint.getTextSize()+mValuePaint.getTextSize()*0.66 ));
        int leftValue = (ScreenParams.getInstance(getContext()).getScreenWidth()-ViewUtils.getTextLength(mValuePaint,getFluxValue()))/2;
        canvas.drawText(getFluxValue(),leftValue,topValue,mValuePaint);
    }


    public void setText(String value){
        this.text = value;
        invalidate();
    }

}
