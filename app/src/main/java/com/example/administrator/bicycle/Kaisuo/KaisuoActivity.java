package com.example.administrator.bicycle.Kaisuo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.bicycle.MainActivity;
import com.example.administrator.bicycle.Post.PostUtil;
import com.example.administrator.bicycle.R;

import com.example.administrator.bicycle.util.Dialog;
import com.example.administrator.bicycle.util.Globals;

import com.luopingelec.permission.PermissionsManager;
import com.luopingelec.permission.PermissionsResultAction;
import com.sofi.smartlocker.ble.BleService;
import com.sofi.smartlocker.ble.interfaces.IRemoteCallback;
import com.sofi.smartlocker.ble.interfaces.IRemoteService;
import com.sofi.smartlocker.ble.util.LOG;
import com.squareup.otto.Subscribe;


import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

import static android.content.Context.BIND_AUTO_CREATE;

public class KaisuoActivity extends AppCompatActivity {
    private final String TAG = "---------------------";
    String url = "https://alabike.luopingelec.com/alabike/ab_mapp";
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 11002;
    TextView tvDate, text;
    ProgressBar jindutiao;
    String data, str, jisukaisuo;


    int type = 100;
    int i = 0;

    // protected  ApplicationState mState;

    private AtomicBoolean connect = new AtomicBoolean(false);

