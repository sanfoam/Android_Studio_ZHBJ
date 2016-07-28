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

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发,请求父控件及祖宗控件是否拦截事件
     * 1.右滑，而且是第一个页面，需要父控件拦截
     * 2.左滑，而且是最后一个页面，需要父控件拦截
     * 3.上下滑也是需要父控件拦截的
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);   // 请求父控件不要拦截,这样是为了保证ACTION_MOVE

                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {  // 左右滑动
                    if (endX > startX) {  // 往右滑
                        //判断当前是否是第一个页面
                        if (getCurrentItem() == 0) {  // 第一个页面，需要父控件拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {    // 往左滑
                        if (getCurrentItem() == getAdapter().getCount() - 1) {  // 最后一个页面，需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {    // 上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);   // 请求父控件要拦截
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
