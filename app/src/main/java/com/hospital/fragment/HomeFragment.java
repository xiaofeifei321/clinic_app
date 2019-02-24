package com.hospital.fragment;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.hospital.util.Events;
import com.hospital.util.SystemHelper;
import com.hospital.yuyue.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class HomeFragment extends Fragment implements OnClickListener{
	private View view;
	private TextView tView1, tView2,tView3, tView4;
	private LinearLayout layout1, layout2,layout3, layout4;
	
	private LinearLayout ll_point;
    private ArrayList<View> arrayList;
    private ArrayList<ImageView> imageViews;
    private Timer timer;
    private LayoutInflater HeadlayoutInflater;
    private ViewPager viewPager;
    

    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_fragment, null);
		tView1 = (TextView) view.findViewById(R.id.you_ly_tv_1);
		tView2 = (TextView) view.findViewById(R.id.you_ly_tv_2);
		tView3 = (TextView) view.findViewById(R.id.you_ly_tv_3);
		tView4 = (TextView) view.findViewById(R.id.you_ly_tv_4);
		layout1 = (LinearLayout) view.findViewById(R.id.you_ly_1);
		layout2 = (LinearLayout) view.findViewById(R.id.you_ly_2);
		layout3 = (LinearLayout) view.findViewById(R.id.you_ly_3);
		layout4 = (LinearLayout) view.findViewById(R.id.you_ly_4);
		tView1.setOnClickListener(this);
		tView2.setOnClickListener(this);
		tView3.setOnClickListener(this);
		tView4.setOnClickListener(this);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ll_point = (LinearLayout)view. findViewById(R.id.ll_point);
		initHeadImage();
		return view;
	}
    public void draw_Point(int index) {
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImageResource(R.drawable.point_gray);
        }
        imageViews.get(index).setImageResource(R.drawable.point_red);
    }
    
    public void initPoint() {
        imageViews = new ArrayList<ImageView>();
        ImageView imageView;

        for (int i = 0; i < 6; i++) {
            imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            ll_point.addView(imageView, layoutParams);

            imageViews.add(imageView);
        }
    }
    
    @SuppressLint("NewApi")
	public void initPagerChild() {
        	arrayList = new ArrayList<View>();
            ImageView imageView0 = new ImageView(getActivity());
            imageView0.setScaleType(ScaleType.FIT_XY);
            imageView0.setImageResource(R.drawable.banner01);

            ImageView imageView1 = new ImageView(getActivity());
            imageView1.setScaleType(ScaleType.FIT_XY);
            imageView1.setImageResource(R.drawable.banner02);
            
            ImageView imageView2 = new ImageView(getActivity());
            imageView2.setScaleType(ScaleType.FIT_XY);
            imageView2.setImageResource(R.drawable.banner03);
           
            ImageView imageView3 = new ImageView(getActivity());
            imageView3.setScaleType(ScaleType.FIT_XY);
            imageView3.setImageResource(R.drawable.banner04);
            
            ImageView imageView4 = new ImageView(getActivity());
            imageView4.setScaleType(ScaleType.FIT_XY);
            imageView4.setImageResource(R.drawable.banner05);
            
            ImageView imageView5 = new ImageView(getActivity());
            imageView5.setScaleType(ScaleType.FIT_XY);
            imageView5.setImageResource(R.drawable.banner06);
            

            arrayList.add(imageView0);
            arrayList.add(imageView1);
            arrayList.add(imageView2);
            arrayList.add(imageView3);
            arrayList.add(imageView4);
            arrayList.add(imageView5);
        initPoint();
    }
    
    public void initHeadImage() {
        
        initPagerChild();
        int sw = SystemHelper.getScreenInfo(getActivity()).widthPixels;
        int sh = SystemHelper.getScreenInfo(getActivity()).heightPixels;
        int h = 250;
        RelativeLayout.LayoutParams childLinerLayoutParames = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, h);
        viewPager.setLayoutParams(childLinerLayoutParames);

        viewPager.setAdapter(new ViewPagerAdapter(arrayList));
        draw_Point(0);// 默认首次进入


        /***
         * viewpager PageChangeListener
         */
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                draw_Point(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    
	  public class ViewPagerAdapter extends PagerAdapter {
	        // 界面列表
	        private List<View> views;

	        public ViewPagerAdapter(List<View> views) {
	            this.views = views;
	        }

	        // 销毁arg1位置的界面
	        @Override
	        public void destroyItem(View arg0, int arg1, Object arg2) {
	            ((ViewPager) arg0).removeView(views.get(arg1));
	        }

	        // 获得当前界面数
	        @Override
	        public int getCount() {
	            if (views != null) {
	                // 返回一个比较大的数字
	                return views.size();
	            }
	            return 0;
	        }

	        // 初始化arg1位置的界面
	        @Override
	        public Object instantiateItem(View arg0, int arg1) {
	            ((ViewPager) arg0).addView(views.get(arg1));
	            return views.get(arg1);
	        }

	        // 判断是否由对象生成界面
	        @Override
	        public boolean isViewFromObject(View arg0, Object arg1) {
	            return (arg0 == arg1);
	        }
	    }
	@Override
	public void onClick(View v) {
		layout1.setBackgroundColor(Color.WHITE);
		layout2.setBackgroundColor(Color.WHITE);
		layout3.setBackgroundColor(Color.WHITE);
		layout4.setBackgroundColor(Color.WHITE);
		Intent intent = new Intent(getActivity(),Item0ListViewActivity.class);
		Bundle b =new Bundle();
		switch (v.getId()) {
		case R.id.you_ly_tv_1:
			layout1.setBackgroundColor(Color.rgb(102, 204, 255));
			
			b.putString("key", "医院简介");
			intent.putExtras(b);
			startActivity(intent);
			
			break;
		case R.id.you_ly_tv_2:
			layout2.setBackgroundColor(Color.rgb(102, 204, 255));
			b.putString("key", "最新活动");
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.you_ly_tv_3:
			layout3.setBackgroundColor(Color.rgb(102, 204, 255));
			b.putString("key", "名医课堂");
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.you_ly_tv_4:
			layout4.setBackgroundColor(Color.rgb(102, 204, 255));
			b.putString("key", "医院地址");
			intent.putExtras(b);
			startActivity(intent);
			break;
		}

	}
}
