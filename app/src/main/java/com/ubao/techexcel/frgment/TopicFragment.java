package com.ubao.techexcel.frgment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ub.service.activity.MyOrderActivity;
import com.ub.service.activity.ServiceDetailActivity;
import com.ub.service.activity.TeamActivity;
import com.ub.techexcel.adapter.CurrentTeamAdapter;
import com.ub.techexcel.adapter.ServiceAdapter;
import com.ub.techexcel.bean.ServiceBean;
import com.ubao.techexcel.R;
import com.ubao.techexcel.info.Customer;

import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends MyFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {


    private RecyclerView serviceListView1, serviceListView2;
    private List<String> mList1 = new ArrayList<String>(),
            mList2 = new ArrayList<String>();

    private TextView allServiceTv, serviceStatusTv;

    private TextView underline;

    private ViewPager mViewPager;

    private List<View> mViews = new ArrayList<View>();
    private LayoutInflater mInflater;
    private CurrentTeamAdapter currentTeamAdapter1, currentTeamAdapter2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topicfragment, container,
                false);

        initView(view);
        initViewPager();
        InitImageView();

        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (isPrepared && isVisible) {  //isPrepared 可见在onCreate之前执行
            if (!isLoadDataFinish) {
                isLoadDataFinish = true;
                initData();
            }
        }
    }

    private void initData() {

        mList1.add("");
        mList1.add("");
        mList1.add("");
        currentTeamAdapter1 = new CurrentTeamAdapter(getActivity(), mList1);
        serviceListView1.setAdapter(currentTeamAdapter1);

        mList2.add("");
        mList2.add("");
        mList2.add("");
        mList2.add("");
        currentTeamAdapter2 = new CurrentTeamAdapter(getActivity(), mList2);
        serviceListView2.setAdapter(currentTeamAdapter2);

    }


    private void initView(View view) {

        allServiceTv = (TextView) view.findViewById(R.id.allService);
        serviceStatusTv = (TextView) view.findViewById(R.id.serviceStatus);

        underline = (TextView) view.findViewById(R.id.underline);

        allServiceTv.setTextColor(getResources().getColor(R.color.c1));

        allServiceTv.setOnClickListener(this);
        serviceStatusTv.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mInflater = LayoutInflater.from(getActivity());

        View view1 = mInflater.inflate(R.layout.tabone1, null);
        View view2 = mInflater.inflate(R.layout.tabtwo2, null);
        serviceListView1 = (RecyclerView) view1.findViewById(R.id.serviceList);
        serviceListView2 = (RecyclerView) view2.findViewById(R.id.serviceList);

        mViews.clear();
        mViews.add(view1);
        mViews.add(view2);

    }

    private void initViewPager() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mViews.get(position));
                return mViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        });
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0, true);
        currIndex = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allService:
                setHeadColor((TextView) view);
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.serviceStatus:
                setHeadColor((TextView) view);
                mViewPager.setCurrentItem(1, true);
                break;
            default:
                break;
        }
    }

    /**
     * PagerAdapter 设置head颜色
     *
     * @param view
     */
    @SuppressLint("NewApi")
    private void setHeadColor(TextView view) {
        allServiceTv.setTextColor(getResources().getColor(R.color.c5));
        serviceStatusTv.setTextColor(getResources().getColor(R.color.c5));
        view.setTextColor(getResources().getColor(R.color.c1));
    }


    int one;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号

    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        one = screenW / 2;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        Animation animation = null;
        switch (arg0) {
            case 0:
                setHeadColor(allServiceTv);
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                }
                break;
            case 1:
                setHeadColor(serviceStatusTv);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                }
                break;
            default:
                break;
        }
        currIndex = arg0;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(200);
        underline.startAnimation(animation);
    }
}
