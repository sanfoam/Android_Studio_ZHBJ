package com.jxust.asus.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.jxust.asus.zhbj.util.PrefUtils;

public class SplashActivity extends Activity {

    RelativeLayout mrlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mrlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        startAnim();
    }


    /**
     * 开启动画
     */
    private void startAnim(){

        // 动画集合
        AnimationSet set = new AnimationSet(false);

        // 旋转的动画
        RotateAnimation rotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(2000);       // 动画时间
        rotate.setFillAfter(true);      // 保持动画状态

        // 缩放的动画
        ScaleAnimation scale = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(2000);
        scale.setFillAfter(true);

        // 渐变的动画
        AlphaAnimation alpha = new AlphaAnimation(0,1);     //  0表示完全透明，1表示完全不透明
        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);

        mrlRoot.startAnimation(set); // 将动画运行起来

        // 设置动画的监听器
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画开始的时候调用
            @Override
            public void onAnimationStart(Animation animation) {

            }

            // 动画结束的时候调用
            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }

            // 动画重复的时候调用
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 跳转下一个页面
     */
    private void jumpNextPage(){

        //判断之前有没有新手引导
        boolean userGuide = PrefUtils.getBoolean(this,"is_user_guide_showed",false);

        if(!userGuide) {
            // 跳转到新手引导页
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
        }else{
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }

        finish();
    }

}
