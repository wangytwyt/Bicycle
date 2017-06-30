package com.example.administrator.bicycle;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.bicycle.util.ContentValuse;

public class PrizeDialogActivity extends Activity {

    private TextView tv_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_prize_dialog);

        Intent intent = getIntent();
        int day = intent.getIntExtra(ContentValuse.prize,-1);


        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_day.setText(day+"");

        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
