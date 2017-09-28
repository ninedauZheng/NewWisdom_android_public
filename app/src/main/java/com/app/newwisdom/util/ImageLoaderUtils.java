package com.app.newwisdom.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.app.newwisdom.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by ninedau_zheng on 2017/8/14.
 */

public class ImageLoaderUtils {

    private static ImageLoaderUtils mInstance;

    private static final int THREAD_POOL_NUM = 2;
    private static final int PRIORITY = 2;
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private final int CONNECT_TIME_OUT = 10 * 1000;
    private final int READ_TIME_OUT = 30 * 1000;
    private ImageLoader mLoader;

    private ImageLoaderUtils(Context context) {
        ImageLoaderConfiguration configuration =
                new ImageLoaderConfiguration.Builder(context)
                        .threadPoolSize(THREAD_POOL_NUM)
                        .threadPriority(PRIORITY).denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new WeakMemoryCache()).diskCacheSize(DISK_CACHE_SIZE)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.FIFO)
                        .defaultDisplayImageOptions(getDisplayImageOptions())
                        .imageDownloader(new BaseImageDownloader(context, CONNECT_TIME_OUT, READ_TIME_OUT)).build();
        ImageLoader.getInstance().init(configuration);
        mLoader = ImageLoader.getInstance();
    }

    public synchronized static ImageLoaderUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImageLoaderUtils(context);
        }
        return mInstance;
    }


    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.bg_banner)
                .showImageOnLoading(R.drawable.bg_banner)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .decodingOptions(new BitmapFactory.Options())
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        return options;
    }

    public void display(ImageView imageView, String path, ImageLoadingListener listener) {
        if (mLoader != null) {
            mLoader.displayImage(path, imageView, listener);
        }
    }
}
