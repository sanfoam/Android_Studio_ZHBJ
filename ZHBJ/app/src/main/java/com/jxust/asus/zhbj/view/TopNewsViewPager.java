package com.jxust.asus.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by asus on 2016/7/24.
 * 头条新闻的ViewPager
 *
 * @author Administrator
 * @time 2016/7/24 12:01
 */
public class TopNewsViewPager extends ViewPager {

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发,请求父控件及祖宗控件是否拦截事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);   // 请求父控件不要拦截
        return super.dispatchTouchEvent(ev);
    }

}
