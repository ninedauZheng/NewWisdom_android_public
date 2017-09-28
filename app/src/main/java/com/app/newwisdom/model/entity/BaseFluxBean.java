package com.app.newwisdom.model.entity;

/**
 * Created by ninedau_zheng on 2017/7/28.
 */

public class BaseFluxBean {

    private String fluxText;
    private long fluxValue;
    private long time;

    private String dateText ;

    private int style ;

    public static final int DAY_MODE = 0;
    public static final int MONTH_MODE = 1;
    public static final int YEAR_MODE = 2;


    public void setTime(long time) {
        this.time = time;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public BaseFluxBean(long value , long time){
        this.fluxValue = value;
        this.time = time;
    }

    public String getFluxText() {
        return fluxText;
    }

    public void setFluxText(String fluxText) {
        this.fluxText = fluxText;
    }

    public long getFluxValue() {
        return fluxValue;
    }

    public void setFluxValue(long fluxValue) {
        this.fluxValue = fluxValue;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
}
