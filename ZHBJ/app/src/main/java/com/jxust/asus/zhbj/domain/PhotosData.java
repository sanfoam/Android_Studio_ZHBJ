package com.jxust.asus.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/30.
 * 组图数据
 * @author Administrator
 * @time 2016/7/30 10:12
 */
public class PhotosData {

    public int retcode;
    public PhotosInfo data;

    public class PhotosInfo{
        public String title;
        public ArrayList<PhotoInfo> news;
    }

    public class PhotoInfo{
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
