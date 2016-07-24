package com.jxust.asus.zhbj.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by asus on 2016/7/20.
 *fragment基类
 * @author Administrator
 * @time 2016/7/20 10:07
 */
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;

    // fragment的创建
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    // 处理fragment的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initViews();
    }

    // 依附的activity创建完成
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    // 初始化界面,子类必须实现初始化布局的方法
    public abstract View initViews();

    // 初始化数据，可以不实现
    public void initData(){

    }

}
