package com.jxust.asus.zhbj.util.bitmap;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by asus on 2016/7/31.
 * 内存缓存
 *
 * @author Administrator
 * @time 2016/7/31 10:09
 */
public class MemoryCacheUtils {

    // java默认为强引用
//    private HashMap<String,Bitmap> mMemoryCache = new HashMap<String,Bitmap>();
  /*  // 将其变成软引用
    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();*/

    // 使用LruCache来解决内存缓存的内存溢出的问题
    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory();// 动态获取最大的内存，模拟器默认是16M
        // 括号里表示的就是将内存控制在一定大小之内，如果超过了设定的控制值就会自动回收
        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 获取图片占据内存的大小
                int byteCount = value.getByteCount();

                return byteCount;
            }
        }; // 指定最大内存


    }

    /**
     * 从内存读
     *
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url) {
       /* // 这就是在软引用的情况下如何获取内存中的数据
        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
        if (softReference != null) {
            Bitmap bitmap = softReference.get();
            return bitmap;
        }
        return null;*/

        // 在LruCache的方式下如何获取内存中的数据
        return mMemoryCache.get(url);


//        return mMemoryCache.get(url);
    }

    /**
     * 写内存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
       /* // 这就是在软引用的情况下如果将数据写入内存中
        SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
        mMemoryCache.put(url, softReference);*/

        // 在LruCache的方式下如何数据写入内存中
        mMemoryCache.put(url,bitmap);

//        mMemoryCache.put(url,bitmap);
    }

}
