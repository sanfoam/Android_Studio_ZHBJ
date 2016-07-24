package com.jxust.asus.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jxust.asus.zhbj.base.BasePager;

/**
 * Created by asus on 2016/7/21.
 * 设置页面
 * @author Administrator
 * @time 2016/7/21 10:34
 */
public class SettingPager extends BasePager {

    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("Main","初始化我们的设置数据");
        tvTitle.setText("设置");

        btnMenu.setVisibility(View.GONE);       // 隐藏菜单按钮
        setSlidingMenuEnable(false);        // 关闭侧边栏

        TextView text = new TextView(mActivity);
        text.setText("设置");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FramLayout中动态添加布局
        flContent.addView(text);

    }

}
