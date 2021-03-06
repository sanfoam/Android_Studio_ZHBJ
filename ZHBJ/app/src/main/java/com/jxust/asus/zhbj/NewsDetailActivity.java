package com.jxust.asus.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by asus on 2016/7/29.
 * 新闻详情页
 *
 * @author Administrator
 * @time 2016/7/29 9:26
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {


    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnSize;
    private ImageButton btnShare;
    private ProgressBar pbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);

        btnBack.setOnClickListener(this);
        btnSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        String url = getIntent().getStringExtra("url");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);    // 表示支持js
        settings.setBuiltInZoomControls(true);  // 表示显示放大缩小按钮
        settings.setUseWideViewPort(true);      // 表示支持双击缩放

        mWebView.setWebViewClient(new WebViewClient() {
            // 在网页开始加载的回调这个方法
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("网页开始加载");
                pbProgress.setVisibility(View.VISIBLE); // 将进度条可见
            }

            // 在网页加载结束的时候回调这个方法
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                pbProgress.setVisibility(View.GONE);    // 将进度条不可见
            }

            // 跳转的链接都会在此方法中回调
           /* @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest webUrl) {
                String url = webUrl.toString();
                System.out.println("跳转url:" + url);
                view.loadUrl(url);  // 每次跳转都会强制让我们的url加载,这样就避免了系统调用系统的浏览器来打开网页
                return true;
//            return super.shouldOverrideUrlLoading(view, request);
            }*/

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // 进度发生变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println("加载进度:" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // 获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("网页标题:" + title);
                super.onReceivedTitle(view, title);
            }
        });

//        mWebView.loadUrl("http://www.baidu.com");  // 加载网页
        mWebView.loadUrl(url);  // 加载网页
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
        }
    }

    private int mCurrentChooseItem;     // 记录当前选中的item,点击确定前
    private int mCurrentItem = 2;       // 记录当前选中的item,点击确定后

    /**
     * 显示选择对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{"超大号", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setTitle("字体设置");
        // 第二个参数的意思是默认选择的项目
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                System.out.println("选中:" + which);
                mCurrentChooseItem = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                WebSettings settings = mWebView.getSettings();// 拿到webView的设置
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentItem = mCurrentChooseItem;
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 分享,在sdcard的根目录放test.jpg
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTheme(OnekeyShareTheme.CLASSIC);     // 设置主题

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
