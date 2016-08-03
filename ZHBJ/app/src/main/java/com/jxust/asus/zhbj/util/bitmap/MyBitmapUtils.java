package com.jxust.asus.zhbj.util.bitmap;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jxust.asus.zhbj.R;

/**
 * Created by asus on 2016/7/30.
 * 自定义图片加载工具
 *
 * @author Administrator
 * @time 2016/7/30 20:20
 */
public class MyBitmapUtils {

    NetCacheUtils mNetCacheUtils;
    LocalCacheUtils mLocalCacheUtils;
    MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }

    public void display(ImageView ivPic, String url) {
        ivPic.setImageResource(R.mipmap.news_pic_default);      // 设置默认加载图片

        Bitmap bitmap = null;

        // 从内存读
        bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
        if(bitmap != null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从内存中读取图片");
            return;
        }

        // 如果在内存中没有读取到就再从本地读
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap != null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从本地读取图片");
            mMemoryCacheUtils.setBitmapToMemory(url, bitmap);    // 将图片从本地缓存保存到内存缓存中
            return;
        }
        // 如果在本地中也没有读取到就从网络读
        mNetCacheUtils.getBitmapFromNet(ivPic, url);
    }

}
