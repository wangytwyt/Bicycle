package com.example.administrator.bicycle;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.bicycle.util.TimeUtils;

public class testActivity extends AppCompatActivity {
private TextView tv_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv_time = (TextView) findViewById(R.id.tv_time);


        findViewById(R.id.but).setOnClickListener(new startBtnListener());
        player();
    }


    private void player(){
        MediaPlayer  mediaPlayer = MediaPlayer.create(this,R.raw.openloke);
        mediaPlayer.start();
    }









    private Handler stepTimeHandler;
    private Runnable mTicker;
    long startTime = 0;

    //开始按钮
    class startBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button b = (Button)v;
            String buttonText = b.getText().toString();
            if("Start".equalsIgnoreCase(buttonText)){
                b.setText("Stop");
                // 清零 开始计时
                tv_time.setText("00:00:00");
                stepTimeHandler = new Handler();
                startTime = System.currentTimeMillis();
                mTicker = new Runnable() {
                    public void run() {
                        String content = TimeUtils.showTimeCount(System.currentTimeMillis() - startTime);
                        tv_time.setText(content);

                        long now = SystemClock.uptimeMillis();
                        long next = now + (1000 - now % 1000);
                        stepTimeHandler.postAtTime(mTicker, next);
                    }
                };
                //启动计时线程，定时更新
                mTicker.run();
            }else{
                b.setText("Start");
                //停止计时 Remove any pending posts of Runnable r that are in the message queue.
                stepTimeHandler.removeCallbacks(mTicker);
            }
        }
    }
}
