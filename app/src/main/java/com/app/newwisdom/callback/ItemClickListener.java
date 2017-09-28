package com.app.newwisdom.callback;

import com.app.newwisdom.model.item.DeviceItem;

/**
 * Created by ninedau_zheng on 2017/7/24.
 */

public interface ItemClickListener {
    void  onExpandChildren(DeviceItem item);

    void  onHidenChildren(DeviceItem item);
}
