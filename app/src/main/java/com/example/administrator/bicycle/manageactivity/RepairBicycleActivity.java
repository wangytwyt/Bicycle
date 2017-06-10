package com.example.administrator.bicycle.manageactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.view.CheckBoxView;

public class RepairBicycleActivity extends AppCompatActivity {
    private CheckBoxView cbxv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_repair_bicycle);

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


        cbxv =(CheckBoxView) findViewById(R.id.cbxv);
        cbxv.setCols(3);
        cbxv.setDataResource(this,R.array.bicycle_repair);


    }



}
