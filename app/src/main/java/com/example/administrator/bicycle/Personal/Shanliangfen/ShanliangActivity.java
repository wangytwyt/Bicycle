package com.example.administrator.bicycle.Personal.Shanliangfen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bicycle.KindHistoryActivity;
import com.example.administrator.bicycle.R;

public class ShanliangActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout guizejiedu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shanliang);


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("善良分");

        guizejiedu = (LinearLayout) findViewById(R.id.line_guizejiedu);
        guizejiedu.setOnClickListener(this);

       findViewById(R.id.iv_kindhistory).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_guizejiedu:
                startActivity(new Intent(ShanliangActivity.this,GuizeActivity.class));
                break;
            case R.id.iv_kindhistory:
                startActivity(new Intent(ShanliangActivity.this,KindHistoryActivity.class));
                break;
        }
    }
}
