package com.app.newwisdom.view.viewGourp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.app.newwisdom.R;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.model.entity.Router;
import com.app.newwisdom.model.entity.SimBean;
import com.app.newwisdom.util.ScreenParams;
import com.app.newwisdom.view.view.CirqueView;

import java.util.List;

/**
 * Created by ninedau_zheng on 2017/7/20.
 */

public class FluxCircleViewGroup extends FrameLayout {
    private double cardUsed;
    private List<SimBean> simList;
    private double cardTotal;
    private CirqueView cv;
    private OnClickListener btnListener;
    private ScreenParams sp;

    public FluxCircleViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public FluxCircleViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FluxCircleViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        sp = ScreenParams.getInstance(getContext());
        Button btn = new Button(getContext());
        btn.setLayoutParams(new LayoutParams(sp.getFormatPX(200), sp.getFormatHeight(60)));
        btn.setBackgroundResource(R.drawable.btn_refresh_sel);
        Drawable d = getResources().getDrawable(R.drawable.ic_shuaxin);
        d.setBounds(sp.getFormatPX(50), 0, sp.getFormatPX(50) + sp.getFormatHeight(60) / 2, sp.getFormatHeight(60) / 2);

        btn.setCompoundDrawables(d, null, null, null);
        btn.setPadding(0, 0, 0, 0);
        btn.setText(getResources().getText(R.string.update));
        btn.setTextSize(sp.getFormatPX(9));
        btn.setTextColor(Color.WHITE);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        cv = new CirqueView(getContext());
        addView(cv);
        addView(btn);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        View view = (View) getChildAt(1);
        FrameLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
        int l = (sp.getScreenWidth() - lp.width) / 2;
        int t = getMeasuredHeight() - lp.height - sp.getFormatHeight(120);
        view.layout(l, t, l + lp.width, t + lp.height);
    }

    public void addListener(OnClickListener listener) {
        this.btnListener = listener;
    }

    public void update() {
        if (btnListener != null) {
            btnListener.onClick(this);
        }
    }

    public void updateValue() {
        DeviceManager dm = DeviceManager.getInstance();
        Router router = dm.getRouter();
        if (router == null) {
            cv.setValueUpdate(0, 1);
        } else {
            simList = router.simList;
            if(simList!=null){
                SimBean sim = simList.get(0);

                cardUsed = sim.usedFlowSize;
                cardTotal = sim.totalFlowSize;
                cv = (CirqueView) getChildAt(0);
                cv.setValueUpdate(cardUsed, cardTotal);
            }
        }
    }
}
