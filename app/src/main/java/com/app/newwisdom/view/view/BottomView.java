package com.app.newwisdom.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.util.ScreenParams;

/**
 * Created by ninedau_zheng on 2017/7/31.
 */

public class BottomView extends ViewGroup {
    private String title;
    private int res;
    private ImageView imgView;
    private TextView tvTitle;

    public BottomView(Context context) {
        this(context , null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.BottomView);
        res = arr.getResourceId(R.styleable.BottomView_icon_res,R.drawable.ic_liuliang);
        title = arr.getString(R.styleable.BottomView_tv_title);
        arr.recycle();
        LayoutInflater.from(getContext()).inflate(R.layout.item_bottom,this);
        imgView = (ImageView)findViewById(R.id.img);
        tvTitle = (TextView)findViewById(R.id.tv);
        imgView.setImageResource(res);
        if(title != null){
            tvTitle.setText(title);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View ll = getChildAt(0);
        ll.layout(0,0,getMeasuredWidth(),getMeasuredHeight());

        View childImg = ((ViewGroup)ll).getChildAt(0);
        int imgLeft = ScreenParams.getInstance(getContext()).getFormatPX(40);
        int imgTop = (getMeasuredHeight()-childImg.getMeasuredHeight())/2;
        childImg.layout(imgLeft, imgTop ,imgLeft+childImg.getWidth(),imgTop+childImg.getHeight());


        View childArrow = ((ViewGroup)ll).getChildAt(2);
        int childArrowWidth  = childArrow.getWidth();
        int childArrowHeight  = childArrow.getHeight();
        int arrLeft = getMeasuredWidth()-childArrowWidth - ScreenParams.getInstance(getContext()).getFormatPX(40);
        int arrTop = (getMeasuredHeight()-childArrow.getMeasuredHeight())/2;
        childArrow.layout(arrLeft,arrTop,arrLeft+childArrowWidth,arrTop+childArrowHeight);

        View childText = ((ViewGroup)ll).getChildAt(1);
        childText.layout(imgLeft+childImg.getWidth()+ScreenParams.getInstance(getContext()).getFormatPX(30),(getMeasuredHeight()-childText.getMeasuredHeight())/2,arrLeft-10,(getMeasuredHeight()-childText.getMeasuredHeight())/2+childText.getHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }


    public void setTitle(String name ){
        if (name != null) {
            tvTitle.setText(name);
        }
    }
}
