package com.app.newwisdom.http;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by ninedau_zheng on 2017/8/23.
 */

public abstract class AbstructSingleton<T> {

    private final AtomicReference<T> ref = new AtomicReference<T>();

    public T get() {
        T ret = ref.get();
        if (ret == null) {
            synchronized (this) {
                if (ref.get() == null) {
                    ret = newObj();
                    ref.set(ret);
                }else{
                    ret = ref.get();
                }
            }
        }
        return ret;
    }

    public abstract T newObj();
}
