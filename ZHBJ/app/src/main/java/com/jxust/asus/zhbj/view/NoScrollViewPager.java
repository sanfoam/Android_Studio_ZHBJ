package com.jxust.asus.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by asus on 2016/7/21.
 * 不能左右划的ViewPager
 * @author Administrator
 * @time 2016/7/21 14:19
 */
public class NoScrollViewPager extends ViewPager{


    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 表示事件是否拦截,返回false表示不拦截，可以让嵌套在内部的ViewPager相应左右滑事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }

    /**
     * 重写onTouchEvent事件，什么都不用做
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return false;
    }
}