    private BluetoothSocket BTSocket;
    private BluetoothAdapter BTAdapter;
    private BluetoothDevice device;
    private double lontitude = 0, latitude = 0;


    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LOG.E(TAG, "onServiceConnected");
            try {
                Constants.bleService = IRemoteService.Stub.asInterface(service);
                //   Constants.bleService.startBleScan();
                Constants.bleService.registerCallback(mCallback);

                connectLock(str, jisukaisuo, data);

                Constants.bleService.setHighMode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LOG.E(TAG, "onServiceDisconnected");
            try {
                if (Constants.bleService != null) {
                    Constants.bleService.unregisterCallback(mCallback);
                    Constants.bleService = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    IRemoteCallback.Stub mCallback = new IRemoteCallback.Stub() {
        @Override
        public void bleEnable(boolean enable) throws RemoteException {

        }


        @Override
        public void bleSupportFeature(boolean isFeature) throws RemoteException {
//            Globals.isBleFeature = isFeature;
//            showBleTipDialog();
        }

        @Override
        public void bleScanResult(String name, String address, int rssi) throws RemoteException {
            LOG.E(TAG, "bleScanResult name:" + name + " address:" + address + " bikeNo:" + Globals.bikeNo
                    + " rssi:" + rssi);
            //该函数用于扫描周边的蓝牙锁设备，找到后在回调函数bleScanResult中给出蓝牙锁的地址信息、信号强度信息


        }

        @Override
        public void bleStatus(boolean status, String address) throws RemoteException {
            LOG.E(TAG, "bleStatus :" + status + " address :" + address);
            //连接成功失败信息

            if (status) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {

                            Constants.bleService.getLockStatus(lontitude, latitude);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

            }

        }

        @Override
        public void bleCmdError(int cmd, String errorMsg) throws RemoteException {
            LOG.E(TAG, "bleCmdError :" + cmd + " msg:" + errorMsg);

            send(errorMsg);
        }

        @Override
        public void bleGetBike(String version, String keySerial, String mac, String vol) throws RemoteException {
            LOG.E(TAG, "bleGetBike version:" + version + " keySerial:" + keySerial + " mac:" + mac + " vol:" + vol);
            try {
                httpPostJson(mac, keySerial, DateToTimestamp(new Date()));
            } catch (Exception e) {
            }



        }

        @Override
        public void bleGetRecord(String phone, String bikeTradeNo, String timestamp, String transType,
                                 String mackey, String index, String Cap, String Vol) throws RemoteException {
            LOG.E(TAG, "bleGetRecord");

        }

        @Override
        public void bleCmdReply(int cmd) throws RemoteException {
            LOG.E(TAG, "bleCmdReply :" + cmd);

          send("已开锁");
        }
    };
private void send(String data){
    Message msg = h.obtainMessage();
    msg.what = 123;
    msg.obj = data;
    h.sendMessage(msg);
}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaisuo);
        i = 0;
        tvDate = (TextView) findViewById(R.id.tv_date);
        text = (TextView) findViewById(R.id.text);
        jindutiao = (ProgressBar) findViewById(R.id.jindutiao);

        getintent();
        try {
            Login();
        } catch (Exception e) {

        }


    }

    private void getintent() {
        Intent intentt = getIntent();
        str = intentt.getStringExtra("result");//str即为回传的值
        jisukaisuo = intentt.getStringExtra("jisukaisuo");
        data = intentt.getStringExtra("data");
    }


    private void connectLock(final String str, String jisukaisuo, final String data) {
        try {
            if (str != null && !str.equals("")) {
                Log.d("straaa", str);
                type = 0;
                //                Login(str);
                if (Constants.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Constants.bleService.connectLock(str);

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } else if (jisukaisuo != null && !jisukaisuo.equals("")) {
                type = 1;
                if (Constants.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Constants.bleService.startBleScan();

                                Log.d("执行成功", "执行成功");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } else if (data != null && !data.equals("")) {
                type = 0;
                if (Constants.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Constants.bleService.connectLock(data);

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
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.bleService != null) {
            showBleTipDialog();
        } else {
            requestLocationPermission();
        }

    }

    private void initService() {
        Intent intent = new Intent(this, BleService.class);
        //  Intent bind = IntentUtil.getExplicitIntent(this, intent);
        startService(intent);
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }


    private void showBleTipDialog() {
        try {
//            if (Globals.BLE_INIT && Globals.isBleFeature) {
            if (!Constants.bleService.isBleEnable()) {
                Dialog.showBleDialog(this, R.string.ble_tip, bleListener, bleNeverListener);
            }

//            } else if (Globals.BLE_INIT) {
//                Dialog.showAlertDialog(this, R.string.ble_feature_tip);
//            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙提示监听
     */
    DialogInterface.OnClickListener bleListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            dialog.cancel();
        }
    };


    /**
     * 蓝牙提示不再提醒
     */
    DialogInterface.OnClickListener bleNeverListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Globals.BLE_INIT = false;
            dialog.cancel();
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


    private void requestLocationPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        initService();

                    }

                    @Override
                    public void onDenied(String permission) {
                        String message = String.format(Locale.getDefault(), getString(R.string.message_denied), permission);
                        Dialog.showToast(KaisuoActivity.this, message);
                    }
                }
        );
    }


//    private void phonePermission() {
//
////        if (Build.VERSION.SDK_INT >= 23) {
//            //校验是否已具有模糊定位权限
//            if (ContextCompat.checkSelfPermission(KaisuoActivity.this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(KaisuoActivity.this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//            } else {
//                //具有权限
//                initService();
//            }
////        } else {
////            //系统不高于6.0直接执行
////            initService();
////        }
//
//    }

//
//   @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
//            grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限
//                initService();
//            } else {
//                // 权限拒绝
//                // 下面的方法最好写一个跳转，可以直接跳转到权限设置页面，方便用户
//                Toast.makeText(this, "请打开手机蓝牙权限", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
////        unbindService(connection);
//        finish();
//        Log.d("finish", "ok");
//    }

    //888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
    //88888888888888888888888888888888888888888开锁信息88888888888888888888888888888888888888888888888888888888888

    //        public void yjks() {
//
//
//        connection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                try {
//                    iRemoteService = IRemoteService.Stub.asInterface(service);
//                    iRemoteService.registerCallback(stub);
//                    Intent intentt = getIntent();
//                    str = intentt.getStringExtra("result");//str即为回传的值
//                    String jisukaisuo = intentt.getStringExtra("jisukaisuo");
//                    data = intentt.getStringExtra("data");
//                    // isBleEnable该函数用于判断手机是否已经打开蓝牙功能，在IPhone手机上需要通过UI提醒用户打开蓝牙功能，否则不能租车，在Android手机上可以直接调用EnableBLE函数打开蓝牙
//                    if (iRemoteService.isBleEnable()) {
//                        connectLock(str, jisukaisuo, data);
//                    } else {
//                        //enableBle该函数用于打开手机的蓝牙通讯功能，IOS不支持该函数，必须由用户手工打开，在Android上执行该函数后打开蓝牙设备
//                        iRemoteService.enableBle();
//                        //该函数用于扫描周边的蓝牙锁设备，找到后在回调函数bleScanResult中给出蓝牙锁的地址信息、信号强度信息
//                        iRemoteService.startBleScan();
//                        connectLock(str, jisukaisuo, data);
//                    }
//
//
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        };
//        initService();
//    }
//
//    private void connectLock(final String str, String jisukaisuo, final String data) {
//        try {
//            if (str != null && !str.equals("")) {
//                Log.d("straaa", str);
//                type = 0;
//                //                Login(str);
//                if (iRemoteService != null) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                iRemoteService.connectLock(str);
//                                Log.d("执行成功", "执行成功");
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            }
//            if (jisukaisuo != null && !jisukaisuo.equals("")) {
//                type = 1;
//                if (iRemoteService != null) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                iRemoteService.startBleScan();
//                                Log.d("执行成功", "执行成功");
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            }
//
//            if (data != null && !data.equals("")) {
//                type = 0;
//                if (iRemoteService != null) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                iRemoteService.connectLock(data);
//                                Log.d("执行成功", "执行成功");
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//




    private CookieManager cookieManager = new CookieManager();
    OkHttpClient client;
    IRemoteService iRemoteService;
    ServiceConnection connection;
    public static final MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");


    //   String url = "http://42.159.113.21/heibike/lock/getlock.mvc";

    private void httpPostJson(String macKey, String keySource, int timestamp) throws Exception {

        //client对象，核心类

        PRent p = new PRent(macKey, keySource, timestamp);
        RequestBody requestbody = RequestBody.create(JSON1, p.getPStr());
        Log.i("requestssss", p.getPStr());


//        //post表单参数
//        FormBody.Builder builder = new FormBody.Builder();
//        builder.add("MAC", macKey);
//        builder.add("keySource", keySource);
//        builder.add("timestamp", timestamp + "");
        //创建请求
//        Request request = new Request.Builder()
//                .url(url)
//                .post(builder.build())
//                .build();


        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestbody);


        OkHttpClient client = provideOkHttpClient(KaisuoActivity.this, cookieManager);


//
//        //用requestBuilder可以添加header
//        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestbody);

//        final Request request = requestBuilder.build();


        final Request request = requestBuilder.build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //子线程中
                LOG.E("requestssssErr", e + "");

            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                LOG.E("requestssss1", str);
                //子线程中


                try {
                    JSONObject ob = new JSONObject(str);
                    if (ob.getInt("result") == 0) {
                        String ss = ob.getString("info");
                        JSONObject object = new JSONObject(ss);
                        String tradeId = object.getString("tradeId");
                        final int encryptionKey = object.getInt("encryptionKey");
                        final String keys = object.getString("keys");
                        final int serverTime = object.getInt("serverTime");


                        Constants.bleService.openLock("12345678999", serverTime, keys, encryptionKey);
                        Log.d("openLock", "执行");

                    } else {
                        Toast.makeText(KaisuoActivity.this, "开锁失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    //登录
    private void Login() throws Exception {
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

                }
                if (type == 1) {

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


    //
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 123) {
                String str = (String) msg.obj;
                tvDate.setText(str);
                jindutiao.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
//
            }


        }
    };

}
