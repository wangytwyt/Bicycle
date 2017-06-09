package com.example.administrator.bicycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.TextUtiil;
import com.sofi.smartlocker.ble.util.LOG;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);
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
        TextView text = (TextView) findViewById(R.id.tv_text);
        tvtitle.setText("帮助");
        Intent intent = getIntent();
        int tag = intent.getIntExtra(ContentValuse.Text, -1);
        switch (tag) {
            case ContentValuse.SubscribeHelp:

                text.setText(TextUtiil.setTxt(this, R.raw.help));
                break;
            case ContentValuse.PHelp:
                text.setText("我们的共享单车无固定停车点，可随时取用，结束取用后，将车辆停放至道路两旁的安全区域，方便他人取用。");
                break;
            case ContentValuse.OpenHelp:
                text.setText(TextUtiil.setTxt(this, R.raw.open));
                break;
            case ContentValuse.CloseHelp:
                text.setText("我们的共享单车无固定停车点，可随时取用，结束取用后，将车辆停放至道路两旁的安全区域，方便他人取用。");
                break;
        }

    }




}
