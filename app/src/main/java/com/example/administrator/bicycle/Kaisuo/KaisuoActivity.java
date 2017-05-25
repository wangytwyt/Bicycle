package com.example.administrator.bicycle.Kaisuo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.bicycle.R;
import com.sofi.smartlocker.ble.BleService;
import com.sofi.smartlocker.ble.interfaces.IRemoteCallback;
import com.sofi.smartlocker.ble.interfaces.IRemoteService;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KaisuoActivity extends AppCompatActivity {

    TextView tvDate, text;
    ProgressBar jindutiao;
    String data,str;

    int type = 100;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaisuo);
        i = 0;
        tvDate = (TextView) findViewById(R.id.tv_date);
        text = (TextView) findViewById(R.id.text);
        jindutiao = (ProgressBar) findViewById(R.id.jindutiao);

        yjks();


    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
        finish();
        Log.d("finish", "ok");
    }

    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
    //88888888888888888888888888888888888888888开锁信息88888888888888888888888888888888888888888888888888888888888

    public void yjks() {

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    iRemoteService = IRemoteService.Stub.asInterface(service);
                    iRemoteService.registerCallback(stub);


                    Intent intentt = getIntent();
                    str = intentt.getStringExtra("result");//str即为回传的值
                    String jisukaisuo = intentt.getStringExtra("jisukaisuo");
                    data = intentt.getStringExtra("data");
                    try {
                        if (str != null && !str.equals("")) {
                            Log.d("straaa", str);
                            type = 0;
//                Login(str);
                            if (iRemoteService != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            iRemoteService.connectLock(str);
                                            Log.d("执行成功", "执行成功");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                        if (jisukaisuo != null && !jisukaisuo.equals("")) {
                            type = 1;
                            if (iRemoteService != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            iRemoteService.startBleScan();
                                            Log.d("执行成功", "执行成功");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }

                        if (data != null && !data.equals("")) {
                            type = 0;
                            if (iRemoteService != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            iRemoteService.connectLock(data);
                                            Log.d("执行成功", "执行成功");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        initService();
    }


    private CookieManager cookieManager = new CookieManager();
    OkHttpClient client;
    IRemoteService iRemoteService;
    ServiceConnection connection;
    public static final MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");
    //    String url = "https://alabike.luopingelec.com/alabike/ab_mapp";
    String url = "http://42.159.113.21/heibike/lock/getlock.mvc";

    private void initService() {

        Intent intent = new Intent(this, BleService.class);
        //  Intent bind = IntentUtil.getExplicitIntent(this, intent);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);


    }


    private void httpPostJson(String macKey, String keySource, int timestamp) throws Exception {

        //client对象，核心类

//        PRent p = new PRent(macKey, keySource, timestamp);
//        RequestBody requestbody = RequestBody.create(JSON1, p.getPStr());
//        Log.i("requestssss", p.getPStr());


        //post表单参数
        FormBody.Builder builder = new FormBody.Builder();

        builder.add("keySource", keySource);
        Log.d("keySource", keySource);
        //创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();


        OkHttpClient client = provideOkHttpClient(KaisuoActivity.this, cookieManager);


//
//        //用requestBuilder可以添加header
//        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestbody);


//        final Request request = requestBuilder.build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //子线程中
                Log.d("requestssssErr", e + "");

            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("requestssss1", str);
                //子线程中

                Info result = JSON.parseObject(str, Info.class);
//                Info result = entity.getInfo();
                String keys = result.getKeys();
                int encryptionKey = result.getEncryptionKey();
                int time = result.getServerTime();


                try {

                    iRemoteService.openLock("15239470236", time, keys, encryptionKey);
                    Log.d("openLock", "执行");

                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    //登录
    private void Login(final String name) throws Exception {
        client = provideOkHttpClient(KaisuoActivity.this, cookieManager);
        //登录

        PUserLogin login = new PUserLogin("12345678999", "aaaaaa");
        RequestBody requestbody = RequestBody.create(JSON1, login.getPStr());
        //用requestBuilder可以添加header
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestbody);


        final Request request = requestBuilder.build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //子线程中
                Log.d("requestssssErr", e + "");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("requestssssdenglu", str);

                if (type == 0) {


                    //子线程
                    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                    if (atomicBoolean.compareAndSet(false, true)) {
                        if (iRemoteService != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        iRemoteService.connectLock(name);
                                        Log.d("执行成功", "执行成功");
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
                if (type == 1) {
                    try {
                        iRemoteService.startBleScan();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

    }


    public OkHttpClient provideOkHttpClient(Context context, CookieManager cookieManager) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(Constants.SOCKET_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(Constants.SOCKET_TIME_OUT, TimeUnit.SECONDS);
        builder.cookieJar(cookieManager);

        X509TrustManager trustManager = provideTrustManager();
        SSLSocketFactory sslSocketFactory = provideSSLFactory(trustManager);
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        }

        File cacheDirectory = new File(context.getCacheDir(), Constants.DEFAULT_CACHE_DIR);
        Cache cache = new Cache(cacheDirectory, Constants.cachSize);
        builder.cache(cache);

        return builder.build();
    }


    private X509TrustManager provideTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
    }

    private SSLSocketFactory provideSSLFactory(TrustManager trustManager) {
        // Create a trust manager that does not validate certificate chains
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    IRemoteCallback.Stub stub = new IRemoteCallback.Stub() {
        @Override
        public void bleSupportFeature(boolean isFeature) throws RemoteException {
        }

        @Override
        public void bleEnable(boolean enable) throws RemoteException {

        }

        //该函数用于扫描周边的蓝牙锁设备，找到后在回调函数bleScanResult中给出蓝牙锁的地址信息、信号强度信息
        @Override
        public void bleScanResult(String name, final String address, int rssi) throws RemoteException {


            AtomicBoolean atomicBoolean = new AtomicBoolean(false);

            if (atomicBoolean.compareAndSet(false, true)) {
                if (iRemoteService != null) {
                    //开锁
                    iRemoteService.connectLock(address);

                }
            }

            Log.d("bleScanResult", address);
        }


        @Override
        public void bleStatus(boolean status, String address) throws RemoteException {

            Log.d("bleStatus", status + "," + address);


            if (status) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            iRemoteService.getLockStatus(0, 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

            }
        }

        @Override
        public void bleCmdError(int cmd, String msg) throws RemoteException {
            Log.d("失败", cmd + "，" + msg);
//            iRemoteService.getLockRecord();
            setText(cmd + msg);
        }

        @Override
        public void bleGetBike(String version, String keySerial, String mac, String vol) throws RemoteException {
            Log.d("bleGetBike", version + "," + keySerial + "," + mac + "," + vol);


            try {
                if (i == 0) {
                    i++;
                    httpPostJson(mac, keySerial, DateToTimestamp(new Date()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void bleGetRecord(String phone, String bikeTradeNo, String timestamp, String transType, String mackey, String index, String Cap, String Vol) throws RemoteException {
            Log.d("bleGetRecord", phone + "," + bikeTradeNo + "," + timestamp + "," + transType + "," + mackey + "," + index + "," + Cap + "," + Vol);
            iRemoteService.delLockRecord(bikeTradeNo);
        }

        @Override
        public void bleCmdReply(int cmd) throws RemoteException {

            Log.d("成功", cmd + "");
            setText(cmd + "");


//            iRemoteService.getLockRecord();

        }
    };


    /**
     * Date类型转换为10位时间戳
     *
     * @param time
     * @return
     */
    public static Integer DateToTimestamp(Date time) {
        Timestamp ts = new Timestamp(time.getTime());

        return (int) ((ts.getTime()) / 1000);
    }


    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 123) {
                String str = (String) msg.obj;
                tvDate.setText(str);
                jindutiao.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
//                try {
//                    iRemoteService.getLockRecord();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
            }


        }
    };


    public void setText(final String cmd) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 123;
                String a = cmd + "";

                message.obj = cmd;
                h.sendMessage(message);


            }
        }).start();


    }
}
