package com.jxust.asus.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/21.
 * <p/>
 * 网络分类信息的封装
 * <p/>
 * 字段名字必须和服务器返回的字段名一致，方便字段解析
 *
 * @author Administrator
 * @time 2016/7/21 20:21
 */
public class NewsData {

    public int retCode;
    public ArrayList<NewsMenuData> data;

    /**
     * 侧边栏数据对象
     */
    public class NewsMenuData {
        public String id;
        public String title;
        public int type;
        public String url;

        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }


    /**
     * 新闻页面下11个子页签的数据对象
     */
    public class NewsTabData {
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }
}
