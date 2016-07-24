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
 * 首页
 * @author Administrator
 * @time 2016/7/21 10:34
 */
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("Main","初始化我们的首页数据");
        tvTitle.setText("智慧北京");    // 修改标题

        btnMenu.setVisibility(View.GONE);       // 隐藏菜单按钮
        setSlidingMenuEnable(false);            // 不让侧边栏显示

        TextView text = new TextView(mActivity);
        text.setText("首页");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FramLayout中动态添加布局
        flContent.addView(text);

    }



}
