package com.example.administrator.bicycle.manageactivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Personal.MessageActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.photo.utils.HeadImagePop;
import com.example.administrator.bicycle.photo.utils.SelectPicPopupWindow;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.view.RoundImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manage);
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
        tvtitle.setText("管理员");






        findViewById(R.id.ll_manage).setOnClickListener(this);
        findViewById(R.id.ll_input).setOnClickListener(this);
        findViewById(R.id.ll_issue).setOnClickListener(this);
        findViewById(R.id.ll_repair).setOnClickListener(this);

        findViewById(R.id.line_one).setOnClickListener(this);
        iv_img = (RoundImageView) findViewById(R.id.iv_img);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.onRequestPermissionsResultCamera(this, requestCode, permissions, grantResults)) {
            HeadImagePop.setSelectPicPopupWindow(this,findViewById(R.id.ll_lin));

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = HeadImagePop.onActivityResult(this, requestCode, resultCode, data);
        if (bitmap != null) {
            iv_img.setImageBitmap(bitmap);
        }

    super.onActivityResult(requestCode, resultCode, data);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_one:

                if (PermissionUtils.checkPermissionCamera(this)) {
                    HeadImagePop.setSelectPicPopupWindow(this,findViewById(R.id.ll_lin));
                }

                break;
            case R.id.ll_manage:
                startActivity(new Intent(this, CollectInformationActivity.class));
                break;
            case R.id.ll_input:
                startActivity(new Intent(this, BicycleInfoActivity.class));
                break;
            case R.id.ll_issue:
                startActivity(new Intent(this, BatchIssueActivity.class));
                break;
            case R.id.ll_repair:
                startActivity(new Intent(this, AllRepairBicycleActivity.class));
                break;
        }
    }
}
