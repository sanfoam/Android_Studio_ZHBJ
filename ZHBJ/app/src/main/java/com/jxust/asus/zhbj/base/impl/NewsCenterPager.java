package com.jxust.asus.zhbj.base.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jxust.asus.zhbj.MainActivity;
import com.jxust.asus.zhbj.base.BaseMenuDetailPager;
import com.jxust.asus.zhbj.base.BasePager;
import com.jxust.asus.zhbj.base.menudetail.InteractMenuDetailPager;
import com.jxust.asus.zhbj.base.menudetail.NewsMenuDetailPager;
import com.jxust.asus.zhbj.base.menudetail.PhotoMenuDetailPager;
import com.jxust.asus.zhbj.base.menudetail.TopicMenuDetailPager;
import com.jxust.asus.zhbj.domain.NewsData;
import com.jxust.asus.zhbj.fragment.LeftMenuFragment;
import com.jxust.asus.zhbj.global.GlobalContants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/21.
 * 新闻中心
 *
 * @author Administrator
 * @time 2016/7/21 10:34
 */
public class NewsCenterPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mPagers; // 4个菜单详情页的集合
    private NewsData mNewsData;
    private boolean flag = true;       // 用来判断是否是刚进应用

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("Main", "初始化我们的新闻数据");
        setSlidingMenuEnable(true);     // 打开侧边栏
        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();

        // 使用xUtils发送请求
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {

            // 访问成功，在主线程运行
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;    // 得到的结果
//              System.out.println("返回结果" + result);
                parseData(result);      // 解析网络数据
            }

            // 访问失败，在主线程运行
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();    // 打印失败日志
            }
        });
    }

    /**
     * 解析网络数据
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        // gson的第一个参数表示的是要解析的json数据
        // 第二个参数表示的是要转成什么对象
        mNewsData = gson.fromJson(result, NewsData.class);
//        System.out.println("解析结果:" + mNewsData);

        // 刷新侧边栏数据
        MainActivity mainUI = (MainActivity) mActivity;
        // 通过MainActivity中的getLeftMenuFragment来获取侧边栏对象
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData); // 将数据传递给侧边栏

        // 准备4个菜单详情页
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        if (flag) {
            setCurrentMenuDetailPager(0);   // 设置菜单详情页，新闻为默认当前页
            flag = false;
        }

    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mPagers.get(position);  // 获取当前菜单详情页
        flContent.removeAllViews();                         // 清除之前的布局
        flContent.addView(pager.mRootView);                 // 将菜单详情页的布局文件设置给FrameLayout布局

        // 获取当前页的数据
        NewsData.NewsMenuData menuData = mNewsData.data.get(position);
        // 设置当前页的标题
        tvTitle.setText(menuData.title);

        pager.initData();   // 初始化当前页面的数据
    }

}
