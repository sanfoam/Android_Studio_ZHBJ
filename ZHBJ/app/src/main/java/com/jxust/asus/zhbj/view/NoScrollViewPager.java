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
     * 返回false表示不拦截，将Touch事件继续传递给子控件的onInterceptTouchEvent方法
     * 返回true表示拦截，将Touch事件交到当前控件的onTouchEvent方法中
     * 在这里返回false表示不处理，直接到View控件中处理
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
     * 重写onTouchEvent事件
     * 返回值决定了当前控件是否消费了这个事件
     * 如果返回true表示消费了这个事件
     * 如果返回false表示没有消费这个事件
     * 区别就在于，一般操作是包含ACTION_DOWN、ACTION_UP、ACTION_MOVE
     * 但是关键就在于，ACTION_UP、ACTION_MOVE都是要在ACTION_DOWN在消费完了以后才可以传到父控件来消费的
     * 举个例子来说明：如果在消费完了ACTION_DOWN后，传递给父控件，而父控件return false
     * 则对应的ACTION_UP和ACTION_MOVE则需要交给父控件的父控件来判断是否要消费这个事件
     * @param ev
     * @return
     */
    @Override
        public boolean onTouchEvent(MotionEvent ev) {

//        return super.onTouchEvent(ev);
        return false;
    }
}
