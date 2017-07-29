package com.example.administrator.bicycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Personal.CertificationActivity;
import com.example.administrator.bicycle.Personal.Guide.GuideActivity;
import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Personal.InvitationActivity;
import com.example.administrator.bicycle.Personal.TripActivity;
import com.example.administrator.bicycle.Personal.qianbao.QianbaoActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.manageactivity.ManageActivity;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.zxing.camera.open.CaptureActivity;
import com.sofi.smartlocker.ble.util.LOG;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends Activity implements View.OnClickListener {

    private final int WT = 1;
    private final int SL = 2;
    private final int DY = 3;

    LinearLayout one, two, three, four, five, six;
    LinearLayout return1;//标题栏返回
    private OkHttpClient okHttpClient = new OkHttpClient();


    private ImageView iv_saomakaisuo, tv_weather;

    private TextView tv_city, tv_temperature, tv_sl, tv_day;

    final Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WT:
                    tv_temperature.setText((String) msg.obj);
                    typeWearher(msg.arg1);
                    break;
                case SL:
                    tv_sl.setText((String) msg.obj);
                    break;
                case DY:
                    tv_day.setText((String) msg.obj);
                    break;
            }

        }
    };

    private RelativeLayout rabackground;
    private  LinearLayout llbackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //   getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        //  getw(MyApplication.latitude, MyApplication.longitude);
    }

    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rabackground = (RelativeLayout) findViewById(R.id.rl_background);
        llbackground = (LinearLayout) findViewById(R.id.ll_background);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_weather = (ImageView) findViewById(R.id.tv_weather);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        iv_saomakaisuo = (ImageView) findViewById(R.id.iv_saomakaisuo);


        tv_sl = (TextView) findViewById(R.id.tv_sl);
        tv_day = (TextView) findViewById(R.id.tv_day);
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

        getWearher();
        getDay();
        getSl();

    }


    private void getWearher() {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.doGet("https://api.thinkpage.cn/v3/weather/daily.json?key=osoydf7ademn8ybv&location=" + MyApplication.city + "&language=zh-Hans&start=0&days=3", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String ss = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(ss);
                        JSONArray jsd = jsonObject.getJSONArray("results");

                        JSONObject jixc = new JSONObject(jsd.get(0).toString());
                        JSONArray jarry = jixc.getJSONArray("daily");
                        JSONObject jdaily = (JSONObject) jarry.get(0);
                        String wearher = jdaily.getString("text_day");
                        int code_day = jdaily.getInt("code_day");
                        int high = jdaily.getInt("high");
                        int low = jdaily.getInt("low");

                        Message msg = new Message();
                        msg.what = WT;
                        msg.arg1 = code_day;
                        msg.obj = low + "℃/" + high + "℃";
                        mhandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });


    }


    private void typeWearher(int type) {


        if (type >= 0 && type < 4) { //晴天
            tv_weather.setImageResource(R.drawable.p2);
            rabackground.setBackgroundResource(R.mipmap.bg_sunny_day);
            llbackground.setBackgroundResource(R.mipmap.sunny_day);
        } else if (type >= 4 && type < 10) { //多云
            tv_weather.setImageResource(R.drawable.p5);
            rabackground.setBackgroundResource(R.mipmap.bg_na);
            llbackground.setBackgroundResource(R.mipmap.bg_na_duoyun);
        } else if (type >= 10 && type < 20) { //雨
            tv_weather.setImageResource(R.drawable.p10);
            rabackground.setBackgroundResource(R.mipmap.bg_rain_day);
            llbackground.setBackgroundResource(R.mipmap.rain);
        } else if (type >= 20 && type < 26) { //雪
            tv_weather.setImageResource(R.drawable.p22);
            rabackground.setBackgroundResource(R.mipmap.bg_snow_day);
            llbackground.setBackgroundResource(R.mipmap.snow_day);
        } else if (type >= 26 && type < 30) { //沙尘暴
            rabackground.setBackgroundResource(R.mipmap.bg_haze);
            llbackground.setBackgroundResource(R.mipmap.wumai01);
            tv_weather.setImageResource(R.drawable.p31);
        } else if (type >= 30 && type < 32) { //雾霾
            tv_weather.setImageResource(R.drawable.p30);
            rabackground.setBackgroundResource(R.mipmap.bg_fog_day);
            llbackground.setBackgroundResource(R.mipmap.bg_fog);
        } else if (type >= 32 && type < 37) { //风
            rabackground.setBackgroundResource(R.mipmap.bg_sunny_day);
            llbackground.setBackgroundResource(R.mipmap.sunny_day);
            tv_weather.setImageResource(R.drawable.p33);
        } else if (type == 37) { //冷
            rabackground.setBackgroundResource(R.mipmap.bg_sunny_day);
            rabackground.setBackgroundResource(R.mipmap.bg_snow_day);
            llbackground.setBackgroundResource(R.mipmap.snow_day);
        } else if (type == 38) { //热
            rabackground.setBackgroundResource(R.mipmap.bg_sunny_day);
            llbackground.setBackgroundResource(R.mipmap.sunny_day);
            tv_weather.setImageResource(R.drawable.p38);
        } else if (type == 99) { //未知
            tv_weather.setImageResource(R.drawable.p99);
            rabackground.setBackgroundResource(R.mipmap.bg_normal);
            llbackground.setBackgroundResource(R.mipmap.normal);
        }
    }


    private void getSl() {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.doGet(Url.getSL + MyApplication.user.getT_USERPHONE(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String userjson = response.body().string();
                        JSONObject jsonObject = new JSONObject(userjson);
                        String result = jsonObject.getString("result");
                        if (result.equals("02")) {
                            JSONObject jspd = jsonObject.getJSONObject("pd");
                            int sl = jspd.getInt("T_USERCREDIT");
                            Message msg = new Message();
                            msg.what = SL;
                            msg.obj = sl + "分";
                            mhandler.sendMessage(msg);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getDay() {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.doGet(Url.getDay + MyApplication.user.getT_USERPHONE(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String userjson = response.body().string();
                        JSONObject jsonObject = new JSONObject(userjson);
                        String result = jsonObject.getString("result");
                        if (result.equals("02")) {
                            JSONObject jspd = jsonObject.getJSONObject("pd");
                            int day = jspd.getInt("T_DOWNTIME");
                            Message msg = new Message();
                            msg.what = DY;
                            msg.obj = day + "天";
                            mhandler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
            JSONArray hourly = jsondata.getJSONArray("hourly");
            JSONObject jj = (JSONObject) hourly.get(0);
            String wearher = jj.getString("condition");
            int temperature = jj.getInt("temp");
            sendHandler(wearher, temperature);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendHandler(String str, int tem) {
        Message msg = new Message();
        msg.what = tem;
        msg.obj = str;
        mhandler.sendMessage(msg);
    }


    private void getw(final double latitude, final double longitude) {
        if (NetWorkStatus.isNetworkAvailable(this)) {
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
        } else {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_one:
                startActivity(new Intent(HomeActivity.this, TripActivity.class));
                break;
            case R.id.lin_two:
                switch (MyApplication.user.getT_SIGN()) {
                    case 1:
                        startActivityForResult(new Intent(HomeActivity.this, ManageActivity.class), ContentValuse.deleteLogin);
                        break;
                    case 2:
                        startActivityForResult(new Intent(HomeActivity.this, InformationActivity.class), ContentValuse.deleteLogin);
                        break;
                }
                break;
            case R.id.lin_three:
                startActivity(new Intent(HomeActivity.this, RechargeActivity.class));

                break;
            case R.id.lin_four:
                startActivity(new Intent(HomeActivity.this, GuideActivity.class));
                break;
            case R.id.lin_five:
                startActivity(new Intent(HomeActivity.this, ReportingCenterActivity.class));
                break;
            case R.id.lin_six:
                startActivity(new Intent(HomeActivity.this, InvitationActivity.class));
                break;
            case R.id.iv_saomakaisuo:
                if (PermissionUtils.checkPermissionCamera(this)) {
                    toCaptureActivity();
                }
                break;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == ContentValuse.deleteLogin) {
            this.finish();
        }

    }

    private void toCaptureActivity() {
        startActivity(new Intent(HomeActivity.this, CaptureActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (PermissionUtils.onRequestPermissionsResultCamera(this, requestCode, permissions, paramArrayOfInt)) {
            toCaptureActivity();
        }

    }
}
