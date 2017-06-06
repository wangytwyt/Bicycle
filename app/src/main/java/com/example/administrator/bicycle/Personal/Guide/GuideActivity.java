package com.example.administrator.bicycle.Personal.Guide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bicycle.AllProblemActivity;
import com.example.administrator.bicycle.AnnualFeeActivity;
import com.example.administrator.bicycle.Personal.zhaobudaoche.ZhaobudaocheActivity;
import com.example.administrator.bicycle.R;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout one, two, three, four, five, six;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guide);
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
        tvtitle.setText("用户指南");
        one = (LinearLayout) findViewById(R.id.line_one);
        two = (LinearLayout) findViewById(R.id.line_two);
        three = (LinearLayout) findViewById(R.id.line_three);
        four = (LinearLayout) findViewById(R.id.line_four);
        five = (LinearLayout) findViewById(R.id.line_five);
        six = (LinearLayout) findViewById(R.id.line_six);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_one:
                startActivity(new Intent(GuideActivity.this, CantOpenActivity.class));
                break;
            case R.id.line_two:
                startActivity(new Intent(GuideActivity.this, ReportActivity.class));
                break;
            case R.id.line_three:
                startActivity(new Intent(GuideActivity.this, AnnualFeeActivity.class));
                break;
            case R.id.line_four:
                startActivity(new Intent(GuideActivity.this, WeitingActivity.class));
                break;
            case R.id.line_five:
                startActivity(new Intent(GuideActivity.this, ZhaobudaocheActivity.class));
                break;
            case R.id.line_six:
               startActivity(new Intent(GuideActivity.this, AllProblemActivity.class));
                break;

        }
    }
}
