package com.jxust.asus.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jxust.asus.zhbj.MainActivity;
import com.jxust.asus.zhbj.R;

/**
 * Created by asus on 2016/7/21.
 * <p/>
 * 主页下5个子页面的基类
 *
 * @author Administrator
 * @time 2016/7/21 10:01
 */
public class BasePager {

    public Activity mActivity;
    public View mRootView;     // 布局对象

    public TextView tvTitle;    // 标题对象

    public FrameLayout flContent;   //内容

    public ImageButton btnMenu;     // 菜单按钮
    public ImageButton btnPhoto;   // 组图切换按钮

    public BasePager(Activity activity) {
        mActivity = activity;
        initView();
    }


    /**
     * 初始化布局
     */
    public void initView() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);

        btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();   // 切换状态，相当于开关，主要功能是:显示时隐藏，隐藏时显示

    }


    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置侧边栏开启或关闭
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;

        SlidingMenu slidingMenu = mainUI.getSlidingMenu();    // 获取MainActivity中的SlidingMenu对象
        // 设置如何的触摸方式来展现SlidingMenu
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

}
