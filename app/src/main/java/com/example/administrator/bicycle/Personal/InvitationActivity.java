package com.example.administrator.bicycle.Personal;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

import java.util.HashMap;


import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatResp;

public class InvitationActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("邀请好友");

        findViewById(R.id.iv_QZone).setOnClickListener(this);
        findViewById(R.id.iv_weixin).setOnClickListener(this);
        findViewById(R.id.iv_qq).setOnClickListener(this);
        findViewById(R.id.iv_wechatmoments).setOnClickListener(this);
        findViewById(R.id.iv_weibo).setOnClickListener(this);


    }


    private void showShareQZone() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("测试分享的标题");
        sp.setTitleUrl("http://www.suning.com/?utm_source=union&utm_medium=C&utm_campaign=1025&utm_content=1021"); // 标题的超链接
        sp.setText("测试分享的文本");
        sp.setImageData(BitmapFactory.decodeResource(this.getResources(),R.mipmap.logo));
        sp.setSite("玩");
        sp.setVenueDescription("测试分享dddd的文本");
        sp.setSiteUrl("http://www.suning.com/?utm_source=union&utm_medium=C&utm_campaign=1025&utm_content=1021");

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //分享成功的回调
            }

            public void onCancel(Platform arg0, int arg1) {
                //取消分享的回调
            }
        });
// 执行图文分享
        qzone.share(sp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_QZone:
                showShareQZone();
                break;
            case R.id.iv_weixin:
                shareWechat();
                break;
            case R.id.iv_qq:
                qq();
                break;
            case R.id.iv_wechatmoments:
                shareWechatMoments();
                break;
            case R.id.iv_weibo:
                weiBoshare();
                break;

        }
    }
    private void shareWechatMoments(){
        Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        weixin.share(initShareParams());
    }

    private void shareWechat(){
    
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        weixin.share(initShareParams());
    }

    private Platform.ShareParams initShareParams() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("测试分享的标题");
        sp.setTitleUrl("http://www.suning.com/?utm_source=union&utm_medium=C&utm_campaign=1025&utm_content=1021"); // 标题的超链接
        sp.setText("测试分享的文本");
        sp.setImageData(BitmapFactory.decodeResource(this.getResources(),R.mipmap.logo));
        sp.setSite("玩");
        sp.setUrl("http://www.suning.com/?utm_source=union&utm_medium=C&utm_campaign=1025&utm_content=1021");
        sp.setVenueDescription("测试分享dddd的文本");
        sp.setSiteUrl("http://www.suning.com/?utm_source=union&utm_medium=C&utm_campaign=1025&utm_content=1021");
        sp.setShareType(Platform.SHARE_WEBPAGE);
        return sp;
    }

    private void qq() {

        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        qq.share(initShareParams());
    }

    private void weiBoshare() {

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText("测试分享的文本");
        sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");

        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);

    }
}

