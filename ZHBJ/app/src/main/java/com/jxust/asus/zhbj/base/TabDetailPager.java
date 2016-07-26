package com.jxust.asus.zhbj.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jxust.asus.zhbj.R;
import com.jxust.asus.zhbj.domain.NewsData;
import com.jxust.asus.zhbj.domain.TabData;
import com.jxust.asus.zhbj.global.GlobalContants;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by asus on 2016/7/22.
 * <p/>
 * 页签详情页
 *
 * @author Administrator
 * @time 2016/7/22 21:56
 */
public class TabDetailPager extends BaseMenuDetailPager {

    NewsData.NewsTabData mTabData;
    private String mUrl;
    private TabData mTabDetailData;

    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        ViewUtils.inject(this, view);    // 对xUtils进行注入操作
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    /**
     * 从服务器中获取数据
     *
     * @return
     */
    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("这是页签详情页:" + result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();    // 打印失败日志
            }

        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);  // 解析页面详情页的数据
        System.out.println("页签详情页解析:" + mTabDetailData);
        mViewPager.setAdapter(new TopNewsAdapter());
        mTabDetailData.data.topnews.size();
    }

    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {

        private BitmapUtils utils;

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.topnews_item_default);         // 设置默认加载的图片
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setImageResource(R.mipmap.topnews_item_default);
            image.setScaleType(ImageView.ScaleType.FIT_XY);     // 基于控件大小来填充图片s

            TabData.TopNewsData topNewsData = mTabDetailData.data.topnews.get(position);
            utils.display(image, topNewsData.topimage);  // 传递imageView对象和图片地址

            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
