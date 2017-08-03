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
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.model.NaviLatLng;
import com.example.administrator.bicycle.MainActivity;
import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Personal.Guide.GuideActivity;
import com.example.administrator.bicycle.Post.PostUtil;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;

import com.example.administrator.bicycle.WebActivity;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.Dialog;
import com.example.administrator.bicycle.util.EndTripDialog;
import com.example.administrator.bicycle.util.Globals;

import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.TimeUtils;
import com.luopingelec.permission.PermissionsManager;
import com.luopingelec.permission.PermissionsResultAction;
import com.sofi.smartlocker.ble.BleService;
import com.sofi.smartlocker.ble.interfaces.IRemoteCallback;
import com.sofi.smartlocker.ble.interfaces.IRemoteService;
import com.sofi.smartlocker.ble.util.LOG;
import com.sofi.smartlocker.ble.util.VerifyUtil;
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


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.BIND_AUTO_CREATE;

public class KaisuoActivity extends AppCompatActivity {
    private final String TAG = "---------------------";

    private int LOCK_STATUS;
    private final int OPEN = 1;
    private final int CLOSE = 2;
    private final int UPDATE = 3;
    private final int FORCECLOSE = 4;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case OPEN:
                    startTiming();
                    lock_status.setText("骑行中");
                    lock_status.setBackgroundResource(R.mipmap.jieshuxingchengqian);
                    break;
                case CLOSE:
                    closeTiming();
                    lock_status.setText("已结束");
                    lock_status.setBackgroundResource(R.mipmap.jieshuxingchenghou);
                    break;
                case UPDATE:
                    lock_status.setText("骑行中且密码修改成功");
                    lock_status.setBackgroundResource(R.mipmap.jieshuxingchengqian);
                    break;

                case ContentValuse.success:
                    Toast.makeText(KaisuoActivity.this, "开锁成功", Toast.LENGTH_SHORT).show();
                    break;

                case FORCECLOSE:

