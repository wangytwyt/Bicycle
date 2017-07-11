package com.example.administrator.bicycle.Personal.qianbao;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.ComplaintsActivity;
import com.example.administrator.bicycle.MyApplication;
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.PrizeDialogActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.HttpUtils;
import com.example.administrator.bicycle.util.NetWorkStatus;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class QianbaoActivity extends AppCompatActivity {
    private ImageView iv_weixin, iv_zhifubao;
    private RadioGroup rg_one, rg_two;
    private Boolean changeedGroup = false;
    private int heibiNumber;

    private double rmb, heibi;

    private TextView tv_rmb, tv_heibi;

  //  private CustomProgressDialog dialog;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
         //   dialog.dismiss();
            switch (msg.what) {
                case ContentValuse.success:
                    tv_rmb.setText(rmb+" 元");
                    tv_heibi.setText(heibi+"  枚");
                    break;

                case ContentValuse.failure:

                    Toast.makeText(QianbaoActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_qianbao);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("我的钱包");

        iv_weixin = (ImageView) findViewById(R.id.iv_weinxin);
        iv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);

        tv_rmb = (TextView) findViewById(R.id.tv_rmb);
        tv_heibi = (TextView) findViewById(R.id.tv_heibi);


//        dialog = CustomProgressDialog.createDialog(this);
//        dialog.setMessage("   加载中...");

        rg_one = (RadioGroup) findViewById(R.id.rg_one);
        rg_two = (RadioGroup) findViewById(R.id.rg_two);
        rg_one.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        rg_one.check(R.id.rb_30);
        rg_two.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        iv_weixin.setSelected(true);
        findViewById(R.id.ll_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iv_weixin.isSelected()) {
                    iv_weixin.setSelected(false);
                    iv_zhifubao.setSelected(true);
                } else {
                    iv_weixin.setSelected(true);
                    iv_zhifubao.setSelected(false);
                }
            }
        });

        findViewById(R.id.ll_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (iv_zhifubao.isSelected()) {
                    iv_zhifubao.setSelected(false);
                    iv_weixin.setSelected(true);
                } else {
                    iv_zhifubao.setSelected(true);
                    iv_weixin.setSelected(false);
                }
            }
        });


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_zhifubao.isSelected()) {
                    zhifubaoPay();
                }
                if (iv_weixin.isSelected()) {
                    weixinPay();
                }
            }
        });
        getRMB();
    }


    private void getRMB() {
        if (!NetWorkStatus.isNetworkAvailable(QianbaoActivity.this)) {
            Toast.makeText(QianbaoActivity.this, "网络不可用，请设置网络！", Toast.LENGTH_SHORT).show();
            return;
        }
       // dialog.show();
        HttpUtils.doGet(Url.getRMB + MyApplication.user.getT_USERPHONE(), new Callback() {
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
                            JSONObject js = jsonObject.getJSONObject("pd");
                            String srmb = js.getString("T_RMB");
                            rmb = stringtodouble(srmb);
                            String sheibi = js.getString("T_HEIBI");
                            heibi = stringtodouble(sheibi);

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

    private double stringtodouble(String srmb) {
        if (srmb.equals("null")) {
            return 0.0;
        } else {
            return Double.valueOf(srmb);
        }
    }

    private void zhifubaoPay() {

    }

    private void weixinPay() {

    }

    class MyRadioGroupOnCheckedChangedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (!changeedGroup) {
                changeedGroup = true;
                if (group == rg_one) {
                    rg_two.clearCheck();
                } else if (group == rg_two) {
                    rg_one.clearCheck();
                }
                changeedGroup = false;
            }

            switch (checkedId) {
                case R.id.rb_20:
                    heibiNumber = 20;
                    break;
                case R.id.rb_30:
                    heibiNumber = 30;
                    break;
                case R.id.rb_50:
                    heibiNumber = 50;
                    break;
                case R.id.rb_80:
                    heibiNumber = 80;
                    break;
                case R.id.rb_100:
                    heibiNumber = 100;
                    break;
                case R.id.rb_200:
                    heibiNumber = 200;
                    break;
            }

        }
    }
}
