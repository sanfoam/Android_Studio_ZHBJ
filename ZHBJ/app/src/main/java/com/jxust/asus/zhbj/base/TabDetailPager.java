package com.jxust.asus.zhbj.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jxust.asus.zhbj.NewsDetailActivity;
import com.jxust.asus.zhbj.R;
import com.jxust.asus.zhbj.domain.NewsData;
import com.jxust.asus.zhbj.domain.TabData;
import com.jxust.asus.zhbj.global.GlobalContants;
import com.jxust.asus.zhbj.util.CacheUtils;
import com.jxust.asus.zhbj.util.PrefUtils;
import com.jxust.asus.zhbj.view.RefreshListView;
import com.jxust.asus.zhbj.view.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/22.
 * <p/>
 * 页签详情页
 *
 * @author Administrator
 * @time 2016/7/22 21:56
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    NewsData.NewsTabData mTabData;
    private String mUrl;
    private TabData mTabDetailData;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;   // 头条新闻的标题

    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator; // 头条新闻位置指示器

    @ViewInject(R.id.lv_list)
    private RefreshListView lvList;        // 新闻列表

    private ArrayList<TabData.TopNewsData> mTopNewsList;        // 头条新闻数据集合
    private ArrayList<TabData.TabNewsData> mNewsList;          // 新闻数据集合
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;            // 更多页面的地址

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        // 加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_head_topnews, null);


        ViewUtils.inject(this, view);          // 对xUtils进行注入操作
        ViewUtils.inject(this, headerView);    // 对xUtils进行注入操作
        // 由于在ViewPager中又绑定一个indicator，
        // 所以在设置滑动监听的时候要在indicator中设置监听
//        mViewPager.setOnPageChangeListener(this);   // 设置滑动监听

        // 将头条新闻以头布局的形式加载给ListView
        lvList.addHeaderView(headerView);

        // 设置下拉刷新监听
        lvList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();    // 从服务端获取更多的数据
                } else {
                    Toast.makeText(mActivity, "最后一页了!", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);    // 收起加载更多的布局
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                System.out.println("被点击:" + position);

                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = mNewsList.get(position).id;
                if (!ids.contains(readId)) {   // 如果字符串中不包含这个id就将其加进ids中
                    ids = ids + readId + ",";
                    PrefUtils.setString(mActivity, "read_ids", ids);
                }

//                mNewsAdapter.notifyDataSetChanged();
                changeReadState(view);  // 实现局部页面刷新,这个view就是被点击的Item的布局对象

                // 跳转到新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }


    /**
     * 改变已读的颜色
     */
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);

        if (!TextUtils.isEmpty(cache)) {  // 如果内存中有缓存就先调用内存中的缓存
            parseData(cache, false); // 解析json数据
        }
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
                parseData(result, false);
                lvList.onRefreshComplete(true); // 在获取到服务器的数据后收起下拉刷新的控件

                // 设置缓存
                CacheUtils.setCache(mUrl, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();    // 打印失败日志
                lvList.onRefreshComplete(false); // 在获取到服务器的数据后收起下拉刷新的控件
            }

        });
    }


    /**
     * 加载更多的数据
     */
    public void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result, true);
                lvList.onRefreshComplete(true); // 在获取到服务器的数据后收起下拉刷新的控件
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();    // 打印失败日志
                lvList.onRefreshComplete(false); // 在获取到服务器的数据后收起下拉刷新的控件
            }

        });
        mNewsAdapter.notifyDataSetChanged();
    }


    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);  // 解析页面详情页的数据
        System.out.println("页签详情页解析:" + mTabDetailData);

        // 处理更多页面的逻辑
        String more = mTabDetailData.data.more;
        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVER_URL + more;
        } else {
            mMoreUrl = null;
        }

        if (!isMore) {
            mTopNewsList = mTabDetailData.data.topnews;

            mNewsList = mTabDetailData.data.news;

            if (mTopNewsList != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                // 设置头条新闻的指示器
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);   // 支持快照显示
                // 因为我们在原先的ViewPager的基础上又绑定了一个indicator，所以我们要在indicator中设置监听才有效
                mIndicator.setOnPageChangeListener(this);   // 给indicator设置监听

                mIndicator.onPageSelected(0);   // 主动设置到第0页，也就是在每次回到这个ViewPager的时候指示器重新定位到第一个点

                tvTitle.setText(mTopNewsList.get(0).title);     // 将标题设置给ViewPager里的tvTitle
            }
            if (mNewsList != null) {
                // 填充新闻列表数据
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

            // 自动轮播条显示
            if (mHandler == null) {
                mHandler = new Handler() {

                    private int currentItem;

                    @Override
                    public void handleMessage(Message msg) {

                        currentItem = mViewPager.getCurrentItem();

                        if (currentItem < mTopNewsList.size() - 1) {   // 说明还有下一页
                            currentItem++;  // 跳到下一个页面
                        } else {
                            currentItem = 0;
                        }
                        // TODO 新建了一个线程，从而避免了此操作抢线程,但是有个问题，就是自动轮播没有用了
                        // TODO 主要原因就是Handler线程和Thread之间没有进行数据的交换
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                // 在另外一个线程中处理,否则会报错java.lang.IllegalStateException
                                mViewPager.setCurrentItem(currentItem);     // 切换到下一个页面
                            }
                        }.interrupt();
                        mHandler.sendEmptyMessageDelayed(0, 3000);  // 延时3秒后发消息,形成循环
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000);  // 这样子就会隔3秒以后自动跳到上面的handlerMessage里面
            }
        } else {    // 如果是加载下一页，需要将数据追加给原来的集合
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);     // 将第二页的数据追加给集合
            mNewsAdapter.notifyDataSetChanged();    // 刷新一下
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String title = mTopNewsList.get(position).title;
        tvTitle.setText(title);     // 将标题设置给ViewPager里的tvTitle
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;
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
            image.setScaleType(ImageView.ScaleType.FIT_XY);     // 基于控件大小来填充图片

            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            utils.display(image, topNewsData.topimage);  // 传递imageView对象和图片地址

            container.addView(image);

            image.setOnTouchListener(new TopNewsTouchListener());   // 设置触摸监听
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 头条新闻的触摸监听
     */
    class TopNewsTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("按下");
                    mHandler.removeCallbacksAndMessages(null);  // 删除Handler的所有消息
                    break;
                case MotionEvent.ACTION_CANCEL:         // 移动事件被取消
                    System.out.println("事件取消");
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                    break;
                case MotionEvent.ACTION_UP:
                    System.out.println("抬起");
                    mHandler.sendEmptyMessageDelayed(0, 3000);   // 让Handler继续发消息
                    break;

            }
            return true;
        }
    }

    /**
     * 新闻列表的适配器
     */
    class NewsAdapter extends BaseAdapter {

        private final BitmapUtils mUtils;

        public NewsAdapter() {
            mUtils = new BitmapUtils(mActivity);
            mUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);   // 设置还没加载出网络图片时的默认图片
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position);

            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);

            mUtils.display(holder.ivPic, item.listimage);  // 将图片加载到iv_pic中

            String ids = PrefUtils.getString(mActivity, "read_ids", "");
            if (ids.contains(getItem(position).id)) { // 如果用户点击过这个新闻，就将新闻颜色变成灰色的
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {        // 如果用户没有点击过这个新闻，就将这个新闻变成黑色
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

}