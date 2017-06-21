package com.example.administrator.bicycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_zhifubao, iv_weixin, iv_priceone, iv_pricetwo;
    private int price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recharge);
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
        TextView text = (TextView) findViewById(R.id.tv_text);

        tvtitle.setText("充值");

        iv_weixin = (ImageView) findViewById(R.id.iv_weixin);
        iv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);
        iv_priceone = (ImageView) findViewById(R.id.iv_priceone);
        iv_pricetwo = (ImageView) findViewById(R.id.iv_pricetwo);
        iv_weixin.setSelected(true);
        iv_pricetwo.setSelected(true);
        findViewById(R.id.rl_one).setOnClickListener(this);
        findViewById(R.id.rl_two).setOnClickListener(this);
        findViewById(R.id.ll_weixin).setOnClickListener(this);
        findViewById(R.id.ll_zhifubao).setOnClickListener(this);
        findViewById(R.id.btn_return).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_one:
                if (iv_priceone.isSelected()) {
                    iv_priceone.setSelected(false);
                    iv_pricetwo.setSelected(true);
                } else {
                    iv_priceone.setSelected(true);
                    iv_pricetwo.setSelected(false);
                }


                break;
            case R.id.rl_two:
                if (iv_pricetwo.isSelected()) {
                    iv_pricetwo.setSelected(false);
                    iv_priceone.setSelected(true);
                } else {
                    iv_pricetwo.setSelected(true);
                    iv_priceone.setSelected(false);
                }
                break;
            case R.id.ll_weixin:

                if (iv_weixin.isSelected()) {
                    iv_weixin.setSelected(false);
                    iv_zhifubao.setSelected(true);
                } else {
                    iv_weixin.setSelected(true);
                    iv_zhifubao.setSelected(false);
                }
                break;
            case R.id.ll_zhifubao:
                if (iv_zhifubao.isSelected()) {
                    iv_zhifubao.setSelected(false);
                    iv_weixin.setSelected(true);
                } else {
                    iv_zhifubao.setSelected(true);
                    iv_weixin.setSelected(false);
                }
                break;
            case R.id.btn_return:
                if(iv_pricetwo.isSelected()){
                    price = 168;
                }
                if(iv_priceone.isSelected()){
                   price = 88;
                }

                if (iv_zhifubao.isSelected()){


                }
                if(iv_weixin.isSelected()){


                }


                break;


        }
    }
}
