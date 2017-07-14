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

        findViewById(R.id.bicy_one).setOnClickListener(this);
        findViewById(R.id.bicy_two).setOnClickListener(this);
        findViewById(R.id.bicy_three).setOnClickListener(this);
        findViewById(R.id.bicy_four).setOnClickListener(this);
        findViewById(R.id.bicy_five).setOnClickListener(this);

        findViewById(R.id.vip_one).setOnClickListener(this);
        findViewById(R.id.vip_two).setOnClickListener(this);
        findViewById(R.id.vip_three).setOnClickListener(this);

        findViewById(R.id.name_one).setOnClickListener(this);
        findViewById(R.id.name_two).setOnClickListener(this);
        findViewById(R.id.name_three).setOnClickListener(this);

        findViewById(R.id.party).setOnClickListener(this);

        findViewById(R.id.my_one).setOnClickListener(this);
        findViewById(R.id.my_two).setOnClickListener(this);

        findViewById(R.id.insurance_one).setOnClickListener(this);
        findViewById(R.id.insurance_two).setOnClickListener(this);
        findViewById(R.id.insurance_three).setOnClickListener(this);

        findViewById(R.id.line_six).setOnClickListener(this);
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bicy_one:
              startActivity(new Intent(this,ZhaobudaocheActivity.class));
                break;
            case R.id.bicy_two:
                startActivity(new Intent(GuideActivity.this, CantOpenActivity.class));
                break;
            case R.id.bicy_three:
                startActivity(new Intent(GuideActivity.this, WeitingActivity.class));
                break;
            case R.id.bicy_four:
                startActivity(new Intent(GuideActivity.this, CarAlsoActivity.class));
                break;
            case  R.id.bicy_five:
                startActivity(new Intent(GuideActivity.this, RidingRulesActivity.class));
                break;




            case R.id.vip_one:
                startActivity(new Intent(GuideActivity.this, AnnualFeeActivity.class));
                break;
            case R.id.vip_two:

                break;
            case R.id.vip_three:

                break;



            case R.id.name_one:

                break;
            case R.id.name_two:

                break;
            case R.id.name_three:

                break;


            case R.id.party:

                break;

            case R.id.my_one:

                break;

            case R.id.my_two:


                break;

            case R.id.insurance_one:

                break;

            case R.id.insurance_two:


                break;


            case R.id.insurance_three:


                break;

            case R.id.line_six:
                startActivity(new Intent(GuideActivity.this, AllProblemActivity.class));
                break;
        }
    }
}
