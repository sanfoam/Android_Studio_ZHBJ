package com.jxust.asus.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/24.
 *
 * 页签详情页数据,这里的字段必须要和服务器中的字段一致，方便字段解析s
 * @author Administrator
 * @time 2016/7/24 14:55
 */
public class TabData {

    public int retCode;
    public TabDetail data;


    public class TabDetail{
        public String title;
        public String more;
        public ArrayList<TabNewsData> news;
        public ArrayList<TopNewsData> topnews;

        @Override
        public String toString() {
            return "TabDetail{" +
                    "title='" + title + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }
    }


    /**
     * 新闻列表新闻
     */
    public class TabNewsData{
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TabNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    /**
     * 头条新闻
     */
    public class TopNewsData{
        public String id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TopNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TabData{" +
                "data=" + data +
                '}';
    }
}
