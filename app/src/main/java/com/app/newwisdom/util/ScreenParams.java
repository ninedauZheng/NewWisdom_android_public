package com.app.newwisdom.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ninedau_zheng on 2017/7/16.
 */

public class ScreenParams {

    private static ScreenParams sp;
    private int realHeight;
    private int realWidth;
    public int width = 720;
    public int height = 1280;
    private Context context;


    private ScreenParams(Context context) {
        this.context = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        realHeight = dm.heightPixels;
        realWidth = dm.widthPixels;
    }

    public synchronized static ScreenParams getInstance(Context context) {
        if (sp == null) {
            sp = new ScreenParams(context);
        }
        return sp;
    }

    public int getFormatPX(int w) {

        return realWidth * w / width;
    }

    public int getFormatHeight(int h) {
        return realHeight * h / height;
    }

    public int getScreenWidth() {
        return realWidth;
    }

    public int getScreenHeight() {
        return realHeight;
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int pt2px(Context context, int pt){
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)((pt*72/120) * fontScale + 0.5f);
    }

}
