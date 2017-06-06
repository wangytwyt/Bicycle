package com.example.administrator.bicycle;

import android.app.Application;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.model.NaviLatLng;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MyApplication extends Application {
    public static NaviLatLng startLatlng = new NaviLatLng();

    public static double latitude, longitude;
    public static String city;

    @Override
    public void onCreate() {
        super.onCreate();


    }


}
