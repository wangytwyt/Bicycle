package com.example.administrator.bicycle.manageactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.MainActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.zxing.utils.CaptureActivity;

public class BicycleInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_carid, tv_lock, tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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
    protected void onResume() {
        if(!ContentValuse.Chassisnumber .equals("")){
            tv_carid.setText(ContentValuse.Chassisnumber );
        }
        if(!ContentValuse.lockname .equals("")){
            tv_lock.setText(ContentValuse.lockname );
        }
        if(!ContentValuse.lockaddress .equals("")){
            tv_address.setText(ContentValuse.lockaddress );
        }
        super.onResume();
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
                startActivity(intent);


                break;
            case R.id.but:

                break;
        }
    }

    private void toCaptureActivity() {
        Intent intent = new Intent(BicycleInfoActivity.this, CaptureActivity.class);
        intent.putExtra(ContentValuse.bicyInfoToCapture, ContentValuse.bicyInfoToCaptureID);
        startActivity(intent);
   //   startActivityForResult (intent, ContentValuse.bicyInfoToCaptureback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {

        if (PermissionUtils.onRequestPermissionsResultCamera(BicycleInfoActivity.this, requestCode, permissions, paramArrayOfInt)) {
            toCaptureActivity();
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case ContentValuse.bicyInfoToCaptureback:
//                String result = data.getStringExtra(ContentValuse.result);
//                if (result != null) {
//                    tv_carid.setText(result);
//                }
//
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
