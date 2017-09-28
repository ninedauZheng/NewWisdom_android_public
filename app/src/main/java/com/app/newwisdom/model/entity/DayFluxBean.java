package com.app.newwisdom.model.entity;

import java.util.Calendar;

/**
 * Created by ninedau_zheng on 2017/7/28.
 */

public class DayFluxBean extends BaseFluxBean {

    public DayFluxBean(long value , long time ){
        super(value,time);
        setStyle(DAY_MODE);
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);

        setDateText(ca);
    }

    private void setDateText(Calendar ca){
        setDateText(ca.get(Calendar.DAY_OF_MONTH)+"");
    }


}
