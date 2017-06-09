package com.example.administrator.bicycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.util.ContentValuse;

public class AssistantActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_p, tv_openhelp, tv_close, tv_billing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_assistant);
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
        tvtitle.setText("帮助");

        findViewById(R.id.tv_p).setOnClickListener(this);
        findViewById(R.id.tv_openhelp).setOnClickListener(this);
        findViewById(R.id.tv_close).setOnClickListener(this);
        findViewById(R.id.tv_billing).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_p:
                Intent intent =   new Intent(AssistantActivity.this,HelpActivity.class);
                intent.putExtra(ContentValuse.Text,ContentValuse.PHelp);
                startActivity(intent);
                break;
            case R.id.tv_openhelp:
                Intent intent1 =   new Intent(AssistantActivity.this,HelpActivity.class);
                intent1.putExtra(ContentValuse.Text,ContentValuse.OpenHelp);
                startActivity(intent1);
                break;
            case R.id.tv_close:
                Intent intent3 =   new Intent(AssistantActivity.this,HelpActivity.class);
                intent3.putExtra(ContentValuse.Text,ContentValuse.CloseHelp);
                startActivity(intent3);
                break;
            case R.id.tv_billing:
                Intent intent2 =   new Intent(AssistantActivity.this,HelpActivity.class);
                intent2.putExtra(ContentValuse.Text,ContentValuse.SubscribeHelp);
                startActivity(intent2);
                break;


        }
    }
}
