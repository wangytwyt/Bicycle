package com.example.administrator.bicycle.Personal.zhaobudaoche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.TextUtiil;

public class ZhaobudaocheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_zhaobudaoche);
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
        tvtitle.setText("找不到车");


    }
}