                    Toast.makeText(KaisuoActivity.this, "关闭成功", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    String data, str, jisukaisuo, getLock;

    private TextView kai_ticker, lock_status;

    private LinearLayout ll_setpassword;
    private EditText et_xiu;


    private String mname, maddress;

    private Handler stepTimeHandler;
    private Runnable mTicker;
    long startTime = 0;

    private String lockkey = "123456";
    private boolean isfinsh = false;
    public static AMapLocationClient mLocationClient = null;
    private static AMapLocationClientOption mLocationOption;

    private String carId;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LOG.E(TAG, "onServiceConnected");
            try {
                MyApplication.bleService = IRemoteService.Stub.asInterface(service);
                //  Constants.bleService.startBleScan();
                MyApplication.bleService.registerCallback(mCallback);
                connectLock(str, jisukaisuo, data, getLock);
                MyApplication.bleService.setHighMode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LOG.E(TAG, "onServiceDisconnected");
            try {
                if (MyApplication.bleService != null) {
                    MyApplication.bleService.unregisterCallback(mCallback);
                    MyApplication.bleService = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    IRemoteCallback.Stub mCallback = new IRemoteCallback.Stub() {


        @Override
        public void bleSupportFeature(boolean isFeature) throws RemoteException {
            Globals.isBleFeature = isFeature;
            showBleTipDialog();
        }

        @Override
        public void bleGetParams(String batteryVol, String solarVol, boolean open) throws RemoteException {
            LOG.E(TAG, "bleGetParams batteryVol:" + batteryVol + " solarVol:" + solarVol + " open:" + open);
        }

        @Override
        public void bleScanResult(String name, final String address, int rssi) throws RemoteException {
            LOG.E(TAG, "bleScanResult name:" + name + " address:" + address + " bikeNo:" + Globals.bikeNo
                    + " rssi:" + rssi);
            //该函数用于扫描周边的蓝牙锁设备，找到后在回调函数bleScanResult中给出蓝牙锁的地址信息、信号强度信息


            if ((jisukaisuo != null && !jisukaisuo.equals(""))) {
                connectLock(address);
            }
            if (getLock != null && !getLock.equals("")) {
                mname = name;
                maddress = address;
                connectLock(address);
            }

        }

        @Override
        public void bleStatus(boolean status, String address) throws RemoteException {
            LOG.E(TAG, "bleStatus :" + status + " address :" + address);
            //连接成功失败信息

            if (status) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

//456842 D4:36:39:97:23:FA
                        try {
                            MyApplication.bleService.openLock(lockkey);

                            lockkey = "487634";
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

            }
        }

        @Override
        public void bleCmdError(int cmd, String errorMsg) throws RemoteException {
        }


        @Override
        public void bleCmdReply(int cmd) throws RemoteException {
            LOG.E(TAG, "bleCmdReply :" + cmd);
            switch (cmd) {
                case VerifyUtil.CMD_CLOSE_BIKE://关锁
                    isfinsh = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyApplication.bleService.disconnectLock();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    LOCK_STATUS = CLOSE;
                    startLocation(KaisuoActivity.this);
                    send(CLOSE);
                    break;
                case VerifyUtil.CMD_OPEN_LOCK://开锁
                    LOCK_STATUS = OPEN;
                    startLocation(KaisuoActivity.this);

                    send(OPEN);
                    break;
                case VerifyUtil.CMD_UPDATE_KEY:
                    isfinsh = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyApplication.bleService.disconnectLock();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    send(UPDATE);
                    Intent intent = new Intent();
                    intent.putExtra(ContentValuse.lockname, mname);
                    intent.putExtra(ContentValuse.lockaddress, maddress);
                    setResult(1, intent);
                    KaisuoActivity.this.finish();
                    break;

            }

        }
    };

/*
 骑行开始并提交当前坐标
* */
    private void startCycling(double latitude, double longitude) {
        if (!NetWorkStatus.isNetworkAvailable(KaisuoActivity.this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpUtils.doGet(Url.startLock + carId + "&T_USERPHONE="
                + MyApplication.user.getT_USERPHONE() + "&T_ENDTIME=" + latitude
                + "&T_ENDAREA=" + longitude, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String str = response.body().string();
                        JSONObject jsb = new JSONObject(str);
                        String result = jsb.getString("result");
                        if (result.equals("02")) {
                            send(ContentValuse.success);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
    /*
    * 骑行结束提交当前坐标
    * */
    private void endCycling(double latitude, double longitude,int status) {
        if (!NetWorkStatus.isNetworkAvailable(KaisuoActivity.this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.doGet(Url.endLock + carId + "&T_USERPHONE="
                + MyApplication.user.getT_USERPHONE() + "&T_ENDJD=" + latitude + "&T_ENDWD=" + longitude+"&T_NOMAL="+status, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    JSONObject jsb = new JSONObject(str);
                    String result = jsb.getString("result");
                    if (result.equals("02")) {
                        send(FORCECLOSE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void send(int data) {
        Message msg = new Message();
        msg.what = data;
        mHandler.sendMessage(msg);
    }

    private void connectLock(final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    MyApplication.bleService.connectLock(address);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }


    private EditText xiu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kaisuo);

        initView();

        getintent();

        if (MyApplication.bleService != null) {
            showBleTipDialog();
        } else {
            requestLocationPermission();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isfinsh) {
            finish();
        }
        return false;

    }

    private void initView() {
        findViewById(R.id.iv_back).setVisibility(View.GONE);
        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("骑行界面");
        xiu = (EditText) findViewById(R.id.et_xiu);
        ll_setpassword = (LinearLayout) findViewById(R.id.ll_setpassword);
        et_xiu = (EditText) findViewById(R.id.et_xiu);




        lock_status = (TextView) findViewById(R.id.lock_status);
        kai_ticker = (TextView) findViewById(R.id.kai_ticker);

        findViewById(R.id.tv_insurance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(KaisuoActivity.this, WebActivity.class);
                intent6.putExtra(ContentValuse.url, Url.Safety_insurance);
                startActivity(intent6);
            }
        });

        findViewById(R.id.but_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (MyApplication.bleService != null) {
                                MyApplication.bleService.updateKey("456842", et_xiu.getText().toString().trim());
                            }

                            Log.d("执行成功", "执行成功");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.tv_endtrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTripDialog.Builder builder = new EndTripDialog.Builder(KaisuoActivity.this);
                builder.setPositiveButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isfinsh = true;
                        dialog.dismiss();
                        //设置你的操作事项
                        LOCK_STATUS = FORCECLOSE;
                        startLocation(KaisuoActivity.this);
                        send(CLOSE);
                        send(FORCECLOSE);

                    }
                });

                builder.setNegativeButton(
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });
    }

    /*骑行计时
    * */
    private void startTiming() {
        stepTimeHandler = new Handler();
        startTime = System.currentTimeMillis();
        mTicker = new Runnable() {
            public void run() {
                String content = TimeUtils.showTimeCount(System.currentTimeMillis() - startTime);
                kai_ticker.setText(content);

                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                stepTimeHandler.postAtTime(mTicker, next);
            }
        };
        //启动计时线程，定时更新
        mTicker.run();
    }

    private void closeTiming() {
        if (stepTimeHandler != null) {
            stepTimeHandler.removeCallbacks(mTicker);
        }

    }

    private void getintent() {
        Intent intentt = getIntent();
        str = intentt.getStringExtra("result");//str即为回传的值
        jisukaisuo = intentt.getStringExtra("jisukaisuo");
        data = intentt.getStringExtra("data");
        getLock = intentt.getStringExtra(ContentValuse.getLock);
    }

/*连接锁
* */
    private void connectLock(final String str, String jisukaisuo, final String data, String getlock) {
        try {
            if (str != null && !str.equals("")) {
                carId = str;

                if (MyApplication.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyApplication.bleService.connectLock(str);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } else if (jisukaisuo != null && !jisukaisuo.equals("")) {

                if (MyApplication.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if (MyApplication.bleService != null) {
                                    MyApplication.bleService.startBleScan();
                                }

                                Log.d("执行成功", "执行成功");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } else if (data != null && !data.equals("")) {
                carId = data;
                if (MyApplication.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyApplication.bleService.connectLock(data);

                                Log.d("执行成功", "执行成功");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } else if (getLock != null && !getLock.equals("")) {
                ll_setpassword.setVisibility(View.VISIBLE);
                findViewById(R.id.tv_endtrip).setVisibility(View.GONE);

                if (MyApplication.bleService != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if (MyApplication.bleService != null) {
                                    MyApplication.bleService.startBleScan();
                                }

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


    private void initService() {
        Intent intent = new Intent(this, BleService.class);
        //  Intent bind = IntentUtil.getExplicitIntent(this, intent);
        startService(intent);
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    private void showBleTipDialog() {
        try {
            if (Globals.BLE_INIT && Globals.isBleFeature) {
                if (!MyApplication.bleService.isBleEnable()) {
                    Dialog.showBleDialog(this, R.string.ble_tip, bleListener, bleNeverListener, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          KaisuoActivity.this.finish();
                       //     Toast.makeText(KaisuoActivity.this,"蓝牙未打开，请打开蓝牙！",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    rentCar();
                }
            } else if (Globals.BLE_INIT) {
                Dialog.showAlertDialog(this, R.string.ble_feature_tip);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void rentCar() {
        try {
            if (MyApplication.bleService != null) {
                MyApplication.bleService.startBleScan();
            }
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
            KaisuoActivity.this.finish();



        }
    };


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


    private void stopService() {
        try {
            if (MyApplication.bleService != null) {
                MyApplication.bleService.unregisterCallback(mCallback);
                MyApplication.bleService = null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (mConn != null) {
            unbindService(mConn);
        }
    }

    @Override
    protected void onDestroy() {
        Globals.BLE_INIT = true;

        mLocationClient.onDestroy();
        stopService();
        super.onDestroy();
    }

    /**
     * 开始定位
     */
    public void startLocation(Context context) {
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


    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。获取当前经纬度
                    double latitude = amapLocation.getLatitude();//精度
                    double longitude = amapLocation.getLongitude();//维度
                    if (LOCK_STATUS == OPEN) {
                        startCycling(latitude, longitude);
                    } else if (LOCK_STATUS == CLOSE) {
                        endCycling(latitude, longitude,1);
                    }else if(LOCK_STATUS == FORCECLOSE){
                        endCycling(latitude, longitude,2);
                    }

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
