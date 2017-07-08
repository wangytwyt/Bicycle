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

import cn.bmob.newsmssdk.BmobSMS;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MyApplication extends MobApplication {
    public static NaviLatLng startLatlng = new NaviLatLng();

    public static double latitude, longitude;
    public static String city;
    private static AMapLocationClientOption mLocationOption;
    public static IRemoteService bleService;

    //声明AMapLocationClient类对象 29.810044 102.684203
    public static AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public static String road;

    public static User user ;


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

    public static void startLocation(Context context) {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(context);
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(10000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
        }
        mLocationClient.startLocation();
    }


    public static AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。获取当前经纬度
                    double latitude = amapLocation.getLatitude();//精度
                    double longitude = amapLocation.getLongitude();//维度

                    MyApplication.latitude = latitude;
                    MyApplication.longitude = longitude;
                    road = amapLocation.getRoad();


                    startLatlng = new NaviLatLng(latitude, longitude);

                    MyApplication.city = amapLocation.getCity();


                    mLocationClient.stopLocation();

                    LOG.E("-------------------", "定位成功");

//                    System.out.println("省："+arg0.getProvince());
//                    System.out.println("国家："+arg0.getCountry());
//                    System.out.println("经度"+arg0.getLatitude());
//                    System.out.println("纬度"+arg0.getLongitude());
//                    System.out.println("路是："+arg0.getRoad());

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

}
