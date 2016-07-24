package com.jxust.asus.zhbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jxust.asus.zhbj.fragment.ContentFragment;
import com.jxust.asus.zhbj.fragment.LeftMenuFragment;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {

    private String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private String FRAGMENT_CONTENT = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);   // 设置侧边栏
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);    // 设置侧边栏的触发模式为全屏触摸
        slidingMenu.setBehindOffset(200);       // 设置屏幕预留面积

        initFragment();
    }


    // 初始化我们的Fragment,将fragment数据填充到布局文件
    public void initFragment(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();// 开启事务

        // replace中第一个参数表示布局文件的id，第二个参数表示要塞入到布局文件的fragment
        // 第三个参数的作用：这样就可以通过findFragmentByTag来寻找我们要寻找的fragment了
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),FRAGMENT_LEFT_MENU);  // 拿到fragment并且放到布局文件中
        transaction.replace(R.id.fl_content,new ContentFragment(),FRAGMENT_CONTENT);  // 拿到fragment并且放到布局文件中

        transaction.commit();   // 将事务提交

//        fm.findFragmentByTag(FRAGMENT_CONTENT);     // 通过标签来找fragment
    }


    // 获取侧边栏对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;
    }

    // 获取主页面对象
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }

}
