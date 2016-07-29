package com.jxust.asus.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jxust.asus.zhbj.R;

import java.util.Date;

/**
 * Created by asus on 2016/7/26.
 * 下拉刷新的ListView
 *
 * @author Administrator
 * @time 2016/7/26 17:22
 */
public class RefreshListView extends ListView implements OnScrollListener, AdapterView.OnItemClickListener {

    private static final int STATE_PULL_REFRESH = 0;    // 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1; // 松开刷新
    private static final int STATE_REFRESHING = 2;      // 正在刷新
    OnRefreshListener mListener;
    OnItemClickListener mItemClickListener;
    private int mCurrentState = STATE_PULL_REFRESH;     // 当前状态
    private int startY = -1;
    private int endY;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private View mFooterView;
    private int mFooterViewHeight;
    private boolean isLoadingMore = false;  // 默认没在加载

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }


    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);  // 将我们新建的布局添加到ListView中

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        // 隐藏头布局
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0); // 负的值就让控件向上移动了
        initArrowAnim();
        // 更新最后刷新时间
        tvTime.setText("最后刷新时间" + getCurrentTime());
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);

        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);       // 隐藏脚布局

        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {   // 表示滑动到最后
                System.out.println("到底了....");
                mFooterView.setPadding(0, 0, 0, 0);
                setSelection(getCount() - 1);   // 该表listView的显示位置
                isLoadingMore = true;   // 将状态变成正在加载

                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    /**
     * 监听用户的触摸操作
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {   // 确保startY有效
                    startY = (int) ev.getRawY();
                }

                if (mCurrentState == STATE_REFRESHING) {  // 正在刷新时，不做处理
                    break;
                }

                endY = (int) ev.getRawY();

                int dy = endY - startY; // 移动偏移量

                if (dy > 0 && getFirstVisiblePosition() == 0) {   // 只有下拉并且当前是第一个item才允许下拉
                    int padding = dy - mHeaderViewHeight;   // 计算padding
                    mHeaderView.setPadding(0, padding, 0, 0); // 设置当前padding

                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {  // 状态改为松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {  // 改为下拉刷新状态
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;    // 重置

                if (mCurrentState == STATE_RELEASE_REFRESH) { // 如果是松开刷新状态就变成正在刷新状态
                    mCurrentState = STATE_REFRESHING;   // 正在刷新
                    // 将状态的padding变成0
                    mHeaderView.setPadding(0, 0, 0, 0);    // 显示
                    refreshState();
                } else if (mCurrentState == STATE_PULL_REFRESH) {
                    // 将隐藏标题
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);

                ivArrow.startAnimation(animDown);   // 箭头向下
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);

                ivArrow.startAnimation(animUp);   // 箭头向上
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新");
                // 必须要先清除动画以后才能够去把那个箭头给隐藏
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();
                }

                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        // 箭头向上的动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向下的动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 收起下拉刷新的控件
     */
    public void onRefreshComplete(boolean success) {
        if (isLoadingMore) {      // 如果正在加载更多
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);   // 隐藏脚布局
            isLoadingMore = false;
        } else {
            mCurrentState = STATE_PULL_REFRESH; // 将刷新状态变成默认的状态
            tvTitle.setText("下拉刷新");
            ivArrow.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.INVISIBLE);

            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);   // 隐藏HeaderView

            if (success) {
                tvTime.setText("最后刷新时间" + getCurrentTime());
            }
        }
    }

    /**
     * 获取当前的时间
     */
    public String getCurrentTime() {
        // 设置时间的输出格式
        // 需要注意的是：HH表示的是24小时制，hh表示12小时制
        // 需要注意的是：月的MM表示是1月份从1开始，mm表示的是一月份从0开始
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this); // 将自身传递给底层，所以会将相应的操作交给onItemClick方法
        mItemClickListener = listener;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mItemClickListener != null) {
            // 这样子在TabDetailPager中获取点击事件的位置就会自动减2
            // getHeaderViewsCount()的作用就是获取到Header个数
            mItemClickListener.onItemClick(adapterView, view, position - getHeaderViewsCount(), id);
        }
    }

    public interface OnRefreshListener {


        public void onRefresh();

        public void onLoadMore();   // 加载更多数据
    }

}
