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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.bicycle.MainActivity;
import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Post.PostUtil;
import com.example.administrator.bicycle.R;

import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.Dialog;
import com.example.administrator.bicycle.util.Globals;

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
    String data, str, jisukaisuo, getLock;

    private LinearLayout ll_setpassword;
    private EditText et_xiu;

    int type = 100;
    int i = 0;

    private String mname,maddress;


    // protected  ApplicationState mState;

    private AtomicBoolean connect = new AtomicBoolean(false);

    private BluetoothSocket BTSocket;
    private BluetoothAdapter BTAdapter;
    private BluetoothDevice device;
    private double lontitude = 0, latitude = 0;

    private String bi;
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
//            if(open){
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            LOG.E("-------ddd","kaisuo");
//
//                            Constants.bleService.openLock("666666");
//                            //  Constants.bleService.getLockStatus(lontitude, latitude);
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }).start();
//
//            }
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
                mname =name;
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

//456842
                        try {
                            MyApplication.bleService.openLock("123456");
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
            if (errorMsg.equals("内存空间不足")) {

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                //查询订单
//                                Constants.bleService.getLockRecord();
//
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();


            }


            send(errorMsg);
        }


//        @Override
//        public void bleGetBike(String version, String keySerial, String mac, String vol) throws RemoteException {
//            LOG.E(TAG, "bleGetBike version:" + version + " keySerial:" + keySerial + " mac:" + mac + " vol:" + vol);
//            if (jisukaisuo != null && !jisukaisuo.equals("")) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Constants.bleService.openLock("666666");
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            } else {
//                try {
//                    httpPostJson(mac, keySerial, DateToTimestamp(new Date()));
//                } catch (Exception e) {
//                }
//            }
//
//
//        }

//        @Override
//        public void bleGetRecord(String phone, String bikeTradeNo, String timestamp, String transType,
//                                 String mackey, String index, String Cap, String Vol) throws RemoteException {
//            LOG.E(TAG, "bleGetRecord");
//            bi = bikeTradeNo;
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        //删除订单
////                        Constants.bleService.delLockRecord(bi);
////                    } catch (RemoteException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }).start();
//        }

        @Override
        public void bleCmdReply(int cmd) throws RemoteException {
            LOG.E(TAG, "bleCmdReply :" + cmd);
            switch (cmd) {
                case VerifyUtil.CMD_CLOSE_BIKE://关锁

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
                    MyApplication.startLocation(KaisuoActivity.this);
                    send("已关锁");
                    break;
                case VerifyUtil.CMD_OPEN_LOCK://开锁
                    send("已开锁");
                    break;
                case VerifyUtil.CMD_UPDATE_KEY:
                    send("密码修改成功");
                    Intent intent = new Intent();
                    intent.putExtra(ContentValuse.lockname,mname);
                    intent.putExtra(ContentValuse.lockaddress,maddress);
                    setResult(1,intent);
                    KaisuoActivity.this.finish();

                    break;

            }

        }
    };


    private void send(String data) {
        Message msg = h.obtainMessage();
        msg.what = 123;
        msg.obj = data;
        h.sendMessage(msg);
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
        setContentView(R.layout.activity_kaisuo);
        i = 0;
        tvDate = (TextView) findViewById(R.id.tv_date);
        text = (TextView) findViewById(R.id.text);
        jindutiao = (ProgressBar) findViewById(R.id.jindutiao);
        xiu = (EditText) findViewById(R.id.et_xiu);
        ll_setpassword = (LinearLayout) findViewById(R.id.ll_setpassword);
        et_xiu = (EditText) findViewById(R.id.et_xiu);


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

        getintent();


    }

    private void getintent() {
        Intent intentt = getIntent();
        str = intentt.getStringExtra("result");//str即为回传的值
        jisukaisuo = intentt.getStringExtra("jisukaisuo");
        data = intentt.getStringExtra("data");
        getLock = intentt.getStringExtra(ContentValuse.getLock);
    }


    private void connectLock(final String str, String jisukaisuo, final String data, String getlock) {
        try {
            if (str != null && !str.equals("")) {

                type = 0;

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
                type = 1;
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
                type = 0;
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

                type = 1;
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


    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.bleService != null) {
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
            if (Globals.BLE_INIT && Globals.isBleFeature) {
                if (!MyApplication.bleService.isBleEnable()) {
                    Dialog.showBleDialog(this, R.string.ble_tip, bleListener, bleNeverListener);
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



    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 123) {
                String str = (String) msg.obj;
                tvDate.setText(str);
                jindutiao.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }


        }
    };

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
        stopService();
        super.onDestroy();
    }
}
