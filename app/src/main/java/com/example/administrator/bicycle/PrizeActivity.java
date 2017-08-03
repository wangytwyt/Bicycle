package com.example.administrator.bicycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.bicycle.util.ContentValuse;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PrizeActivity extends Activity {
    private final int one = 0X124557;
    private final int two = 0X124558;
    private boolean israndom = true;
    private Timer timer;

    private int day;


    private TextView tvone, tvtwo;
    private Button butstart, butend;
    private Handler mhandler
            = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setText(msg);
            if (!israndom) {
                timer.cancel();
                Intent intent = new Intent(PrizeActivity.this, PrizeDialogActivity.class);
                intent.putExtra(ContentValuse.prize, day);
                PrizeActivity.this.startActivityForResult(intent, ContentValuse.prizeResult);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_prize);
        initView();
    }

    private void initView() {

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
        }, 0, 30);        //从1000ms即1s开始，30ms为数字改变周期
    }


    private void setText(Message msg) {
        day = msg.what;
        tvone.setText(msg.what / 10 + "");
        tvtwo.setText(msg.what % 10 + "");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        if (requestCode == ContentValuse.prizeResult) {
           finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
