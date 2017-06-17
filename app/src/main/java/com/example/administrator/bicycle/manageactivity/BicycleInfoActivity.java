package com.example.administrator.bicycle.manageactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.MainActivity;
import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.zxing.utils.CaptureActivity;

public class BicycleInfoActivity extends Activity implements View.OnClickListener {
    private TextView tv_carid, tv_lock, tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_bicycle_info);
        init();
    }

    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("车辆信息");


        tv_carid = (TextView) findViewById(R.id.tv_carid);
        tv_lock = (TextView) findViewById(R.id.tv_lock);
        tv_address = (TextView) findViewById(R.id.tv_address);

        findViewById(R.id.tv_getbid).setOnClickListener(this);
        findViewById(R.id.tv_getlock).setOnClickListener(this);
        findViewById(R.id.but).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getbid:
                if (PermissionUtils.checkPermissionCamera(this)) {
                    toCaptureActivity();
                }
                break;
            case R.id.tv_getlock:
                Intent intent = new Intent(BicycleInfoActivity.this, KaisuoActivity.class);
                intent.putExtra(ContentValuse.getLock, ContentValuse.getLock);
                startActivityForResult(intent, ContentValuse.bicyInfoToKaisuo);
                break;
            case R.id.but:

                break;
        }
    }

    private void toCaptureActivity() {
        Intent intent = new Intent(BicycleInfoActivity.this, CaptureActivity.class);
        intent.putExtra(ContentValuse.bicyInfoToCapture, ContentValuse.bicyInfoToCaptureID);
        startActivityForResult(intent, ContentValuse.bicyInfoToCaptureback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {

        if (PermissionUtils.onRequestPermissionsResultCamera(BicycleInfoActivity.this, requestCode, permissions, paramArrayOfInt)) {
            toCaptureActivity();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 0) {
            return;
        }

        switch (requestCode) {
            case ContentValuse.bicyInfoToCaptureback:

                String result = data.getStringExtra(ContentValuse.bicyInfoToCapture);
                if (result != null) {
                    tv_carid.setText(result);
                }

                break;
            case ContentValuse.bicyInfoToKaisuo:
                String name = data.getStringExtra(ContentValuse.lockname);
                String address = data.getStringExtra(ContentValuse.lockaddress);
                if (name != null && address != null) {
                    tv_lock.setText(name);
                    tv_address.setText(address);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
