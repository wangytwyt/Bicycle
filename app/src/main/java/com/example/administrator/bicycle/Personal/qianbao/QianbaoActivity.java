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
   private ImageView iv_weixin,iv_zhifubao;
private RadioGroup  rg_one,rg_two;
    private Boolean changeedGroup = false;
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

        iv_weixin = (ImageView)findViewById(R.id.iv_weinxin);
        iv_zhifubao= (ImageView)findViewById(R.id.iv_zhifubao);
        rg_one = (RadioGroup)findViewById(R.id.rg_one);

        rg_two = (RadioGroup)findViewById(R.id.rg_two);
        rg_one.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        rg_two.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        iv_weixin.setSelected(true);
        findViewById(R.id.ll_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zhifubao.setSelected(false);
                if(iv_weixin.isSelected()){
                    iv_weixin.setSelected(false);
                }else {
                    iv_weixin.setSelected(true);
                }
            }
        });

        findViewById(R.id.ll_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_weixin.setSelected(false);

                if(iv_zhifubao.isSelected()){
                    iv_zhifubao.setSelected(false);

                }else {
                    iv_zhifubao.setSelected(true);

                }
            }
        });
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
        }
    }
}
