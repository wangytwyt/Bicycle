/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.administrator.bicycle.zxing.camera.open;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.Personal.qianbao.QianbaoActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;


import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.zxing.camera.CameraManager;
import com.example.administrator.bicycle.zxing.decode.DecodeThread;
import com.example.administrator.bicycle.zxing.utils.BeepManager;
import com.example.administrator.bicycle.zxing.utils.CaptureActivityHandler;
import com.example.administrator.bicycle.zxing.utils.InactivityTimer;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Policy;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();


    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;


    private TextView shanguangdeng;//闪光灯
    private TextView image_inputNum;//输入铭牌
    private LinearLayout fanhui;//返回


    private int bicyInfoToCaptureID;//获取车号

    private CustomProgressDialog dialog;
    private Camera camera;
    private Handler mhandlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
               dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    Intent intent = new Intent(CaptureActivity.this, KaisuoActivity.class);
                    intent.putExtra("result", (String) msg.obj);
                    startActivity(intent);
                    CaptureActivity.this.finish();
                    break;

                case ContentValuse.failure:

                    Toast.makeText(CaptureActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);


        bicyInfoToCaptureID = getIntent().getIntExtra(ContentValuse.getbike, -1);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        shanguangdeng = (TextView) findViewById(R.id.shanguangdeng);//闪光灯
        shanguangdeng.setSelected(false);
        image_inputNum = (TextView) findViewById(R.id.image_inputNum);
        fanhui = (LinearLayout) findViewById(R.id.btn_return);


        shanguangdeng.setOnClickListener(this);

        image_inputNum.setOnClickListener(this);
        fanhui.setOnClickListener(this);


        if (bicyInfoToCaptureID != -1) {
            shanguangdeng.setVisibility(View.GONE);

            image_inputNum.setVisibility(View.GONE);
        }


        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("  获取中  ");

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        String resultString = rawResult.getText();
//		bundle.putInt("width", mCropRect.width());
//		bundle.putInt("height", mCropRect.height());
//		bundle.putString("result", rawResult.getText());


        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {




            if (bicyInfoToCaptureID == -1) {
                //车号
                String str = resultString.substring(resultString.length() - 9, resultString.length());
                getLockInfo(str);

//                Intent intent = new Intent(CaptureActivity.this, KaisuoActivity.class);
//                intent.putExtra("result", resultString);
//                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra(ContentValuse.getbike, resultString);
                setResult(1, intent);
                CaptureActivity.this.finish();
            }

        }

    }

//获取锁的详细信息
    private void getLockInfo(String carId) {
        if (!NetWorkStatus.isNetworkAvailable(CaptureActivity.this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        HttpUtils.doGet(Url.getLockInfo + carId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandlers.sendEmptyMessage(ContentValuse.failure);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String str = response.body().string();
                        JSONObject jsb = new JSONObject(str);
                        String result = jsb.getString("result");
                        if (result.equals("02")) {
                            JSONObject jsonObject = jsb.getJSONObject("pd");
                            String T_BIKEArea = jsonObject.getString("T_BIKEArea");
                            Message msg = new Message();
                            msg.obj = T_BIKEArea;
                            msg.what = ContentValuse.success;

                            mhandlers.sendMessage(msg);

                        } else {
                            mhandlers.sendEmptyMessage(ContentValuse.failure);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mhandlers.sendEmptyMessage(ContentValuse.failure);
                }
            }
        });

    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shanguangdeng:

                if (cameraManager.isOpen()) {
                    shanguangdeng.setSelected(false);
                    cameraManager.offLight();

                } else {
                    shanguangdeng.setSelected(true);
                    cameraManager.openLight();
                }

                break;

            case R.id.image_inputNum:
                Intent intenttwo = new Intent(CaptureActivity.this, InputActivity.class);
                startActivity(intenttwo);
                CaptureActivity.this.finish();
                break;
            case R.id.btn_return:
                finish();
                break;
        }
    }
}