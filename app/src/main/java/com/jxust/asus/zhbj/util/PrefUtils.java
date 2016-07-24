package com.jxust.asus.zhbj.util;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus on 2016/7/17.
 * 这是SharePreferce封装
 *
 * @author Administrator
 * @time 2016/7/17 17:03
 */
public class PrefUtils {

    public static final String PREF_NAME = "config";

    public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

}
