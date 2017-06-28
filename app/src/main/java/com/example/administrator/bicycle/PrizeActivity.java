package com.example.administrator.bicycle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PrizeActivity extends AppCompatActivity {
    private final int one = 0X124557;
    private final int two = 0X124558;
    private boolean israndom = true;
    private Timer timer;


    private TextView tvone, tvtwo;
    private Button butstart, butend;
    private Handler mhandler
            = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setText(msg);
            if (!israndom) {
                timer.cancel();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_prize);
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
        tvtitle.setText("抽奖");

        tvone = (TextView) findViewById(R.id.tv_one);
        tvtwo = (TextView) findViewById(R.id.tv_two);


        butstart = (Button) findViewById(R.id.but_start);
        butend = (Button) findViewById(R.id.but_end);

        butstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                israndom = true;
                random();
                butstart.setEnabled(false);
                butend.setEnabled(true);


            }
        });

        butend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butstart.setEnabled(true);
                butend.setEnabled(false);
                israndom = false;

            }
        });

    }


    private void random() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message mm = new Message();
                mm.what = (int) (Math.random() * 60 + 30);
                mhandler.sendMessage(mm);
            }
        }, 1000, 30);        //从1000ms即1s开始，30ms为数字改变周期
    }


    private void setText(Message msg) {
        tvone.setText(msg.what / 10 + "");
        tvtwo.setText(msg.what % 10 + "");
    }


}
