package com.app.newwisdom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.newwisdom.R;
import com.app.newwisdom.util.ImageLoaderUtils;
import com.app.newwisdom.view.view.BatteryView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ninedau_zheng on 2017/8/14.
 */

public class TestActivity extends Activity implements View.OnClickListener {

    private TextView tv;
    private Button btn;
    private int aaa;
    private BatteryView bv;

    private DisplayImageOptions.Builder builder;
    private ImageLoaderUtils loader;
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        init();
//        init1();
        init2();
    }

    private void init2() {
        loader = ImageLoaderUtils.getInstance(TestActivity.this);
        ViewPager viewpager = (ViewPager) findViewById(R.id.vp);
        ((Button) findViewById(R.id.btn)).setOnClickListener(this);
        ArrayList<View> list = new ArrayList<>();
        list.add(getImageView());
        list.add(getImageView());
        list.add(getImageView());
        list.add(getImageView());
         adapter = new MyAdapter(list);
        viewpager.setAdapter(adapter);
    }

    private ImageView getImageView() {
        ImageView img = new ImageView(TestActivity.this);
        img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setImageDrawable(getResources().getDrawable(R.drawable.bg_banner));
        return img;
    }

    @Override
    public void onClick(View v) {
        List<String> path = new ArrayList<>();
        path.add("http://47.92.31.204:1823/static/upload/1502095065iptzj.jpg");
        path.add("http://47.92.31.204:1823/static/upload/1502095065iptzj.jpg");
        path.add("http://47.92.31.204:1823/static/upload/1502095065iptzj.jpg");
        path.add("http://47.92.31.204:1823/static/upload/1502095065iptzj.jpg");
        adapter.update(path);
    }

    class MyAdapter extends PagerAdapter {

        private List<View> list;
        private List<String> path;

        public void update(List<String> path) {
            this.path = path;
            notifyDataSetChanged();
        }

        public MyAdapter(List<View> list) {
            this.list = list;

        }

        @Override
        public int getItemPosition(Object object) {
            Log.i("ninedau","getPostion");
            View v = (View)object;
            int tag = (int) v.getTag();
            if(v instanceof ImageView){
                if (path != null && !TextUtils.isEmpty(path.get(tag))) {
                    loader.display((ImageView)v, path.get(tag), null);
                }
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("ninedau", "instantiate" + position);
            ImageView img = (ImageView) list.get(position);
            img.setTag(position);
            if (path != null && !TextUtils.isEmpty(path.get(position))) {
                loader.display(img, path.get(position), null);
            }
            container.addView(img);
            return list.get(position);
        }
    }
}
