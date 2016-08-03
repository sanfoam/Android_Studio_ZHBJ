package com.jxust.asus.zhbj.util;

import android.content.Context;

/**
 * Created by asus on 2016/8/1.
 * 转换像素
 * @author Administrator
 * @time 2016/8/1 20:40
 */
public class DensityUtils {

    /**
     * dp转px
     */
    public static int dp2px(Context ctx,float dp){
        float density = ctx.getResources().getDisplayMetrics().density;// 获取设备密度

        // 将dp转px dp = px/设备密度
        int px = (int) (dp * density + 0.5f);   // 之所以要加0.5f的原因:加了0.5f以后就可以四舍五入
        return px;
    }

    /**
     * px转dp
     */
    public static float px2dp(Context ctx,float px){
        float density = ctx.getResources().getDisplayMetrics().density; // 获取设备密度

        float dp = px/density;
        return dp;
    }

}
