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
import com.example.administrator.bicycle.Post.Url;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.WebActivity;
import com.example.administrator.bicycle.util.ContentValuse;

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

                Intent bicyintent1 =  new Intent(GuideActivity.this, WebActivity.class);
                bicyintent1.putExtra(ContentValuse.url,Url.noFindCar);
                startActivity(bicyintent1);
            //  startActivity(new Intent(this,ZhaobudaocheActivity.class));
                break;
            case R.id.bicy_two:

                Intent bicyintent2 =  new Intent(GuideActivity.this, WebActivity.class);
                bicyintent2.putExtra(ContentValuse.url,Url.no_unlock);
                startActivity(bicyintent2);
              //  startActivity(new Intent(GuideActivity.this, CantOpenActivity.class));
                break;
            case R.id.bicy_three:
                startActivity(new Intent(GuideActivity.this, WeitingActivity.class));
                break;
            case R.id.bicy_four:

                Intent bicyintent4 =  new Intent(GuideActivity.this, WebActivity.class);
                bicyintent4.putExtra(ContentValuse.url,Url.reyurnRule);
                startActivity(bicyintent4);
              //  startActivity(new Intent(GuideActivity.this, CarAlsoActivity.class));
                break;
            case  R.id.bicy_five:
                //reyurnRule
                Intent bicyintent5 =  new Intent(GuideActivity.this, WebActivity.class);
                bicyintent5.putExtra(ContentValuse.url,Url.rid_rule);
                startActivity(bicyintent5);
              //  startActivity(new Intent(GuideActivity.this, RidingRulesActivity.class));
                break;
            case R.id.vip_one:

                Intent vipbicyintent =  new Intent(GuideActivity.this, WebActivity.class);
                vipbicyintent.putExtra(ContentValuse.url,Url.recharge);
                startActivity(vipbicyintent);
              //  startActivity(new Intent(GuideActivity.this, AnnualFeeActivity.class));
                break;
            case R.id.vip_two:
                Intent intent =  new Intent(GuideActivity.this, WebActivity.class);
                intent.putExtra(ContentValuse.url,Url.recharge_HB);
                startActivity(intent);
                break;
            case R.id.vip_three:
                Intent intent1 =  new Intent(GuideActivity.this, WebActivity.class);
                intent1.putExtra(ContentValuse.url,Url.Vip_refund);
                startActivity(intent1);
                break;
            case R.id.name_one:
                Intent intent2 =  new Intent(GuideActivity.this, WebActivity.class);
                intent2.putExtra(ContentValuse.url,Url.real_name);
                startActivity(intent2);
                break;
            case R.id.name_two:
                Intent intent3 =  new Intent(GuideActivity.this, WebActivity.class);
                intent3.putExtra(ContentValuse.url,Url.authentication);
                startActivity(intent3);
                break;
            case R.id.name_three:
                Intent intent4 =  new Intent(GuideActivity.this, WebActivity.class);
                intent4.putExtra(ContentValuse.url,Url.fail);
                startActivity(intent4);
                break;
            case R.id.party:
                Intent intent5 =  new Intent(GuideActivity.this, WebActivity.class);
                intent5.putExtra(ContentValuse.url,Url.activity_rule);
                startActivity(intent5);
                break;
            case R.id.my_one:
                Intent mintent =  new Intent(GuideActivity.this, WebActivity.class);
                mintent.putExtra(ContentValuse.url,Url.publicurl);
                startActivity(mintent);
                break;
            case R.id.my_two:
                Intent intent6 =  new Intent(GuideActivity.this, WebActivity.class);
                intent6.putExtra(ContentValuse.url,Url.contact_service);
                startActivity(intent6);
                break;

            case R.id.insurance_one:
                Intent intent7 =  new Intent(GuideActivity.this, WebActivity.class);
                intent7.putExtra(ContentValuse.url,Url.cedicalInsurance);
                startActivity(intent7);
                break;

            case R.id.insurance_two:
                Intent intent8 =  new Intent(GuideActivity.this, WebActivity.class);
                intent8.putExtra(ContentValuse.url,Url.compensation);
                startActivity(intent8);

                break;


            case R.id.insurance_three:

                Intent intent9 =  new Intent(GuideActivity.this, WebActivity.class);
                intent9.putExtra(ContentValuse.url,Url.danger);
                startActivity(intent9);
                break;

            case R.id.line_six:
             startActivity(new Intent(GuideActivity.this, AllProblemActivity.class));
                break;
        }
    }
}
