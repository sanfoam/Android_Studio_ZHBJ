package com.jxust.asus.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by asus on 2016/7/22.
 *
 * 菜单详情页基类
 * @author Administrator
 * @time 2016/7/22 15:43
 */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;

    public View mRootView;  // 根布局对象

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }

    /**
     * 初始化界面
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public void initData(){

    }

}
