package com.jxust.asus.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jxust.asus.zhbj.R;
import com.jxust.asus.zhbj.base.BasePager;
import com.jxust.asus.zhbj.base.impl.GovAffairsPager;
import com.jxust.asus.zhbj.base.impl.HomePager;
import com.jxust.asus.zhbj.base.impl.NewsCenterPager;
import com.jxust.asus.zhbj.base.impl.SettingPager;
import com.jxust.asus.zhbj.base.impl.SmartServicePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/20.
 * 这是我们的主页面
 *
 * @author Administrator
 * @time 2016/7/20 10:52
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup rgGroup;

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    private ArrayList<BasePager> mPageList;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content_menu, null);  // 拿到一个View对象
//        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        ViewUtils.inject(this, view);    // 这个的作用是和上面的那段代码是同样的效果的

        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);    // 默认勾选首页

        // 初始化5个子页面
        mPageList = new ArrayList<BasePager>();
       /* for(int i = 0;i < 5;i++){
            BasePager pager = new BasePager(mActivity);
            mPageList.add(pager);
        }*/

        mPageList.add(new HomePager(mActivity));
        mPageList.add(new NewsCenterPager(mActivity));
        mPageList.add(new SmartServicePager(mActivity));
        mPageList.add(new GovAffairsPager(mActivity));
        mPageList.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        // 设置监听RadioGroup的选择事件,
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // setCurrentItem有两个参数
                // 参数1表示的意思是要设置的页面
                // 参数2表示的是切换页面是否需要动画，默认为true为有动画，如果改为false则表示没有动画
                switch (checkedId){
                    case R.id.rb_home:
//                        mViewPager.setCurrentItem(0);         // 设置当前页面
                        mViewPager.setCurrentItem(0,false);     // 去掉切换页面的动画
                        break;
                    case R.id.rb_news:
//                        mViewPager.setCurrentItem(1);
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
//                        mViewPager.setCurrentItem(2);
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gov:
//                        mViewPager.setCurrentItem(3);
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
//                        mViewPager.setCurrentItem(4);
                        mViewPager.setCurrentItem(4,false);
                        break;
                }
            }
        });

        // 监听页面切换的相关操作，主要是用于初始化数据
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 页面被选中
            @Override
            public void onPageSelected(int position) {
                mPageList.get(position).initData();     // 获取当前被选中的页面，初始化该页面数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPageList.get(0).initData();        // 人工手动的初始化首页

    }

    /**
     * ViewPager的适配器
     */
    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化界面
        // 适配器中初始化界面有个特点：就是会预加载页面数据
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPageList.get(position);
            container.addView(pager.mRootView);
//            pager.initData();   // 初始化数据，不要放在此处初始化数据，否则会预加载下一个页面
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 获取新闻中心页面
     * @return
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPageList.get(1);
    }
}
