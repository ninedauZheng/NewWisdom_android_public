package com.app.newwisdom.view.viewGourp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.newwisdom.R;
import com.app.newwisdom.callback.OnViewPagerClickCallback;
import com.app.newwisdom.model.entity.Banner;
import com.app.newwisdom.util.ImageLoaderUtils;
import com.app.newwisdom.util.ScreenParams;
import com.app.newwisdom.view.view.CircleGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/7/16.
 */

public class CommonViewPager extends FrameLayout {

    public ViewPager vp;
    public CircleGroup cg;
    private List<String> imgURLList;
    ViewPagerAdapter adapter;
    private ImageLoaderUtils utils = null;
    //设置图片资源
    private ArrayList<View> imageList;

    private OnViewPagerClickCallback callback;
    private int num;

    public CommonViewPager(Context context) {
        super(context);
    }

    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommonViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        utils = ImageLoaderUtils.getInstance(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vp = (ViewPager) getChildAt(0);
        cg = (CircleGroup) getChildAt(1);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        vp.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        cg.layout(0, getMeasuredHeight() - cg.getMeasuredHeight() - ScreenParams.getInstance(getContext()).getFormatHeight(10), getMeasuredWidth(), getMeasuredHeight() - ScreenParams.getInstance(getContext()).getFormatHeight(10));
        if (adapter == null) {
            adapter = new ViewPagerAdapter(null);
            vp.setAdapter(adapter);
            addListener();
            initData();
        }
    }

    public void setOnPagerClickCallback(OnViewPagerClickCallback callback){
        if (callback != null) {
            this.callback=callback;
        }
    }

    private void initData() {
        if (imgURLList != null && imgURLList.size() > 0) {
            setImageView();
        }
    }

    private void setImageView() {
        imageList = new ArrayList<>();
        for (String va : imgURLList) {
            ImageView img = new ImageView(getContext());
            img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getMeasuredHeight()));
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            imageList.add(img);
        }
        initCircleGroup(getImgCount());
    }

    private int getImgCount() {
        return imgURLList.size() > 3 ? imgURLList.size() : 4;
    }

    private ImageView getDefaultView() {
        ImageView img = new ImageView(getContext());
        img.setImageResource(R.drawable.bg_banner);
        img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getMeasuredHeight()));
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        return img;
    }

    public void updateData(List<Banner> bannerList) {
        if (bannerList != null && bannerList.size() != 0) {
            Log.i("ninedau","adapter ,bannerList="+bannerList.size());

            adapter.update(bannerList);
            if(bannerList.size()>3){
                cg.setTotalNum(bannerList.size());
            }else{
                cg.setTotalNum(4);
            }
        }
    }

    private void addListener() {

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cg.setSelectedItem(position % getImgCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

    public void initCircleGroup(int num) {
        if (cg == null) {
            cg = (CircleGroup) getChildAt(1);
        }
        cg.setTotalNum(num);

    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<View> mList;
        private List<Banner> pathList;

        public ViewPagerAdapter(List<View> list) {
            if (list != null) {
                this.mList = list;
            } else {
                setInitImageView();
            }
        }

        public void update(List<Banner> pathList) {
//            if(pathList!=null&&pathList.size()!=0){
//                clearList();
//            }
            this.pathList = pathList;
            mList = imageList;
            resetList();
            notifyDataSetChanged();
        }

        private void resetList() {
            if (pathList.size() < 4) {
                int add = 4 - pathList.size();
                for (int i = 0; i < add; i++) {
                    Log.i("ninedau","adddddddd");
//                    mList.add(mList.get(i));
                    pathList.add(pathList.get(i));
                }

            }
            num = mList.size();
        }

        private void clearList(){
            mList.clear();
            imgURLList.clear();
        }


        private void setInitImageView() {
            mList = new ArrayList<>();
            num = 4;
            mList.add(getDefaultView());
            mList.add(getDefaultView());
            mList.add(getDefaultView());
            mList.add(getDefaultView());
            imgURLList = new ArrayList<>();
            imgURLList.add("");
            imgURLList.add("");
            imgURLList.add("");
            imgURLList.add("");

        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            View v = (View) object;
            int tag = (int) v.getTag();
            if (v instanceof ImageView) {
                if (pathList != null && !TextUtils.isEmpty(pathList.get(tag).pic)) {
                    utils.display((ImageView) v, pathList.get(tag).pic, null);
                    final int finalPosition = tag;
                    v.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("ninedau","finalPostion ="+finalPosition);
                            if(callback!=null){
                                callback.onPagerItemClick(finalPosition);
                            }
                        }
                    });
                }
            }
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mList.size();
            View view = mList.get(position % mList.size());
            view.setTag(position % mList.size());
            if (pathList != null && !TextUtils.isEmpty(pathList.get(position).pic)) {
                utils.display((ImageView) view, pathList.get(position).pic, null);
                final int finalPosition = position;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ninedau","finalPostion ="+finalPosition);
                        if(callback!=null){
                            callback.onPagerItemClick(finalPosition);
                        }
                    }
                });
            }
            container.addView(view, 0);
            return mList.get(position);
        }
    }


}
