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

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context ,String key,String defaultValue){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }

    public static void setString(Context context ,String key ,String value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

}
