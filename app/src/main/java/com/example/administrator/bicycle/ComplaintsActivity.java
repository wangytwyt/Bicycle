package com.example.administrator.bicycle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.bicycle.manageactivity.RepairBicycleActivity;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.view.CheckBoxView;
import com.example.administrator.bicycle.zxing.camera.open.CaptureActivity;

public class ComplaintsActivity extends Activity {
private CheckBoxView cbxv;
    private EditText edt_one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_complaints);
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
        tvtitle.setText("投诉");

        cbxv =(CheckBoxView) findViewById(R.id.cbxv);
        cbxv.setDataResource(this,R.array.car_fault);
        edt_one =(EditText) findViewById(R.id.edt_one);
        findViewById(R.id.iv_getbid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.checkPermissionCamera(ComplaintsActivity.this)) {
                    toCaptureActivity();
                }
            }
        });
    }

    private void toCaptureActivity() {
        Intent intent = new Intent(ComplaintsActivity.this, CaptureActivity.class);
        intent.putExtra(ContentValuse.getbike, ContentValuse.getbid);
        startActivityForResult(intent, ContentValuse.getbikeback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {

        if (PermissionUtils.onRequestPermissionsResultCamera(ComplaintsActivity.this, requestCode, permissions, paramArrayOfInt)) {
            toCaptureActivity();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 0) {
            return;
        }

        switch (requestCode) {
            case ContentValuse.getbikeback:

                String result = data.getStringExtra(ContentValuse.getbike);
                if (result != null) {
                    edt_one.setText(result);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
