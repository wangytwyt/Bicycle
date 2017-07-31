package com.example.administrator.bicycle.zxing.camera.open;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InputActivity extends AppCompatActivity {
    EditText mingpai;
    private CustomProgressDialog dialog;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    Intent intent = new Intent(InputActivity.this, KaisuoActivity.class);
                    intent.putExtra("data", (String) msg.obj);
                    startActivity(intent);
                    break;

                case ContentValuse.failure:

                    Toast.makeText(InputActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("输入车牌号");
        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("  获取中  ");
        mingpai = (EditText) findViewById(R.id.mingpai);
        ImageView go = (ImageView) findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = mingpai.getText().toString().trim();

                if (data != null && !data.equals("")) {
                    getLockInfo(data);

                } else {
                    Toast.makeText(InputActivity.this, "请输入正确的铭牌号", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    //获取锁的详细信息
    private void getLockInfo(String carId) {
        if (!NetWorkStatus.isNetworkAvailable(InputActivity.this)) {
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        HttpUtils.doGet(Url.getLockInfo + carId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandler.sendEmptyMessage(ContentValuse.failure);
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

                            mhandler.sendMessage(msg);
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
                }
            }
        });
    }
}
