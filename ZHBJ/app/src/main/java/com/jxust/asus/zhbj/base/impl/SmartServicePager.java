package com.jxust.asus.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.jxust.asus.zhbj.base.BasePager;

/**
 * Created by asus on 2016/7/21.
 * 智慧服务
 * @author Administrator
 * @time 2016/7/21 10:34
 */
public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("Main","初始化我们的生活数据");
        tvTitle.setText("生活");

        setSlidingMenuEnable(true);     // 打开侧边栏

        TextView text = new TextView(mActivity);
        text.setText("智慧服务");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FramLayout中动态添加布局
        flContent.addView(text);

    }

}
