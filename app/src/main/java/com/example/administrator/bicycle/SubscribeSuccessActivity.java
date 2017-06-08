package com.example.administrator.bicycle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SubscribeSuccessActivity extends AppCompatActivity {

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SubscribeSuccessActivity.this.finish();
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_subscribe_success);
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
        tvtitle.setText("预约成功");

       new Thread(){
           @Override
           public void run() {
               try {
                   sleep(3000);
               }catch (Exception e){
                   e.getSuppressed();
               }
              Message msg = new Message();
               mhandler.sendMessage(msg);

           }
       }.start();


    }
}
