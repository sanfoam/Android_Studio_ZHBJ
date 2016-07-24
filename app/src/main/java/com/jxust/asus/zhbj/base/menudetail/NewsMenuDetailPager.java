package com.jxust.asus.zhbj.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jxust.asus.zhbj.R;
import com.jxust.asus.zhbj.base.BaseMenuDetailPager;
import com.jxust.asus.zhbj.base.TabDetailPager;
import com.jxust.asus.zhbj.domain.NewsData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/22.
 * <p/>
 * 菜单详情页-新闻
 *
 * @author Administrator
 * @time 2016/7/22 15:48
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager mViewPager;

    private ArrayList<TabDetailPager> mPagerList;

    private ArrayList<NewsData.NewsTabData> mNewsTabData;       // 页签网络数据
    private TabPageIndicator mIndicator;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_menu_details, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);

        // 对ViewUtils进行注入
        ViewUtils.inject(this,view);

        // 初始化自定义控件TabPageIndicator
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        mPagerList = new ArrayList<TabDetailPager>();

        // 初始化页签数据
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData.get(i));
            mPagerList.add(pager);
        }

        mViewPager.setAdapter(new MenuDetailAdapter());

        // 将ViewPager和mIndicator关联起来
        mIndicator.setViewPager(mViewPager); // 必须在ViewPager设置完adapter后才能调用
    }

    // 跳转到一个页面
    @OnClick(R.id.btn_next)
    public void nextPager(View view) {
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }

    class MenuDetailAdapter extends PagerAdapter {

        /**
         * 重写getPageTitle方法，返回页面标题，用于ViewPagerIndicator的页签的显示
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;

        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化界面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();

            return pager.mRootView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
