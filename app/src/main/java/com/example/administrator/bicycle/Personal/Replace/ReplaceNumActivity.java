package com.example.administrator.bicycle.Personal.Replace;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.bicycle.Personal.InformationActivity;
import com.example.administrator.bicycle.Personal.UpdatePhoneActivity;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;

public class ReplaceNumActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_replace_num);

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
        tvtitle.setText("手机号");

        TextView tvphone = (TextView) findViewById(R.id.tv_phone);
        tvphone.setText("15243255136");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ReplaceNumActivity.this, UpdatePhoneActivity.class);
                startActivityForResult(intent2, ContentValuse.requestCodePhone);
            }
            });
    }
}
