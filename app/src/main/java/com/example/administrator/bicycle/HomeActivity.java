package com.example.administrator.bicycle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.bicycle.Personal.Guide.GuideActivity;
import com.example.administrator.bicycle.Personal.Guide.ReportActivity;
import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Personal.InvitationActivity;
import com.example.administrator.bicycle.Personal.TripActivity;
import com.example.administrator.bicycle.Personal.qianbao.QianbaoActivity;
import com.example.administrator.bicycle.Personal.yonhuxieyi.YhxyActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.administrator.bicycle.manageactivity.ManageActivity;
import com.example.administrator.bicycle.zxing.utils.CaptureActivity;
import com.sofi.smartlocker.ble.util.LOG;
import com.sofi.smartlocker.ble.util.StringUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout one, two, three, four, five, six;
    LinearLayout return1;//标题栏返回
    private OkHttpClient okHttpClient = new OkHttpClient();
    //声明AMapLocationClient类对象
//    public AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    public AMapLocationListener mLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation amapLocation) {
//            if (amapLocation != null) {
//                if (amapLocation.getErrorCode() == 0) {
//                    //可在其中解析amapLocation获取相应内容。
//                    //通过经纬度获取天气
//                    getw(amapLocation.getLatitude(), amapLocation.getLongitude());
//                } else {
//                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                    Log.e("AmapError", "location Error, ErrCode:"
//                            + amapLocation.getErrorCode() + ", errInfo:"
//                            + amapLocation.getErrorInfo());
//                }
//            }
//        }
//    };
//
//    //声明AMapLocationClientOption对象
//    public AMapLocationClientOption mLocationOption = null;


    private ImageView iv_saomakaisuo;

    private TextView tv_city, tv_weather, tv_temperature;

    final Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String dd = msg.what+"℃";
            tv_weather.setText((String)msg.obj);
            tv_temperature.setText(dd);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        init();

//
//        //初始化定位
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
//
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(true);
//        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
//        //关闭缓存机制
//        mLocationOption.setLocationCacheEnable(false);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();


        //      getw(MyApplication.getLatitude(), amapLocation.getLongitude());
        getw(MyApplication.latitude, MyApplication.longitude);
    }

    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        iv_saomakaisuo = (ImageView)findViewById(R.id.iv_saomakaisuo);


        one = (LinearLayout) findViewById(R.id.lin_one);
        two = (LinearLayout) findViewById(R.id.lin_two);
        three = (LinearLayout) findViewById(R.id.lin_three);
        four = (LinearLayout) findViewById(R.id.lin_four);
        five = (LinearLayout) findViewById(R.id.lin_five);
        six = (LinearLayout) findViewById(R.id.lin_six);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        iv_saomakaisuo.setOnClickListener(this);

        tv_city.setText(MyApplication.city);

    }


    /*
     * 获取24小时天气信息
     */
    void post(String url, String lat, String lon) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("lat", lat)
                .add("lon", lon)
                .add("token", "1b89050d9f64191d494c806f78e8ea36")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Authorization", "APPCODE 09d7e8c73a914f0aa287f6891553b792")
                .post(formBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String str = response.body().string();
            JsonResponse(str);

        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private void JsonResponse(String str) {
        try {
            JSONObject JSONObject = new JSONObject(str);
            String data = JSONObject.getString("data");
            JSONObject jsondata = new JSONObject(data);
            JSONArray hourly =jsondata.getJSONArray("hourly");
            JSONObject jj = (JSONObject) hourly.get(0);
            String wearher =  jj.getString("condition");
            int temperature =jj.getInt("temp");
            sendHandler(wearher,temperature);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
private void sendHandler(String str,int tem){
    Message msg = new Message();
    msg.what = tem;
    msg.obj = str;
    mhandler.sendMessage(msg);
}

    private void getw(final double latitude, final double longitude) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                 post("http://aliv8.data.moji.com/whapi/json/aliweather/forecast24hours", latitude + "", longitude + "");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_one:
                startActivity(new Intent(HomeActivity.this, TripActivity.class));
                break;
            case R.id.lin_two:
                //           startActivity(new Intent(HomeActivity.this, InformationActivity.class));
        startActivity(new Intent(HomeActivity.this, ManageActivity.class));
                break;
            case R.id.lin_three:

                startActivity(new Intent(HomeActivity.this, InvitationActivity.class));

                break;
            case R.id.lin_four:
                startActivity(new Intent(HomeActivity.this, GuideActivity.class));
                break;
            case R.id.lin_five:
                startActivity(new Intent(HomeActivity.this, ReportingCenterActivity.class));
                break;
            case R.id.lin_six:
                startActivity(new Intent(HomeActivity.this, QianbaoActivity.class));
                break;

            case R.id.iv_saomakaisuo:
                startActivity(new Intent(HomeActivity.this, CaptureActivity.class));
                finish();
                break;

        }


    }
}
