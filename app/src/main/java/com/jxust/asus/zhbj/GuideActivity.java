package com.jxust.asus.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jxust.asus.zhbj.util.PrefUtils;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/16.
 *
 * @author Administrator
 * @time 2016/7/16 16:35
 */
public class GuideActivity extends Activity {

    private static final int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    private ViewPager vpGuide;

    private LinearLayout llPointGroup;

    ArrayList<ImageView> mImageViewList;

    private int mPointWidth;        // 两个圆点之间的距离

    private View viewRedPoint;      // 小红点

    private Button btnStart;        // 开始体验s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        initView();     // 初始化界面
        initData();
    }

    private void initData() {
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.setOnPageChangeListener(new GuidePageListener());
        viewRedPoint = findViewById(R.id.view_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {    // 监听点击事件

            @Override
            public void onClick(View view) {
                // 更新sp，表示已经展示了新手引导
                PrefUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);

                // 跳转主页面
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    /**
     * ViewPager的滑动监听
     */
    class GuidePageListener implements ViewPager.OnPageChangeListener {

        // 页面滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //  System.out.println("当前位置:" + position + ",百分比:" + positionOffset + ",移动距离:" + positionOffsetPixels);
            int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;     // 红点所要移动的距离

            // 获取红点的布局参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
            params.leftMargin = len;    // 这是左边距

            viewRedPoint.setLayoutParams(params);       // 重新给小红点设置布局参数

        }

        // 页面被选中事件
        @Override
        public void onPageSelected(int position) {
            if (position == mImageIds.length - 1) {     // 最后一个页面
                btnStart.setVisibility(View.VISIBLE);
            } else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * 初始化界面
     */
    private void initView() {
        mImageViewList = new ArrayList<ImageView>();

        // 初始化引导页的3个页面
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);  // 设置引导页背景
            mImageViewList.add(image);
        }

        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);   // 引导圆点的父控件

        // 初始化引导页的小圆点
        for (int i = 0; i < mImageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);   // 设置引导页默认圆点

            // 父控件是什么类型的，里面的LayoutParams就用什么类型的
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);    // 限制小圆点的尺寸

            if (i > 0) {
                params.leftMargin = 10;         // 设置圆点间隔
            }

            point.setLayoutParams(params);      // 设置圆点的尺寸

            llPointGroup.addView(point);        // 将圆点添加到线性布局

        }


        // 拿到视图树，对layout结束事件的监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            // 当layout执行结束后回调此方法
            @Override
            public void onGlobalLayout() {
                //    System.out.println("layout 结束");
                llPointGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);  // 删除监听

                // 由于获取圆点的X坐标需要在布局全部准备好才行，所以要使用视图树
                // measure(测量布局大小) layout(界面位置) onDraw
                mPointWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();

                //                System.out.println("圆点距离:" + mPointWidth);
            }
        });

    }


    /**
     * ViewGroup的数据适配器
     */
    class GuideAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
