package com.jxust.asus.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by asus on 2016/7/24.
 * 11个页签水平滑动的ViewPager,暂时不用
 * @author Administrator
 * @time 2016/7/24 12:01
 */
public class HorizontalViewPager extends ViewPager {

    public HorizontalViewPager(Context context, AttributeSet attrs) {
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
        if(getCurrentItem() != 0) { // 如果不是第一个页面，这不要显示侧边栏
            getParent().requestDisallowInterceptTouchEvent(true);   // 请求父控件不要拦截
        } else {        // 如果是第一个页面，这要显示侧边栏
            getParent().requestDisallowInterceptTouchEvent(false);  // 请求父控件拦截
        }
        return super.dispatchTouchEvent(ev);
    }

}
