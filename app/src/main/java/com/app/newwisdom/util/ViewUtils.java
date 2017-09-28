package com.app.newwisdom.util;

import android.graphics.Paint;

/**
 * Created by ninedau_zheng on 2017/7/20.
 */

public class ViewUtils {

    public static int  getTextLength(Paint paint , String value){
        float[] s = new float[value.length()];
        paint.getTextWidths(value,0,value.length(),s);
        int length = 0;
        for(float f:s){
            length+= f;
        }
        return length;
    }
}
