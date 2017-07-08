package com.example.administrator.bicycle.Personal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Post.Juhe;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.RegisteredSuccessFragement;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.IdcardUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CertificationActivity extends AppCompatActivity {
    private EditText ed_Certification, ed_name;
    private String cardID,name;
    private CustomProgressDialog dialog;
    private TextView tv_error;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    Toast.makeText(CertificationActivity.this, "认证成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra(ContentValuse.Realname, true);
                    setResult(1, intent);
                    CertificationActivity.this.finish();

                    break;

                case ContentValuse.failure:
                    dialog.dismiss();
                    Toast.makeText(CertificationActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_certification);
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
        tvtitle.setText("实名认证");


        ed_Certification = (EditText) findViewById(R.id.ed_Certification);
        ed_name = (EditText) findViewById(R.id.ed_name);

        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("认证中...");
        tv_error = (TextView) findViewById(R.id.tv_error);

        findViewById(R.id.but_certification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardID = ed_Certification.getText().toString().trim();
                name = ed_name.getText().toString().trim();

                if(cardID == null ||cardID.equals("")){
                    Toast.makeText(CertificationActivity.this,"请输入身份证号",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name == null ||name.equals("")){
                    Toast.makeText(CertificationActivity.this,"请输入名字",Toast.LENGTH_SHORT).show();
                    return;
                }

                    if (NetWorkStatus.isNetworkAvailable(CertificationActivity.this)) {
                        if (!IdcardUtils.validateCard(cardID)) {
                            tv_error.setText("输入身份证号不合法");
                            return;
                        }

                        if (cardID.length() < 17) {

                            if (!IdcardUtils.validateIdCard15(cardID)) {
                                tv_error.setText("请输入正确的身份证信息");
                                return;
                            } else {
                                tv_error.setText("");
                            }



                            if (TimeUtils.isSuper12(cardID, 15)) {
                                tv_error.setText("12岁以下禁止骑行");
                            } else {

                                realnameAuthentication(cardID,name);
                            }

                        } else {
                            if (!IdcardUtils.validateIdCard18(cardID)) {
                                tv_error.setText("请输入正确的身份证信息");
                                return;
                            } else {
                                tv_error.setText("");
                            }

//
//

                            if (TimeUtils.isSuper12(cardID, 18)) {
                                tv_error.setText("12岁以下禁止骑行");
                            } else {

                                realnameAuthentication(cardID,name);
                            }


                        }
                    } else {
                        Toast.makeText(CertificationActivity.this, "请设置网络！", Toast.LENGTH_SHORT).show();
                    }
                }



        });
    }


    //实名验证
    private void realnameAuthentication(String SidCard, String name) {

dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("T_USERIDCARD", SidCard);
        map.put("T_NAME", name);

        HttpUtils.doPost(Url.IdentityUrl, map, new Callback() {
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
                        if (response.equals("02")) {
                            mhandler.sendEmptyMessage(ContentValuse.success);
                        } else {
                            mhandler.sendEmptyMessage(ContentValuse.failure);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    mhandler.sendEmptyMessage(ContentValuse.failure);
                }


            }
        });

    }


}
