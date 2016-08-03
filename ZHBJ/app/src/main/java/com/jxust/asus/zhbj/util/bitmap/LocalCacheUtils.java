package com.jxust.asus.zhbj.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jxust.asus.zhbj.util.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by asus on 2016/7/31.
 * 本地缓存
 *
 * @author Administrator
 * @time 2016/7/31 9:27
 */
public class LocalCacheUtils {

    public static final String CACHE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";

    /**
     * 从本地sdcard读图片
     *
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url) {
        try {
            String fileName = MD5Encoder.encode(url);   // 拿到图片的名字

            File file = new File(CACHE_PATH, fileName);
            if (file.exists()) {  // 表示本地存在此文件，即有本地缓存
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;        // 表示没有本地缓存
    }

    /**
     * 向sdcard写图片
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);

            File file = new File(CACHE_PATH, fileName);  // 会创建一个文件夹，以fileName为文件名

            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {   // 如果文件夹不存在，创建文件夹
                parentFile.mkdirs();
            }
            // 将图片保存在本地
            // 第一个参数表示的是压缩格式，第二个参数表示的是压缩品质，第三个参数表示的是输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
