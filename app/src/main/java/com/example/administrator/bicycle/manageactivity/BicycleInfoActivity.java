package com.example.administrator.bicycle.manageactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.zxing.camera.open.CaptureActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BicycleInfoActivity extends Activity implements View.OnClickListener {
    private TextView tv_carid, tv_lock, tv_address;
    private String result, name, address;
    private Button but;
    private CustomProgressDialog dialog;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String status = (String) msg.obj;
            if (status.equals("02")) {
                Toast.makeText(BicycleInfoActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BicycleInfoActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
        but = (Button) findViewById(R.id.but);

        but.setOnClickListener(this);

        dialog = new CustomProgressDialog(this);
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
                String id = tv_carid.getText().toString().trim();
                String lock = tv_lock.getText().toString().trim();
                String address = tv_address.getText().toString().trim();

                commit(id, lock, address);

                break;
        }
    }

    private void commit(String id, String lock, String address) {
        if (!NetWorkStatus.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("T_BIKENO", id);
        map.put("T_BIKES", lock);
        map.put("T_BIKEArea", address);

        HttpUtils.doPost(Url.saveBike, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
                send("hjh");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String result = jsonObject.getString("result");

                        send(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    send("hjh");
                }
            }
        });
    }

    private void send(String obj) {
        Message message = new Message();
        message.obj = obj;
        mhandler.sendMessage(message);
    }


    private void toCaptureActivity() {
        Intent intent = new Intent(BicycleInfoActivity.this, CaptureActivity.class);
        intent.putExtra(ContentValuse.getbike, ContentValuse.getbid);
        startActivityForResult(intent, ContentValuse.getbikeback);
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
            case ContentValuse.getbikeback:

                result = data.getStringExtra(ContentValuse.getbike);
                if (result != null) {
                    tv_carid.setText(result);
                }
                setbut();


                break;
            case ContentValuse.bicyInfoToKaisuo:
                name = data.getStringExtra(ContentValuse.lockname);
                address = data.getStringExtra(ContentValuse.lockaddress);
                if (name != null && address != null) {
                    tv_lock.setText(name);
                    tv_address.setText(address);
                }
                setbut();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setbut() {
        if (result != null && name != null && address != null && !result.equals("") && !name.equals("") && !address.equals("")) {
            but.setBackgroundResource(R.mipmap.com_off);
            but.setEnabled(true);
        } else {
            but.setBackgroundResource(R.mipmap.com_no);
            but.setEnabled(false);

        }
    }
}
