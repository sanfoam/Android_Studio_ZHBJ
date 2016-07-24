package com.jxust.asus.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jxust.asus.zhbj.base.BaseMenuDetailPager;

/**
 * Created by asus on 2016/7/22.
 *
 * 菜单详情页-组图
 * @author Administrator
 * @time 2016/7/22 15:48
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

    public PhotoMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        TextView text = new TextView(mActivity);
        text.setText("菜单详情页-组图");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        return text;
    }
}
