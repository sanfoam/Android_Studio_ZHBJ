package com.jxust.asus.zhbj.util;

import android.content.Context;

/**
 * Created by asus on 2016/7/29.
 * 缓存工具类
 * @author Administrator
 * @time 2016/7/29 19:45
 */
public class CacheUtils {

    /**
     * 设置缓存
     * key 就是url,value就是json数据
     */
    public static void setCache(String key, String json, Context context){
        PrefUtils.setString(context,key,json);
        // 可以将缓存放在文件中，文件名就是Md5(url),文件内容是json
    }

    /**
     * 获取缓存
     * key 就是url
     */
    public static String getCache(String key, Context context){
        return PrefUtils.getString(context,key,null);
    }
}
