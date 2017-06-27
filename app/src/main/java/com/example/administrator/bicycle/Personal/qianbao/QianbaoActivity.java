package com.example.administrator.bicycle.Personal.qianbao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.bicycle.R;


public class QianbaoActivity extends AppCompatActivity {
    private ImageView iv_weixin, iv_zhifubao;
    private RadioGroup rg_one, rg_two;
    private Boolean changeedGroup = false;
    private int[] money = {20, 30, 50, 80, 100, 200};
    private int heibiNumber;

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
