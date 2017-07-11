package com.example.administrator.bicycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.manageactivity.RepairBicycleActivity;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.view.CheckBoxView;
import com.example.administrator.bicycle.zxing.camera.open.CaptureActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ComplaintsActivity extends Activity {
    private CheckBoxView cbxv;
    private EditText edt_one, edt_two;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:

                    Toast.makeText(ComplaintsActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    ComplaintsActivity.this.finish();
                    break;

                case ContentValuse.failure:

                    Toast.makeText(ComplaintsActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private CustomProgressDialog dialog;

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

        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("   提交中...   ");

        cbxv = (CheckBoxView) findViewById(R.id.cbxv);
        cbxv.setDataResource(this, R.array.car_fault);
        edt_one = (EditText) findViewById(R.id.edt_one);
        edt_two = (EditText) findViewById(R.id.edt_two);
        findViewById(R.id.iv_getbid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.checkPermissionCamera(ComplaintsActivity.this)) {
                    toCaptureActivity();
                }
            }
        });

        findViewById(R.id.btn_nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private void commit() {
        String bicy = edt_one.getText().toString().trim();
        String note = edt_two.getText().toString().trim();

        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bicy.equals("")) {
            Toast.makeText(this, "请扫码输入车号！", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("BxId", cbxv.getIds());
        map.put("BIKENO", bicy);
        map.put("note", note);
        map.put("T_USERPHONE", MyApplication.user.getT_USERPHONE());
        HttpUtils.doPost(Url.insertTs, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandler.sendEmptyMessage(ContentValuse.failure);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String userjson = response.body().string();
                        JSONObject jsonObject = new JSONObject(userjson);
                        String result = jsonObject.getString("result");
                        if (result.equals("02")) {
                            mhandler.sendEmptyMessage(ContentValuse.success);
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }

                    } catch (Exception e) {
                        mhandler.sendEmptyMessage(ContentValuse.failure);
                        e.printStackTrace();
                    }
                } else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
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
