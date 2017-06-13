package com.example.administrator.bicycle.manageactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

public class AllRepairBicycleActivity extends AppCompatActivity implements View.OnClickListener{
private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_all_repair_bicycle);
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
        tvtitle.setText("车辆维修");

        text = (TextView) findViewById(R.id.tv_text);
        text.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.tv_cleaner).setOnClickListener(this);
        findViewById(R.id.ll_one).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cleaner:
                text.setText("");
                break;
            case R.id.ll_one:
                startActivity(new Intent(this,RepairBicycleActivity.class));
                break;
        }

    }
}
