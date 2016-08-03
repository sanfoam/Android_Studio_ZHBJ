package com.jxust.asus.zhbj.base.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jxust.asus.zhbj.R;
import com.jxust.asus.zhbj.base.BaseMenuDetailPager;
import com.jxust.asus.zhbj.domain.PhotosData;
import com.jxust.asus.zhbj.global.GlobalContants;
import com.jxust.asus.zhbj.util.CacheUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by asus on 2016/7/22.
 *
 * 菜单详情页-组图
 * @author Administrator
 * @time 2016/7/22 15:48
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

    private ListView lvPhoto;
    private GridView gvPhoto;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private PhotoAdapter mAdapter;
    private ImageButton btnPhoto;

    public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);

        this.btnPhoto = btnPhoto;
        // 监听用户点击事件
        this.btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDisplay();    // 切换展现方式
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager,null);

        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        String cache = CacheUtils.getCache(GlobalContants.PHOTO_URL, mActivity);

        if(TextUtils.isEmpty(cache)){

        }
        getDatafromServer();
    }

    /**
     * 获取服务器端数据
     */
    public void getDatafromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                System.out.println("返回结果:"+result);
                parseData(result);

                // 设置缓存
                CacheUtils.setCache(GlobalContants.PHOTO_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity,msg,Toast.LENGTH_LONG).show();
                error.printStackTrace();    // 打印错误信息
            }
        });
    }

    /**
     * 解析数据
     * @param result
     */
    private void parseData(String result) {
        Gson gson  = new Gson();
        PhotosData photosData = gson.fromJson(result, PhotosData.class);
        mPhotoList = photosData.data.news;  // 获取组图列表集合

        if(mPhotoList != null) {
            mAdapter = new PhotoAdapter();
            lvPhoto.setAdapter(mAdapter);
            gvPhoto.setAdapter(mAdapter);
        }
    }

    class PhotoAdapter extends BaseAdapter{

        private BitmapUtils utils;

//        private MyBitmapUtils utils;

        public PhotoAdapter (){
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.news_pic_default);
//            utils = new MyBitmapUtils();
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotosData.PhotoInfo getItem(int i) {
            return mPhotoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int positon, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mActivity,R.layout.list_photo_item,null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            PhotosData.PhotoInfo item = getItem(positon);
            holder.tvTitle.setText(item.title);
            utils.display(holder.ivPic,item.listimage);   // 从url中获取图片，然后通过Xutil来赋给holder.ivPic从而显示出来
            return convertView;
        }
    }
    static class ViewHolder{
        public TextView tvTitle;
        public ImageView ivPic;
    }

    private boolean isListDisplay = true;   // 是否是列表展示
    /**
     * 切换展现方式
     */
    private void changeDisplay(){
        if(isListDisplay){      // 现在是列表展示方式，要转换成网格展示方式
            isListDisplay = false;
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setImageResource(R.mipmap.icon_pic_list_type); // 将界面的图标换成列表的图标
        } else {    // 现在是网格展示方式，要转换成列表展示方式
            isListDisplay = true;
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);
            btnPhoto.setImageResource(R.mipmap.icon_pic_grid_type); // 将界面的图标换成网格的图标
        }
    }
}
