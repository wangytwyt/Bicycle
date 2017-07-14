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
import com.example.administrator.bicycle.util.EndTripDialog;
import com.example.administrator.bicycle.util.Globals;

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


import static android.content.Context.BIND_AUTO_CREATE;

public class KaisuoActivity extends AppCompatActivity {
    private final String TAG = "---------------------";
    String url = "https://alabike.luopingelec.com/alabike/ab_mapp";
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 11002;

    private final int OPEN = 1;
    private final int CLOSE = 2;
    private final int UPDATE = 3;

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
            }


        }
    };

    String data, str, jisukaisuo, getLock;

    private TextView kai_ticker, lock_status;

    private LinearLayout ll_setpassword;
    private EditText et_xiu;

    int type = 100;
    int i = 0;

    private String mname, maddress;

    private Handler stepTimeHandler;
    private Runnable mTicker;
    long startTime = 0;
    private EndTripDialog enddialog;

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
        }


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

                    send(CLOSE);
                    break;
                case VerifyUtil.CMD_OPEN_LOCK://开锁
                    send(OPEN);
                    break;
                case VerifyUtil.CMD_UPDATE_KEY:
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
        i = 0;

        initView();

        getintent();


    }

    private void initView() {
        xiu = (EditText) findViewById(R.id.et_xiu);
        ll_setpassword = (LinearLayout) findViewById(R.id.ll_setpassword);
        et_xiu = (EditText) findViewById(R.id.et_xiu);


        lock_status = (TextView) findViewById(R.id.lock_status);
        kai_ticker = (TextView) findViewById(R.id.kai_ticker);
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
                        dialog.dismiss();
                        //设置你的操作事项
                        Toast.makeText(KaisuoActivity.this,"哇啊傻傻的发呆",Toast.LENGTH_SHORT).show();
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
                    Dialog.showBleDialog(this, R.string.ble_tip, bleListener, bleNeverListener, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            KaisuoActivity.this.finish();
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
            KaisuoActivity.this.finish();
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
