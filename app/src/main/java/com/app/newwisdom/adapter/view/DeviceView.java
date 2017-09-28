package com.app.newwisdom.adapter.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.adapter.DeviceAdapter;
import com.app.newwisdom.model.item.DeviceItem;

import static com.app.newwisdom.adapter.DeviceAdapter.COMMON_CHILD_VIEW;
import static com.app.newwisdom.adapter.DeviceAdapter.TITLE_VIEW_BIG;
import static com.app.newwisdom.adapter.DeviceAdapter.TITLE_VIEW_SMALL;

/**
 * Created by ninedau_zheng on 2017/8/2.
 */

public class DeviceView extends RecyclerView.ViewHolder {
    private View mView;
    public TextView tv;



    public DeviceView(View itemView ) {
        super(itemView);
        this.mView = itemView;
    }


    public void bindView(final int position , DeviceItem item , View.OnClickListener listener) {
        if (item.type == DeviceAdapter.COMMON_PARENT_VIEW) {
            tv = (TextView) mView.findViewById(R.id.tv_title);
            tv.setText(item.title);
            item.childItem = new DeviceItem(position,COMMON_CHILD_VIEW, "");
            if(listener!=null){
                mView.setOnClickListener(listener);
            }
        } else if (item.type == TITLE_VIEW_BIG || item.type == TITLE_VIEW_SMALL) {
            tv = (TextView) mView.findViewById(R.id.tv_title);
            tv.setText(item.title);
        } else {

        }
    }


}
