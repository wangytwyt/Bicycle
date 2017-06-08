package com.example.administrator.bicycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SubscribeActivity extends AppCompatActivity {
private RadioGroup rg_group;
    private Button but;
    private TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_subscribe);
        initView();


    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_submit =(TextView) findViewById(R.id.tv_submit);
        tv_submit.setText("帮助");
        tv_submit.setVisibility(View.VISIBLE);
        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("预约");
        rg_group =(RadioGroup) findViewById(R.id.rg_group);
         rg_group.check(R.id.rb_one);
        but = (Button) findViewById(R.id.but_sure);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubscribeActivity.this,SubscribeSuccessActivity.class));
                SubscribeActivity.this.finish();
            }
        });
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubscribeActivity.this,HelpActivity.class));
            }
        });
    }

}
