package com.example.administrator.bicycle;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.model.NaviLatLng;
import com.example.administrator.bicycle.entity.User;
import com.example.administrator.bicycle.util.AccountKey;
import com.mob.MobApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sofi.smartlocker.ble.interfaces.IRemoteService;
import com.sofi.smartlocker.ble.util.LOG;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.bmob.newsmssdk.BmobSMS;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MyApplication extends MobApplication {
    public static NaviLatLng startLatlng = new NaviLatLng();


    public  static boolean islock = false;

    public static double latitude, longitude;
    public static String city;
    private static AMapLocationClientOption mLocationOption;
    public static IRemoteService bleService;

    //声明AMapLocationClient类对象 29.810044 102.684203
    public static AMapLocationClient mLocationClient = null;


    public static User user ;


    public static IWXAPI msgApi;


    @Override
    public void onCreate() {
        super.onCreate();



        //短信验证码初始化
        BmobSMS.initialize(this, AccountKey.Bmob_Application_ID);

        initImageLoader(getApplicationContext());

    }

    public static boolean isLogin(){
        if(user == null){
            return false;
        }else {
            return  true;
        }
    }

    /**
     * 初始化图片加载类配置信息
     **/
    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)//加载图片的线程数
                .denyCacheImageMultipleSizesInMemory() //解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//设置磁盘缓存文件名称
                .tasksProcessingOrder(QueueProcessingType.LIFO)//设置加载显示图片队列进程
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }



}
