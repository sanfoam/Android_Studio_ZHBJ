package com.jxust.asus.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jxust.asus.zhbj.MainActivity;
import com.jxust.asus.zhbj.R;
import com.jxust.asus.zhbj.base.impl.NewsCenterPager;
import com.jxust.asus.zhbj.domain.NewsData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/20.
 * <p/>
 * 这是我们的侧边栏
 *
 * @author Administrator
 * @time 2016/7/20 10:52
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lvList;
    private ArrayList<NewsData.NewsMenuData> mMenuList;

    private int mCurrentPos;        // 当前被点击的菜单项
    private MenuAdapter menuAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);// 拿到View对象
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mCurrentPos = position;                 // 获取哪个选项被点了
                menuAdapter.notifyDataSetChanged();     // 刷新Adapter

                setCurrentMenuDetailPager(position);

                toggleSlidingMenu();   // 表示隐藏SlidingMenu
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
     * 设置当前菜单详情页
     * @param position
     */
    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUI = (MainActivity) mActivity;
        ContentFragment fragment = mainUI.getContentFragment();// 获取主页面fragment
        NewsCenterPager pager = fragment.getNewsCenterPager();    // 获取新闻中心页面
        pager.setCurrentMenuDetailPager(position);      // 设置当前菜单详情
    }


    // 设置网络数据
    public void setMenuData(NewsData data) {
//        Log.i("main", "侧边栏拿到数据了：" + data);
        mMenuList = data.data;
        menuAdapter = new MenuAdapter();
        lvList.setAdapter(menuAdapter);

    }


    /**
     * 侧边栏数据适配器
     */
    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        // 方便我们去拿到对象
        @Override
        public NewsData.NewsMenuData getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            view = View.inflate(mActivity, R.layout.list_menu_item, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            NewsData.NewsMenuData newsMenuData = getItem(position);
            tvTitle.setText(newsMenuData.title);

            if(mCurrentPos == position){    // 当前绘制的view是否被选中
                // 如果是被点击的显示红色
                tvTitle.setEnabled(false);      // 说明已被点击，不可再被点击
            }else {
                // 如果是没被点击的显示白色
                tvTitle.setEnabled(true);       // 说明没被点击，可以被用户点击
            }

            return view;
        }
    }

}
